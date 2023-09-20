package com.enterpriseapplications.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.enterpriseapplications.R
import com.enterpriseapplications.views.lists.HomePage
import com.enterpriseapplications.views.lists.ProfileList
import com.enterpriseapplications.views.lists.SearchList


val screens = listOf(
    Screen.Profile,
    Screen.Search,
    Screen.Home,
    Screen.Settings
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBarController(navController: NavHostController) {
    Scaffold(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp), bottomBar = {
        NavigationBar(modifier = Modifier.padding(5.dp)){
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            screens.forEachIndexed{index, screen ->
                NavigationBarItem(icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = null)}, selected = currentDestination?.hierarchy?.any {it.route == screen.route} == true, onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
        }
    })
    {
        NavHost(navController, startDestination = Screen.Home.route,Modifier.padding(it)) {
            composable(Screen.Home.route) { HomePage(navController = navController)}
            composable(Screen.Search.route) { SearchList(navController = navController)}
            composable(Screen.Profile.route) { ProfileList(navController = navController)}
            composable(Screen.Settings.route) { ProfileList(navController = navController)}
        }
    }
}

sealed class Screen(val route: String, @StringRes resourceID: Int) {
    object Profile: Screen("profile", R.string.profile)
    object Search: Screen("search",R.string.search)
    object Home: Screen("home",R.string.home)
    object Settings: Screen("Settings",R.string.settings)
}