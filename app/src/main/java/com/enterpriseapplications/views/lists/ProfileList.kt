package com.enterpriseapplications.views.lists

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.enterpriseapplications.config.authentication.AuthenticatedUser
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.navigation.icons
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileList(navController: NavHostController,authenticationManager: AuthenticationManager)
{
    val authenticatedUser: State<AuthenticatedUser?> = AuthenticationManager.currentUser.collectAsState()
    val userID: UUID = authenticatedUser.value!!.userID
    Column(modifier = Modifier
        .padding(vertical = 2.dp)
        .verticalScroll(ScrollState(0))) {
        TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
            Text(text = "Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }, navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        })
        Column(modifier = Modifier.padding(vertical = 2.dp)) {
            MenuItem(callback = {navController.navigate("profilePage/$userID")}, leadingIcon = Icons.Filled.AccountCircle , headerText = "Profile", supportingText = "View your own profile")
            MenuItem(callback = {navController.navigate("likedProducts")}, leadingIcon = Icons.Filled.ThumbUp, headerText = "Liked Products", supportingText = "View the products you liked")
            MenuItem(callback = {navController.navigate("conversationPage")},headerText = "Conversations",leadingIcon = Icons.Filled.Chat,supportingText = "Continue to chat with other users")
            MenuItem(callback = {navController.navigate("follows/$userID")},leadingIcon = Icons.Filled.Person,headerText = "Follows", supportingText = "View who you follow and who is following you")
            MenuItem(callback = {navController.navigate("orders")}, leadingIcon = Icons.Filled.ShoppingCart, headerText = "Orders", supportingText = "View the orders you created previously")
            MenuItem(callback = {navController.navigate("offers")}, leadingIcon = Icons.Filled.Star, headerText = "Offers", supportingText = "View the offers you created and received")
            MenuItem(callback = {navController.navigate("reviews")}, leadingIcon = Icons.Filled.AccountBox, headerText = "Reviews", supportingText = "View the reviews you made and received")
            MenuItem(callback = {navController.navigate("paymentMethodsPage")}, headerText = "Payment Methods", supportingText = "View all of your payment methods", leadingIcon = Icons.Filled.Payments)
            MenuItem(callback = {navController.navigate("addresses")}, leadingIcon = Icons.Filled.LocationOn , headerText = "Addresses", supportingText = "View the addresses that have been registered for you")
        }
    }
}