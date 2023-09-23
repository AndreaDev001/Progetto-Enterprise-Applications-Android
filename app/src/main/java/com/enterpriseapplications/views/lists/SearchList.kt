package com.enterpriseapplications.views.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItem(callback: () -> Unit, leadingIcon: ImageVector? = null,trailingIcon: ImageVector = Icons.Filled.ArrowForward, headerText: String, supportingText: String) {
    ListItem(headlineText = {
        Text(text = headerText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }, supportingText = {
        Text(text = supportingText, fontSize = 15.sp, fontWeight = FontWeight.Thin)
    }, leadingContent = {
        if (leadingIcon != null) {
            Icon(imageVector = leadingIcon, contentDescription = null)
        }
    },
        trailingContent = {
            IconButton(onClick = {callback()}) {
                Icon(imageVector = trailingIcon, contentDescription = null)
            }
        }, modifier = Modifier.fillMaxWidth())
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchList(navController: NavHostController)
{
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 2.dp)) {
        TopAppBar(title = {
            Text(text = "Search", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }
        ,modifier = Modifier.fillMaxWidth())
        Column(modifier = Modifier.padding(vertical = 2.dp)) {
            MenuItem(callback = {navController.navigate("searchProducts")}, leadingIcon = Icons.Filled.ShoppingCart, headerText = "Products", supportingText = "Search for products")
            MenuItem(callback = {navController.navigate("searchUsers")}, leadingIcon = Icons.Filled.Person, headerText = "Users", supportingText = "Search for users")
            MenuItem(callback = {navController.navigate("searchReports")},headerText = "Reports", supportingText = "Search for reports")
            MenuItem(callback = {navController.navigate("searchBans")}, headerText = "Bans", supportingText = "Search for bans")
        }
    }
}