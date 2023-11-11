package com.enterpriseapplications.config.authentication

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.Display.Mode
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.enterpriseapplications.ConnectionBuilderForTesting
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.navigation.Screen
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenRequest
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.Base64;
import java.util.UUID

class AuthenticationManager(val application: CustomApplication)
{
    private lateinit var authorizationServiceConfiguration: AuthorizationServiceConfiguration;
    private var authorizationService: AuthorizationService;
    private var appAuthConfiguration: AppAuthConfiguration = AppAuthConfiguration.Builder().setSkipIssuerHttpsCheck(true).setConnectionBuilder(ConnectionBuilderForTesting.INSTANCE).build();
    private var _hasLoaded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    lateinit var navController: NavHostController;

    companion object
    {

        private var _currentUser: MutableStateFlow<AuthenticatedUser?> = MutableStateFlow(null);
        private var _currentToken: MutableStateFlow<TokenData?> = MutableStateFlow(null)
        val currentUser: StateFlow<AuthenticatedUser?> = _currentUser.asStateFlow()
        val currentToken: StateFlow<TokenData?> = _currentToken.asStateFlow()
        private const val authorizationServerIpAddress: String = "enterpriseapplications.live:9000";
        const val issuerURI: String = "http://$authorizationServerIpAddress"
        const val tokenURI: String = "http://$authorizationServerIpAddress/oauth2/token"
        const val redirectURI: String = "clowning://moose.ac"
        const val clientID: String = "client"
        const val clientSecret: String = "secret"
    }

    init
    {
        authorizationService = AuthorizationService(application.getContext()!!,appAuthConfiguration);
        AuthorizationServiceConfiguration.fetchFromIssuer(Uri.parse(issuerURI),{ configuration: AuthorizationServiceConfiguration?, exception: AuthorizationException? ->
            if(configuration != null && exception == null) {
                this.authorizationServiceConfiguration = configuration
                val preferences: Pair<TokenData?,AuthenticatedUser?>? = this.readPreferences()
                if(preferences != null) {

                    
                }
                _hasLoaded.value = true
            }
        }, ConnectionBuilderForTesting.INSTANCE)
    }

    private fun isExpired(exp: Long): Boolean {
        val time = System.currentTimeMillis() / 1000;
        return time >= exp;
    }

    fun createLoginRequest(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
        val redirect = Uri.parse(redirectURI)
        val authorizationRequest: AuthorizationRequest = AuthorizationRequest.Builder(authorizationServiceConfiguration,
            clientID,ResponseTypeValues.CODE, redirect).setScopes("openid").build();
        val intent = authorizationService.getAuthorizationRequestIntent(authorizationRequest);
        launcher.launch(intent);
    }

    fun completeLoginRequest(activityResult: ActivityResult) {
        val response: AuthorizationResponse? = AuthorizationResponse.fromIntent(activityResult.data!!);
        val exception: AuthorizationException? = AuthorizationException.fromIntent(activityResult.data!!);
        if(exception == null && response != null) {
            val secret: ClientSecretBasic = ClientSecretBasic(clientSecret)
            val tokenRequest: TokenRequest = response.createTokenExchangeRequest()
            authorizationService.performTokenRequest(tokenRequest,secret) {result,error ->
                Log.d("AUTH","TOKEN REQUEST CONFIRMED")
                if(result != null && error == null) {
                    Log.d("AUTH","TOKEN REQUEST SUCCESS")
                    val accessToken: String? = result.accessToken
                    val refreshToken: String? = result.refreshToken
                    val idToken: String? = result.idToken;
                    Log.d("ACCESS_TOKEN",accessToken!!)
                    Log.d("REFRESH_TOKEN",refreshToken!!)
                    Log.d("ID_TOKEN",idToken!!)
                    val tokenData: TokenData = TokenData(accessToken,refreshToken,idToken);
                    val authenticatedUser: AuthenticatedUser = this.readFromClaims(accessToken);
                    this.updatePreferences(tokenData,authenticatedUser)
                    navController.popBackStack(navController.backQueue[0].destination.id,true)
                    navController.navigate(Screen.Main.route)
                }
            }
        }
    }
    @Synchronized
    fun updatePreferences(requiredTokenData: TokenData?,requiredUserData: AuthenticatedUser?) {
        _currentUser.value = requiredUserData
        _currentToken.value = requiredTokenData
        val preferences: SharedPreferences = application.getContext()!!.getSharedPreferences("auth", Context.MODE_PRIVATE)
        if(requiredTokenData != null && requiredUserData != null) {
            preferences.edit().putString("token_data", Gson().toJson(_currentToken.value)).apply();
            preferences.edit().putString("authenticated_user",Gson().toJson(_currentUser.value)).apply()
        }
        else
        {
            preferences.edit().remove("token-data").apply();
            preferences.edit().remove("authenticated_user").apply();
        }
    }
    private fun readPreferences(): Pair<TokenData?,AuthenticatedUser>? {
        val preferences: SharedPreferences = application.getContext()!!.getSharedPreferences("auth",Context.MODE_PRIVATE)
        val tokenData: String? = preferences.getString("token_data",null)
        val authenticatedUser: String? = preferences.getString("authenticated_user",null)
        if(tokenData != null && authenticatedUser != null) {
            return Pair(Gson().fromJson(tokenData,TokenData::class.java),Gson().fromJson(authenticatedUser,AuthenticatedUser::class.java));
        }
        return null;
    }

    @Synchronized
    fun refreshToken(value: Pair<TokenData?,AuthenticatedUser?>): Pair<TokenData?,AuthenticatedUser?>? {
        if(value.first!= null && value.second != null) {
            if(!isExpired(value.second!!.tokenExpiration))
                return Pair(value.first!!, value.second!!);
            val requestBody: RequestBody = FormBody.Builder().add("grant_type",GrantTypeValues.REFRESH_TOKEN).add("refresh_token", currentToken.value!!.refreshToken).build();
            val request: Request = Request.Builder().url(tokenURI).post(requestBody).addHeader("Authorization", "Basic ${Base64.getEncoder().encodeToString("client:secret".toByteArray())}").build()
            try
            {
                val response = OkHttpClient.Builder().build().newCall(request).execute();
                val tokenResponse = JSONObject(response.body!!.string());
                val accessToken: String = tokenResponse.getString("access_token");
                val refreshToken: String = tokenResponse.getString("refresh_token");
                val idToken: String = tokenResponse.getString("id_token");
                val tokenData: TokenData = TokenData(accessToken,refreshToken,idToken);
                val authenticatedUser: AuthenticatedUser = this.readFromClaims(accessToken);
                this.updatePreferences(tokenData,authenticatedUser);
                return Pair(tokenData,authenticatedUser)
            }
            catch (exception: Exception) {
                exception.printStackTrace();
                CoroutineScope(Dispatchers.Main).launch {
                    navController.navigate(Screen.Login.route)
                    updatePreferences(null,null)
                }
            }
        }
        return null;
    }
    private fun readFromClaims(accessToken: String): AuthenticatedUser {
        val accessTokenClaims = JSONObject(String(Base64.getDecoder().decode(accessToken.split(".")[1])));
        val roles: JSONArray = accessTokenClaims.getJSONArray("roles");
        val values: MutableList<String> = mutableListOf()
        for(i in 0 until roles.length())
            values.add(roles.getString(i));
        return AuthenticatedUser(UUID.fromString(accessTokenClaims.getString("sub")),accessTokenClaims.getString("email"),accessTokenClaims.getString("username"),accessTokenClaims.getLong("exp"),values);
    }
    fun dispose() {
        this.authorizationService.dispose();
    }
    val hasLoaded: StateFlow<Boolean> = _hasLoaded.asStateFlow()
}