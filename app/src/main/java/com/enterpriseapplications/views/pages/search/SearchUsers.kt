package com.enterpriseapplications.views.pages.search

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.viewmodel.search.SearchUsersViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.ProductCard
import com.enterpriseapplications.views.UserCard
import com.enterpriseapplications.views.lists.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchUsers(navController: NavHostController) {
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope()
    val viewModel: SearchUsersViewModel = viewModel(factory = viewModelFactory)
    val controller: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current;

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
        ModalNavigationDrawer(gesturesEnabled = false, drawerState = drawerState, drawerContent = {
            ModalDrawerSheet(drawerShape = RectangleShape) {
                MenuItem(callback = {scope.launch {
                    controller?.hide();
                    drawerState.close()}}, trailingIcon = Icons.Filled.Close, headerText = "Filters", supportingText = "Use the filters to find the desired users")
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
                ItemList(viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun FilterOptions(viewModel: SearchUsersViewModel) {
    val genders: State<List<String>> = viewModel.genders.collectAsState();
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .verticalScroll(ScrollState(0)), horizontalAlignment = Alignment.CenterHorizontally) {
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentUsers(false)}, formControl = viewModel.emailControl, supportingText = "Write the user's email",label = "Email")
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentUsers(false)},formControl = viewModel.nameControl, supportingText = "Write the user's name", label = "Name", placeHolder = "Write a name...")
        CustomTextField(modifier = Modifier.padding(2.dp),valueCallback = {viewModel.updateCurrentUsers(false)},formControl = viewModel.surnameControl, supportingText = "Write the user's surname",label = "Surname", placeHolder = "Write a surname...")
        CustomTextField(modifier = Modifier.padding(2.dp),valueCallback = {viewModel.updateCurrentUsers(false)}, formControl = viewModel.usernameControl, supportingText = "Write the user's username",label = "Username", placeHolder = "Write a username...")
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentUsers(false)},formControl = viewModel.descriptionControl, supportingText = "Write the user's description", label = "Description", placeHolder = "Write a description...")
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentUsers(false)}, formControl = viewModel.minRatingControl, supportingText = "Write the user's minimum rating",label = "Minimum rating", keyboardType = KeyboardType.Number)
        CustomTextField(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentUsers(false)}, formControl = viewModel.maxRatingControl, supportingText = "Write the user's maximum rating",label = "Maximum rating", keyboardType = KeyboardType.Number)
        FormDropdown(modifier = Modifier.padding(2.dp), valueCallback = {viewModel.updateCurrentUsers(false)},formControl = viewModel.genderControl, items = genders.value, label = "Gender")
    }
}

@Composable
private fun ItemList(viewModel: SearchUsersViewModel) {
    val currentPage: State<Int> = viewModel.currentPage.collectAsState()
    val currentTotalPages: State<Int> = viewModel.currentTotalPages.collectAsState()
    val currentTotalElements: State<Int> = viewModel.currentTotalElements.collectAsState()
    val currentUsers: State<List<UserDetails>> = viewModel.currentUsers.collectAsState()
    val lazyGridState: LazyGridState = rememberLazyGridState()
    val bottomReached by remember {
        derivedStateOf {
            lazyGridState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage();
    }
    Column(modifier = Modifier.padding(5.dp)) {
        Text(text = "Use the available filters to find the desired users", fontSize = 18.sp,modifier = Modifier.padding(vertical = 2.dp))
        Text(text = "${currentPage.value + 1} page", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
        Text(text = "${currentTotalPages.value} total pages",fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
        Text(text = "${currentTotalElements.value} total elements",fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
    }
    Column(modifier = Modifier.padding(5.dp)) {
        if(currentTotalElements.value > 0) {
           LazyVerticalGrid(state = lazyGridState, modifier = Modifier.padding(vertical = 2.dp), columns = GridCells.Fixed(2), verticalArrangement = Arrangement.Top, horizontalArrangement = Arrangement.SpaceBetween, content = {
                itemsIndexed(items = currentUsers.value) { _, item ->
                    Box(modifier = Modifier.padding(5.dp)) {
                        UserCard(user = item)
                    }
                }
            })
        }
        else
            MissingItems(buttonText = "Reset Search", callback = {viewModel.resetSearch()})
    }
}