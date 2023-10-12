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
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.viewmodel.HomePageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.ProductCard
import com.enterpriseapplications.views.pages.search.MissingItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavHostController) {
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
        Column(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .verticalScroll(ScrollState(0))) {
            Text(modifier = Modifier.padding(horizontal = 2.dp), fontSize = 15.sp, fontWeight = FontWeight.Medium, text = "In this page you can find the most recently created products and the most liked ones, if you want to make an accurate search use the available search pages")
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                RecentlyCreated(viewModel = viewModel)
                MostLiked(viewModel = viewModel)
                MostExpensive(viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun RecentlyCreated(viewModel: HomePageViewModel) {
    Column(modifier = Modifier.padding(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Recently Created", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Icon(imageVector = Icons.Filled.Timer, contentDescription = null,modifier = Modifier.size(25.dp))
        }
        Text(modifier = Modifier.padding(2.dp),text = "Here you can see the product that have been just created", fontSize = 15.sp, fontWeight = FontWeight.Thin)
    }
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
            viewModel.updateRecentProducts(true);
        }
        if(currentRecentlyCreatedProducts.value.isNotEmpty()) {
            LazyRow(state = lazyListState,modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)) {
                itemsIndexed(currentRecentlyCreatedProducts.value) {index,item ->
                    Box(modifier = Modifier.padding(2.dp)) {
                        ProductCard(product = item)
                    }
                }
            }
        }
        else
            MissingItems(callback = {viewModel.updateRecentProducts(page = false,first = true)})
    }
}
@Composable
private fun MostLiked(viewModel: HomePageViewModel) {
    Column(modifier = Modifier.padding(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Most Liked Products", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Icon(modifier = Modifier
                .padding(horizontal = 2.dp)
                .size(25.dp),imageVector = Icons.Filled.ThumbUp,contentDescription = null)
        }
        Text(modifier = Modifier.padding(2.dp),text = "Here you can see the most liked products", fontSize = 15.sp, fontWeight = FontWeight.Thin)
    }
    val currentMostLikedProducts: State<List<Product>> = viewModel.currentMostLikedProducts.collectAsState()
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
            viewModel.updateMostLikedProducts(true);
        }
        if(currentMostLikedProducts.value.isNotEmpty()) {
            LazyRow(state = lazyListState,modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)) {
                itemsIndexed(items = currentMostLikedProducts.value) { index,item ->
                    Box(modifier = Modifier.padding(2.dp)) {
                        ProductCard(product = item)
                    }
                }
            }
        }
        else
            MissingItems(callback = {viewModel.updateMostLikedProducts(page = false,first = true)})
    }
}
@Composable
private fun MostExpensive(viewModel: HomePageViewModel) {
    Column(modifier = Modifier.padding(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Most Expensive Products", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Icon(modifier = Modifier
                .padding(horizontal = 2.dp)
                .size(25.dp),imageVector = Icons.Filled.MonetizationOn,contentDescription = null)
        }
        Text(modifier = Modifier.padding(2.dp),text = "Here you can see the most expensive products", fontSize = 15.sp, fontWeight = FontWeight.Thin)
    }
    val currentMostExpensiveProducts: State<List<Product>> = viewModel.currentMostExpensiveProducts.collectAsState()
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
            viewModel.updateMostLikedProducts(true);
        }
        if(currentMostExpensiveProducts.value.isNotEmpty()) {
            LazyRow(state = lazyListState,modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)) {
                itemsIndexed(items = currentMostExpensiveProducts.value) { index,item ->
                    Box(modifier = Modifier.padding(2.dp)) {
                        ProductCard(product = item)
                    }
                }
            }
        }
        else
            MissingItems(callback = {viewModel.updateMostExpensiveProducts(page = false,first = true)})
    }
}