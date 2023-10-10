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
import com.enterpriseapplications.ConnectionBuilderForTesting
import com.enterpriseapplications.CustomApplication
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
import org.json.JSONObject
import java.util.Base64;
import java.util.UUID

class AuthenticationManager(val application: CustomApplication)
{
    private var authorizationServiceConfiguration: AuthorizationServiceConfiguration;
    private var authorizationService: AuthorizationService;
    private var appAuthConfiguration: AppAuthConfiguration;

    companion object
    {
        private var _currentUser: MutableStateFlow<AuthenticatedUser?> = MutableStateFlow(null);
        private var _currentToken: MutableStateFlow<TokenData?> = MutableStateFlow(null)
        val currentUser: StateFlow<AuthenticatedUser?> = _currentUser.asStateFlow()
        val currentToken: StateFlow<TokenData?> = _currentToken.asStateFlow()
        val authorizeURI: String = "http://192.168.1.74:9000/oauth2/authorize"
        val tokenURI: String = "http://192.168.1.74:9000/oauth2/token"
        val redirectURI: String = "clowning://moose.ac"
        val clientID: String = "client"
        val clientSecret: String = "secret"
    }

    init
    {
        val authorize = Uri.parse(authorizeURI);
        val token = Uri.parse(tokenURI);
        appAuthConfiguration = AppAuthConfiguration.Builder().setSkipIssuerHttpsCheck(true).setConnectionBuilder(ConnectionBuilderForTesting.INSTANCE).build();
        authorizationServiceConfiguration = AuthorizationServiceConfiguration(authorize,token)
        authorizationService = AuthorizationService(application.getContext()!!,appAuthConfiguration);
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
                    this.updatePreferences(tokenData,authenticatedUser);
                }
            }
        }
    }
    @Synchronized
    fun updatePreferences(requiredTokenData: TokenData?,requiredUserData: AuthenticatedUser?) {
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
        _currentToken.value = requiredTokenData
        _currentUser.value = requiredUserData
    }
    private fun readPreferences(): Pair<TokenData,AuthenticatedUser>? {
        val preferences: SharedPreferences = application.getContext()!!.getSharedPreferences("auth",Context.MODE_PRIVATE)
        val tokenData: String? = preferences.getString("token_data",null)
        val authenticatedUser: String? = preferences.getString("authenticated_user",null)
        if(tokenData != null && authenticatedUser != null) {
            return Pair(Gson().fromJson(tokenData,TokenData::class.java),Gson().fromJson(authenticatedUser,AuthenticatedUser::class.java));
        }
        return null;
    }

    @Synchronized
    fun refreshToken(): Pair<TokenData,AuthenticatedUser>? {
        if(currentUser.value != null && currentToken.value != null) {
            val expired = (System.currentTimeMillis() / 1000) >= currentUser.value!!.tokenExpiration;
            if(!expired) {
                return Pair(currentToken.value!!, currentUser.value!!)
            }
            val requestBody: RequestBody = FormBody.Builder().add("grant_type",GrantTypeValues.REFRESH_TOKEN).add("refresh_token", currentToken.value!!.refreshToken).build();
            val request: Request = Request.Builder().url(tokenURI).post(requestBody).addHeader("Authorization", "Basic ${Base64.getEncoder().encodeToString("secret".toByteArray())}").build()
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
            }
        }
        return null;
    }
    private fun readFromClaims(accessToken: String): AuthenticatedUser {
        val accessTokenClaims = JSONObject(String(Base64.getDecoder().decode(accessToken.split(".")[1])));
        return AuthenticatedUser(UUID.fromString(accessTokenClaims.getString("sub")),accessTokenClaims.getString("email"),accessTokenClaims.getString("username"),accessTokenClaims.getString("name"),accessTokenClaims.getString("surname"),accessTokenClaims.getLong("exp"));
    }
    fun dispose() {
        this.authorizationService.dispose();
    }
}