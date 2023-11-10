package com.enterpriseapplications.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.enterpriseapplications.R
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.views.pages.HomePage
import com.enterpriseapplications.views.lists.ProfileList
import com.enterpriseapplications.views.lists.SearchList
import com.enterpriseapplications.views.pages.SettingsPage
import com.enterpriseapplications.views.pages.AddProduct
import com.enterpriseapplications.views.pages.CheckoutPage
import com.enterpriseapplications.views.pages.LoginPage
import com.enterpriseapplications.views.screens.MainGraphHolder
import com.enterpriseapplications.views.pages.ProductPageDetails
import com.enterpriseapplications.views.pages.UserPageDetails
import com.enterpriseapplications.views.pages.profile.AddressesPage
import com.enterpriseapplications.views.pages.profile.ConversationPage
import com.enterpriseapplications.views.pages.profile.FollowPage
import com.enterpriseapplications.views.pages.profile.LikedProductsPage
import com.enterpriseapplications.views.pages.profile.MessagePage
import com.enterpriseapplications.views.pages.profile.OffersPage
import com.enterpriseapplications.views.pages.profile.OrdersPage
import com.enterpriseapplications.views.pages.profile.PaymentMethodsPage
import com.enterpriseapplications.views.pages.profile.ReviewsPage
import com.enterpriseapplications.views.pages.search.SearchBans
import com.enterpriseapplications.views.pages.search.SearchProducts
import com.enterpriseapplications.views.pages.search.SearchReports
import com.enterpriseapplications.views.pages.search.SearchUsers
import com.enterpriseapplications.views.screens.LoginScreen
import com.enterpriseapplications.views.screens.SplashScreen
import java.util.UUID


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
    "Settings"
)


object NavigationGraph {
    const val rootGraph = "rootGraph";
    const val mainGraph = "mainGraph";
}

@Composable
fun NavigationGraph(navController: NavHostController, authenticationManager: AuthenticationManager) {
    NavHost(navController = navController, startDestination = Screen.Splash.route, route = NavigationGraph.rootGraph) {
        run {
            composable(route = Screen.Splash.route) { SplashScreen(
                navController = navController,
                authenticationManager = authenticationManager
            )}
            composable(route = Screen.Login.route) { LoginScreen()}
            composable(route = Screen.Main.route) {
                MainGraphHolder(authenticationManager)
            }
        }
    }
}
@Composable
fun MainGraph(navController: NavHostController,authenticationManager: AuthenticationManager) {
    NavHost(navController,route = NavigationGraph.mainGraph, startDestination = Destination.Home.route, modifier = Modifier.padding(2.dp)) {
        composable(Destination.Login.route) {
            LoginPage(
                navController = navController,
                authenticationManager = authenticationManager
            )
        }
        composable(Destination.Home.route) { HomePage(navController = navController) }
        composable(Destination.Search.route) { SearchList(navController = navController) }
        composable(Destination.Add.route) { AddProduct(navController = navController) }
        composable(Destination.Profile.route) {
            ProfileList(
                navController = navController,
                authenticationManager
            )
        }
        composable(Destination.Settings.route) { SettingsPage(navController = navController) }

        composable(Destination.Search.SearchProducts.route) { SearchProducts(navController = navController) }
        composable(Destination.Search.SearchUsers.route) { SearchUsers(navController = navController) }
        composable(Destination.Search.SearchReports.route) { SearchReports(navController = navController) }
        composable(Destination.Search.SearchBans.route) { SearchBans(navController = navController) }

        composable(Destination.Profile.Message.route) { backStackEntry ->
            val conversationID: String? = backStackEntry.arguments?.getString("conversationID");
            MessagePage(navController = navController, conversationID = conversationID!!)
        }
        composable(Destination.Profile.Conversation.route) { ConversationPage(navController = navController) }
        composable(Destination.Profile.Product.route) { backStackEntry ->
            val productID: String? = backStackEntry.arguments?.getString("productID");
            ProductPageDetails(navController, productID = productID)
        }
        composable(Destination.Profile.LikedProducts.route) { LikedProductsPage(navController = navController) }
        composable(Destination.Profile.Reviews.route) { ReviewsPage(navController = navController) }
        composable(Destination.Profile.Orders.route) { OrdersPage(navController = navController) }
        composable(Destination.Profile.Follows.route) { FollowPage(navController = navController) }
        composable(Destination.Profile.ProfilePage.route) { backStackEntry ->
            val userID: UUID? = UUID.fromString(backStackEntry.arguments?.getString("userID"))
            UserPageDetails(navController = navController, userID = userID)
        }
        composable(Destination.Profile.Offers.route) { OffersPage(navController = navController) }
        composable(Destination.Profile.Addresses.route) { AddressesPage(navController = navController) }
        composable(Destination.Profile.PaymentMethods.route) { PaymentMethodsPage(navController = navController) }
        composable(Destination.Profile.Checkout.route) { backStackEntry ->
            val productID: String? = backStackEntry.arguments?.getString("productID")
            val requiredPrice: String? = backStackEntry.arguments?.getString("requiredPrice")
            CheckoutPage(navController = navController, productID, requiredPrice)
        }
    }
}

sealed class Screen(val route: String) {
    object Splash: Screen("splash")
    object Login: Screen("login")
    object Main: Screen("main");
}
sealed class Destination(val route: String, @StringRes resourceID: Int) {
    object Profile: Destination("profile", R.string.profile) {
        object PaymentMethods: Destination("paymentMethodsPage",R.string.paymentMethods)
        object Message: Destination("messagePage/{conversationID}",R.string.messagePage)
        object Conversation: Destination("conversationPage",R.string.conversationPage)
        object Product: Destination("productPage/{productID}",R.string.productPage)
        object ProfilePage: Destination("profilePage/{userID}",R.string.profilePage)
        object LikedProducts: Destination("likedProducts",R.string.likedProducts)
        object Checkout: Destination("checkoutPage/{productID}/{requiredPrice}",R.string.checkoutPage)
        object Orders: Destination("orders",R.string.orders)
        object Reviews: Destination("reviews",R.string.reviews)
        object Offers: Destination("offers",R.string.offers)
        object Follows: Destination("follows/{userID}",R.string.follows)
        object Addresses: Destination("addresses",R.string.addresses)
    }
    object Search: Destination("search",R.string.search) {
        object SearchProducts: Destination("searchProducts",R.string.searchProducts)
        object SearchUsers: Destination("searchUsers",R.string.searchUsers)
        object SearchReports: Destination("searchReports",R.string.searchReports)
        object SearchBans: Destination("searchBans",R.string.searchBans)
    }
    object Add: Destination("add",R.string.add);
    object Home: Destination("home",R.string.home)
    object Settings: Destination("Settings",R.string.settings)
    object Login: Destination("Login",0)
}