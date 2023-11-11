package com.enterpriseapplications.views.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavHostController,authenticationManager: AuthenticationManager) {
    val hasLoaded: State<Boolean> = authenticationManager.hasLoaded.collectAsState()
    LaunchedEffect(key1 = hasLoaded.value) {
        if(hasLoaded.value) {
            navController.popBackStack();
            if(AuthenticationManager.currentUser.value != null && AuthenticationManager.currentToken.value != null) {
                navController.navigate(Screen.Main.route)
            }
            else
            {
                navController.navigate(Screen.Login.route)
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize().animateContentSize(), contentAlignment = Alignment.Center) {
        Icon(imageVector = Icons.Filled.ShoppingBasket,contentDescription = null,modifier = Modifier.size(140.dp))
    }
}