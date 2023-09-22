package com.enterpriseapplications.views.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.enterpriseapplications.navigation.icons


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileList(navController: NavHostController)
{
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
            Text(text = "Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }, navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        })
        Column(modifier = Modifier.padding(vertical = 2.dp)) {
            MenuItem(callback = {navController.navigate("profilePage")}, leadingIcon = Icons.Filled.AccountCircle , headerText = "Profile", supportingText = "View your own profile")
            MenuItem(callback = {navController.navigate("likedProducts")}, leadingIcon = Icons.Filled.ThumbUp, headerText = "Liked Products", supportingText = "View the products you liked")
            MenuItem(callback = {navController.navigate("orders")}, leadingIcon = Icons.Filled.ShoppingCart, headerText = "Orders", supportingText = "View the orders you created previously")
            MenuItem(callback = {navController.navigate("offers")}, leadingIcon = Icons.Filled.Star, headerText = "Offers", supportingText = "View the offers you created and received")
            MenuItem(callback = {navController.navigate("reviews")}, leadingIcon = Icons.Filled.AccountBox, headerText = "Reviews", supportingText = "View the reviews you made and received")
            MenuItem(callback = {navController.navigate("addresses")}, leadingIcon = Icons.Filled.LocationOn , headerText = "Addresses", supportingText = "View the addresses that have been registered for you")
        }
    }
}