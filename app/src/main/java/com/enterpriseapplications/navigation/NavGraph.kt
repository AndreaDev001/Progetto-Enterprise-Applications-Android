package com.enterpriseapplications.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.enterpriseapplications.R
import com.enterpriseapplications.views.pages.HomePage
import com.enterpriseapplications.views.lists.ProfileList
import com.enterpriseapplications.views.lists.SearchList
import com.enterpriseapplications.views.lists.SettingsList
import com.enterpriseapplications.views.pages.AddProduct
import com.enterpriseapplications.views.pages.profile.AddressesPage
import com.enterpriseapplications.views.pages.profile.LikedProductsPage
import com.enterpriseapplications.views.pages.profile.OffersPage
import com.enterpriseapplications.views.pages.profile.OrdersPage
import com.enterpriseapplications.views.pages.profile.ProfilePage
import com.enterpriseapplications.views.pages.profile.ReviewsPage
import com.enterpriseapplications.views.pages.search.SearchProducts
import com.enterpriseapplications.views.pages.search.SearchUsers


val screens = listOf(
    Screen.Home,
    Screen.Search,
    Screen.Add,
    Screen.Profile,
    Screen.Settings
)
val icons = listOf(
    Icons.Filled.Home,
    Icons.Filled.Search,
    Icons.Filled.AddCircle,
    Icons.Filled.Person,
    Icons.Filled.Settings
)
val texts = listOf(
    "Home",
    "Search",
    "Add",
    "Profile",
    "Settings"g
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBarController(navController: NavHostController) {
    Scaffold(modifier = Modifier.fillMaxWidth(), bottomBar = {
        NavigationBar(modifier = Modifier.padding(5.dp)){
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            screens.forEachIndexed{index, screen ->
                val icon: ImageVector = icons[index]
                val text: String = texts[index]
                NavigationBarItem(label = {
                   Text(text = text, fontSize = 15.sp)
                }, icon = { Icon(imageVector = icon, contentDescription = null)}, selected = currentDestination?.hierarchy?.any {it.route == screen.route} == true, onClick = {
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
            composable(Screen.Home.route) { HomePage(navController = navController) }
            composable(Screen.Search.route) { SearchList(navController = navController)}
            composable(Screen.Add.route) {AddProduct(navController = navController)}
            composable(Screen.Profile.route) { ProfileList(navController = navController)}
            composable(Screen.Settings.route) { SettingsList(navController = navController) }

            composable(Screen.Search.SearchProducts.route) { SearchProducts(navController = navController)}
            composable(Screen.Search.SearchUsers.route) { SearchUsers(navController = navController)}

            composable(Screen.Profile.LikedProducts.route) { LikedProductsPage(navController = navController)}
            composable(Screen.Profile.Reviews.route) {ReviewsPage(navController = navController)}
            composable(Screen.Profile.Orders.route) { OrdersPage(navController = navController)}
            composable(Screen.Profile.ProfilePage.route) {ProfilePage(navController = navController)}
            composable(Screen.Profile.Offers.route) { OffersPage(navController = navController)}
            composable(Screen.Profile.Addresses.route) { AddressesPage(navController = navController)}
        }
    }
}
sealed class Screen(val route: String, @StringRes resourceID: Int) {
    object Profile: Screen("profile", R.string.profile) {
        object ProfilePage: Screen("profilePage",R.string.profilePage)
        object LikedProducts: Screen("likedProducts",R.string.likedProducts)
        object Orders: Screen("orders",R.string.orders)
        object Reviews: Screen("reviews",R.string.reviews)
        object Offers: Screen("offers",R.string.offers)
        object Addresses: Screen("addresses",R.string.addresses)
    }
    object Search: Screen("search",R.string.search) {
        object SearchProducts: Screen("searchProducts",R.string.searchProducts)
        object SearchUsers: Screen("searchUsers",R.string.searchUsers)
    }
    object Add: Screen("add",R.string.add);
    object Home: Screen("home",R.string.home)
    object Settings: Screen("Settings",R.string.settings)
}