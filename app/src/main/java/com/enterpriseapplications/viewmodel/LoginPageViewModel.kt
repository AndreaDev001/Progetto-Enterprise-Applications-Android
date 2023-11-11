package com.enterpriseapplications.viewmodel

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.enterpriseapplications.CustomApplication

class LoginPageViewModel(val application: CustomApplication): BaseViewModel(application)
{
    fun createLoginRequest(launcher: ManagedActivityResultLauncher<Intent,ActivityResult>) {
        application.authenticationManager.createLoginRequest(launcher);
    }
    fun completeLoginRequest(activityResult: ActivityResult) {
        application.authenticationManager.completeLoginRequest(activityResult);
    }
}