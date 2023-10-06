package com.enterpriseapplications

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.navigation.NavigationBarController
import com.enterpriseapplications.ui.theme.EnterpriseApplicationsAndroidTheme
import com.enterpriseapplications.views.pages.HomePage
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.ResponseTypeValues

class MainActivity : ComponentActivity() {

    private lateinit var service: AuthorizationService;



    override fun onCreate(savedInstanceState: Bundle?) {
        /**
        var appAuthConfiguration: AppAuthConfiguration = AppAuthConfiguration.Builder().setSkipIssuerHttpsCheck(true).setConnectionBuilder(ConnectionBuilderForTesting.INSTANCE).build();
        service = AuthorizationService(this,appAuthConfiguration)**/
        super.onCreate(savedInstanceState)
        val application = (application as CustomApplication)
        setContent {
            EnterpriseApplicationsAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    val launcher =
                        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                            application.authenticationManager.completeLoginRequest(it);
                            /**
                            if (it.resultCode == RESULT_OK) {
                            val exception: AuthorizationException? =
                            AuthorizationException.fromIntent(it.data!!);
                            val response = AuthorizationResponse.fromIntent(it.data!!);
                            if (exception != null) {
                            Log.d("AUTH 2", "ERROR")
                            } else if (response != null) {
                            val secret = ClientSecretBasic("secret");
                            val tokenRequest = response.createTokenExchangeRequest();
                            service.performTokenRequest(tokenRequest,secret) {response,exception ->
                            if(exception == null && response != null) {
                            if(response.accessToken != null && response.refreshToken != null) {
                            Log.d("AUTH", response.accessToken!!);
                            Log.d("AUTH", response.refreshToken!!);
                            }
                            }
                            }
                            }
                            }**/
                            /**
                            if (it.resultCode == RESULT_OK) {
                            val exception: AuthorizationException? =
                            AuthorizationException.fromIntent(it.data!!);
                            val response = AuthorizationResponse.fromIntent(it.data!!);
                            if (exception != null) {
                            Log.d("AUTH 2", "ERROR")
                            } else if (response != null) {
                            val secret = ClientSecretBasic("secret");
                            val tokenRequest = response.createTokenExchangeRequest();
                            service.performTokenRequest(tokenRequest,secret) {response,exception ->
                            if(exception == null && response != null) {
                            if(response.accessToken != null && response.refreshToken != null) {
                            Log.d("AUTH", response.accessToken!!);
                            Log.d("AUTH", response.refreshToken!!);
                            }
                            }
                            }
                            }
                            }**/
                        }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {application.authenticationManager.createLoginRequest(launcher) }) {
                            Text(text = "Perform login");
                        }
                    }
                }
            }
        }
    }

    private fun performAuth() {
        /**val redirectURI = Uri.parse("clowning://moose.ac");
        val authorizeURI = Uri.parse("http://10.0.2.2:9000/oauth2/authorize");
        val tokenURI = Uri.parse("http://10.0.2.2:9000/oauth2/token");
        val config = AuthorizationServiceConfiguration(authorizeURI, tokenURI);
        val request =
            AuthorizationRequest.Builder(config, "client", ResponseTypeValues.CODE, redirectURI)
                .setScope("openid").build();
        val intent = service.getAuthorizationRequestIntent(request);
        launcher.launch(intent);**/
    }
}