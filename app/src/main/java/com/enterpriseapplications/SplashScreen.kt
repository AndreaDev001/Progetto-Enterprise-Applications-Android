package com.enterpriseapplications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.viewmodel.HomePageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.pages.LoginPage

@Composable
fun SplashScreen(navController: NavHostController,authenticationManager: AuthenticationManager) {
    val homePageViewModel: HomePageViewModel = viewModel(factory = viewModelFactory)
    val recentProductsSearching: State<Boolean> = homePageViewModel.currentRecentProductsSearching.collectAsState()
    val likedProductsSearching: State<Boolean> = homePageViewModel.currentMostLikedProductsSearching.collectAsState()
    val expensiveSearching: State<Boolean> = homePageViewModel.currentMostExpensiveProductsSearching.collectAsState()
    val completed = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = recentProductsSearching.value && likedProductsSearching.value && expensiveSearching.value) {
        completed.value = true
    }
    if(!completed.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(modifier = Modifier.size(120.dp),imageVector = Icons.Default.ShoppingBag, contentDescription = null)
        }
    }
    else
        LoginPage(navController = navController, authenticationManager = authenticationManager)
}