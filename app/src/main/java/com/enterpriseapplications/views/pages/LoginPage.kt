package com.enterpriseapplications.views.pages

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.config.authentication.AuthenticatedUser
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.viewmodel.LoginPageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LoginPage(navController: NavHostController,authenticationManager: AuthenticationManager) {
    val viewModel: LoginPageViewModel = viewModel(factory = viewModelFactory)
    val authenticatedUser: State<AuthenticatedUser?> = AuthenticationManager.currentUser.collectAsState()
    if(authenticatedUser.value != null) {
        navController.navigate("home")
    }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.completeLoginRequest(it)
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

            Text(text = "You must be logged in to use the application",modifier = Modifier.fillMaxWidth().padding(5.dp))
            Button(onClick = {viewModel.createLoginRequest(launcher)},shape = RoundedCornerShape(5.dp),modifier = Modifier.padding(10.dp)) {
                Text(text = "Perform Login", fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.fillMaxWidth().padding(2.dp))
            }
        }
}