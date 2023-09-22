package com.enterpriseapplications.views.pages.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.enterpriseapplications.viewmodel.search.SearchUsersViewModel
import com.enterpriseapplications.views.lists.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUsers(navController: NavHostController) {
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope()
    val viewModel: SearchUsersViewModel = SearchUsersViewModel()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 2.dp)) {
        TopAppBar(title = {
            Text(text = "Search Users", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },modifier = Modifier.fillMaxWidth())
        ModalNavigationDrawer(drawerContent = {
            ModalDrawerSheet(drawerShape = RectangleShape) {
                MenuItem(callback = {scope.launch {drawerState.close()}}, trailingIcon = Icons.Filled.Close, headerText = "Filters", supportingText = "Use the filters to find the desired users")
                Spacer(modifier = Modifier.height(10.dp))
                FilterOptions(viewModel = viewModel)
            }
        }) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = {scope.launch {drawerState.open()}}, modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Filters", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun FilterOptions(viewModel: SearchUsersViewModel) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.nameControl, supportingText = "Write the product's name", label = "Name", placeHolder = "Write a name...")
        CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.surnameControl, supportingText = "Write the product's surname",label = "Surname", placeHolder = "Write a surname...")
        CustomTextField(modifier = Modifier.padding(5.dp), formControl = viewModel.usernameControl, supportingText = "Write the user's username",label = "Username", placeHolder = "Write a username...")
        FormDropdown(modifier = Modifier.padding(5.dp),formControl = viewModel.genderControl, items = listOf("MALE","FEMALE"))
        CustomTextField(modifier = Modifier.padding(5.dp),formControl = viewModel.descriptionControl, supportingText = "Write the user's description", label = "Description", placeHolder = "Write a description...")
    }
}