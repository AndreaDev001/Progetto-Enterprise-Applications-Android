package com.enterpriseapplications.views.pages.search

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.model.Ban
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.viewmodel.search.SearchBansViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.BanCard
import com.enterpriseapplications.views.ProductCard
import com.enterpriseapplications.views.lists.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBans(navController: NavHostController) {
    val viewModel: SearchBansViewModel = viewModel(factory = viewModelFactory)
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 2.dp)) {
        TopAppBar(title = {
            Text(text = "Search Bans", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }, modifier = Modifier.fillMaxWidth())
        ModalNavigationDrawer(drawerState = drawerState, gesturesEnabled = true,drawerContent = {
            ModalDrawerSheet(drawerShape = RectangleShape) {
                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)) {
                    MenuItem(callback = {scope.launch {drawerState.close()}}, trailingIcon = Icons.Filled.Close, headerText = "Filters" , supportingText = "Use the following filters to find the desired products", leadingIcon = null)
                    Spacer(modifier = Modifier.height(10.dp))
                    FilterOptions(viewModel = viewModel)
                }
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
private fun FilterOptions(viewModel: SearchBansViewModel) {
    Column(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .verticalScroll(ScrollState(0)), horizontalAlignment = Alignment.CenterHorizontally) {
        CustomTextField(formControl = viewModel.bannerEmail, supportingText = "Write the banner email", placeHolder = "Write an email...", label = "Banner email")
        CustomTextField(formControl = viewModel.bannedEmail, supportingText = "Write the banned email", placeHolder = "Write an email...", label = "Banned email")
        CustomTextField(formControl = viewModel.bannerUsername, supportingText = "Write the banner username", placeHolder = "Write an username...",label = "Banner username")
        CustomTextField(formControl = viewModel.bannedUsername, supportingText = "Write the banned username", placeHolder = "Write an username...",label = "Banned username")
        FormDropdown(formControl = viewModel.reason, items = listOf("RACISM","NUDITY"), label = "Reason")
    }
}
@Composable
private fun ItemList(viewModel: SearchBansViewModel) {
    val currentBans: State<List<Ban>> = viewModel.currentBans.collectAsState()
    val currentPage: State<Int> = viewModel.currentPage.collectAsState()
    val currentTotalPages: State<Int> = viewModel.currentTotalPages.collectAsState()
    val currentTotalElements: State<Int> = viewModel.currentTotalElements.collectAsState()
    Column(modifier = Modifier.padding(5.dp)) {
        Text(text = "Use the available filters to find the desired bans", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
        Text(text = "${currentPage.value} page", fontSize = 10.sp,modifier = Modifier.padding(vertical = 2.dp))
        Text(text = "${currentTotalPages.value} total pages",fontSize = 10.sp,modifier = Modifier.padding(vertical = 2.dp))
        Text(text = "${currentTotalElements.value} total elements", fontSize = 10.sp,modifier = Modifier.padding(vertical = 2.dp))
        if(currentTotalElements.value > 0) {
            LazyVerticalGrid(modifier = Modifier.padding(vertical = 2.dp), columns = GridCells.Fixed(2), verticalArrangement = Arrangement.Top, horizontalArrangement = Arrangement.SpaceBetween, content = {
                itemsIndexed(items = currentBans.value) { _, item ->
                    Box(modifier = Modifier.padding(5.dp)) {
                        BanCard(ban = item)
                    }
                }
            })
        }
        else
            MissingItems(callback = {viewModel.resetSearch()})
    }
}