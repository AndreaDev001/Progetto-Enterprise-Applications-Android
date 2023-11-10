package com.enterpriseapplications.views.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.navigation.Destination
import com.enterpriseapplications.navigation.MainGraph

val navigationItems = listOf(
    Destination.Home.route,
    Destination.Search.route,
    Destination.Add.route,
    Destination.Profile.route,
    Destination.Settings.route
)
val navigationIcons = listOf(
    Icons.Filled.Home,
    Icons.Filled.Search,
    Icons.Filled.Add,
    Icons.Filled.Person,
    Icons.Filled.Settings
)
val navigationTexts = listOf(
    "Home",
    "Search",
    "Add",
    "Profile",
    "Settings"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGraphHolder(authenticationManager: AuthenticationManager) {
    val navController: NavHostController = rememberNavController()
    val currentNavigation = navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        BottomAppBar(modifier = Modifier.fillMaxWidth()) {
            NavigationBar(modifier = Modifier.fillMaxWidth()) {
                for (i in navigationItems.indices) {
                    NavigationBarItem(icon = {
                        Icon(imageVector = navigationIcons[i], contentDescription = null)
                    },selected = currentNavigation.value?.destination?.route == navigationItems[i], onClick = {
                        navController.navigate(navigationItems[i]) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }, label = {
                        Text(text = navigationTexts[i], fontSize = 12.sp, fontWeight = FontWeight.Normal)
                    })
                }
            }
        }
    }) {
        Box(modifier = Modifier.padding(it)) {
            MainGraph(navController = navController, authenticationManager = authenticationManager)
        }
    }
}