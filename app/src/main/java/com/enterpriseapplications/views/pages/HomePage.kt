package com.enterpriseapplications.views.pages

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.config.authentication.AuthenticatedUser
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.viewmodel.HomePageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.ProductCard
import com.enterpriseapplications.views.pages.search.MissingItems
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavHostController) {
    val swipeRefreshState: SwipeRefreshState = SwipeRefreshState(isRefreshing = false)
    val viewModel: HomePageViewModel = viewModel(factory = viewModelFactory);
    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
            Text(text = "Home", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }, navigationIcon = {
            if(navController.previousBackStackEntry != null)
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
        })
        SwipeRefresh(state = swipeRefreshState, onRefresh = {viewModel.initialize()}) {
            Column(modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .verticalScroll(ScrollState(0))) {
                Text(modifier = Modifier.padding(2.dp), fontSize = 25.sp, fontWeight = FontWeight.Bold,text = "Welcome back")
                Text(modifier = Modifier.padding(horizontal = 2.dp), fontSize = 15.sp, fontWeight = FontWeight.Medium, text = "In this page you can find the most recently created products and the most liked ones, if you want to make an accurate search use the available search pages")
                Column(modifier = Modifier.padding(vertical = 10.dp)) {
                    RecentlyCreated(navController = navController)
                    MostLiked(navController = navController)
                    MostExpensive(navController = navController)
                }
            }
        }
    }
}

@Composable
private fun RecentlyCreated(navController: NavHostController) {
    val viewModel: HomePageViewModel = viewModel(factory = viewModelFactory)
    Column(modifier = Modifier.padding(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Recently Created", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Icon(imageVector = Icons.Filled.Timer, contentDescription = null,modifier = Modifier.size(25.dp))
        }
        Text(modifier = Modifier.padding(2.dp),text = "Here you can see the product that have been just created", fontSize = 15.sp, fontWeight = FontWeight.Thin)
    }
    val isSearching: State<Boolean> = viewModel.currentRecentProductsSearching.collectAsState()
    val currentRecentlyCreatedProducts: State<List<Product>> = viewModel.currentRecentProducts.collectAsState();
    Column(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()) {
        val lazyListState: LazyListState = rememberLazyListState()
        val endReached by remember {
            derivedStateOf {
                lazyListState.isScrolledToEnd()
            }
        }
        LaunchedEffect(endReached) {
            viewModel.updateCurrentPage(0)
        }
        if(isSearching.value)
            ProgressIndicator()
        else
        {
            if(currentRecentlyCreatedProducts.value.isNotEmpty()) {
                LazyRow(state = lazyListState,modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)) {
                    itemsIndexed(currentRecentlyCreatedProducts.value) {index,item ->
                        Box(modifier = Modifier.padding(2.dp)) {
                            ProductCard(navController,product = item)
                        }
                    }
                }
            }
            else
                MissingItems(callback = {viewModel.resetPage(0)})
        }
    }
}
@Composable
private fun MostLiked(navController: NavHostController) {
    val viewModel: HomePageViewModel = viewModel(factory = viewModelFactory)
    Column(modifier = Modifier.padding(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Most Liked Products", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Icon(modifier = Modifier
                .padding(horizontal = 2.dp)
                .size(25.dp),imageVector = Icons.Filled.ThumbUp,contentDescription = null)
        }
        Text(modifier = Modifier.padding(2.dp),text = "Here you can see the most liked products", fontSize = 15.sp, fontWeight = FontWeight.Thin)
    }
    val isSearching: State<Boolean> = viewModel.currentRecentProductsSearching.collectAsState()
    val currentMostLikedProducts: State<List<Product>> = viewModel.currentMostLikedProducts.collectAsState()
    if(isSearching.value)
        ProgressIndicator()
    else
    {
        Column(modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()) {
            val lazyListState: LazyListState = rememberLazyListState()
            val endReached by remember {
                derivedStateOf {
                    lazyListState.isScrolledToEnd()
                }
            }
            LaunchedEffect(endReached) {
                viewModel.updateCurrentPage(1)
            }
            if(currentMostLikedProducts.value.isNotEmpty()) {
                LazyRow(state = lazyListState,modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)) {
                    itemsIndexed(items = currentMostLikedProducts.value) { index,item ->
                        Box(modifier = Modifier.padding(2.dp)) {
                            ProductCard(navController,product = item)
                        }
                    }
                }
            }
            else
                MissingItems(callback = {viewModel.resetPage(1)})
        }
    }
}
@Composable
private fun MostExpensive(navController: NavHostController) {
    val viewModel: HomePageViewModel = viewModel(factory = viewModelFactory)
    Column(modifier = Modifier.padding(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Most Expensive Products", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Icon(modifier = Modifier
                .padding(horizontal = 2.dp)
                .size(25.dp),imageVector = Icons.Filled.MonetizationOn,contentDescription = null)
        }
        Text(modifier = Modifier.padding(2.dp),text = "Here you can see the most expensive products", fontSize = 15.sp, fontWeight = FontWeight.Thin)
    }
    val isSearching: State<Boolean> = viewModel.currentMostExpensiveProductsSearching.collectAsState()
    val currentMostExpensiveProducts: State<List<Product>> = viewModel.currentMostExpensiveProducts.collectAsState()
    if(isSearching.value)
        ProgressIndicator()
    else
    {
        Column(modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()) {
            val lazyListState: LazyListState = rememberLazyListState()
            val endReached by remember {
                derivedStateOf {
                    lazyListState.isScrolledToEnd()
                }
            }
            LaunchedEffect(endReached) {
                viewModel.updateCurrentPage(2)
            }
            if(currentMostExpensiveProducts.value.isNotEmpty()) {
                LazyRow(state = lazyListState,modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)) {
                    itemsIndexed(items = currentMostExpensiveProducts.value) { index,item ->
                        Box(modifier = Modifier.padding(2.dp)) {
                            ProductCard(navController,product = item)
                        }
                    }
                }
            }
            else
                MissingItems(callback = {viewModel.resetPage(2)})
        }
    }
}