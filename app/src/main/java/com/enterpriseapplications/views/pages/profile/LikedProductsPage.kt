package com.enterpriseapplications.views.pages.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Like
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.viewmodel.profile.LikedProductsViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.ProductCard
import com.enterpriseapplications.views.pages.search.MissingItems
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikedProductsPage(navController: NavHostController)
{
    val userID: UUID = UUID.fromString("064a18ac-3fd9-40d5-9ed9-ac9d682852c6");
    val viewModel: LikedProductsViewModel = viewModel(factory = viewModelFactory)
    viewModel.userID = userID
    viewModel.initialize()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 2.dp)) {
        TopAppBar(title = {
            Text(text = "Liked Products", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },modifier = Modifier.fillMaxWidth())
        Column(modifier = Modifier.padding(5.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(2.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(text = "Liked Products", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Icon(imageVector = Icons.Filled.ThumbUp, contentDescription = null,modifier = Modifier.padding(horizontal = 2.dp))
            }
            Text(text = "Here you can see all the products you have liked", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp), fontWeight = FontWeight.Thin)
            LikedProductsLists(viewModel = viewModel)
        }
    }
}
@Composable
private fun LikedProductsLists(viewModel: LikedProductsViewModel) {
    val currentLikes: State<List<Like>> = viewModel.currentLikedProducts.collectAsState()
    val currentLikesPage: State<Page> = viewModel.currentLikedProductsPage.collectAsState()
    val lazyState: LazyGridState = rememberLazyGridState()
    val bottomReached by remember {
        derivedStateOf {
            lazyState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage();
    }
    Column(modifier = Modifier.padding(2.dp)) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = "${currentLikesPage.value.number + 1} page", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            Text(text = "${currentLikesPage.value.totalPages} total pages", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            Text(text = "${currentLikesPage.value.totalElements} total elements", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
        }
        if(currentLikesPage.value.totalElements > 0) {
            LazyVerticalGrid(columns = GridCells.Fixed(2), state = lazyState,modifier = Modifier.padding(vertical = 2.dp), verticalArrangement = Arrangement.Top,content = {
                itemsIndexed(items = currentLikes.value) {index,item ->
                    Box(modifier = Modifier.padding(2.dp)) {
                        ProductCard(product = item.product)
                    }
                }
            })
        }
        else
            MissingItems(buttonText = "Reload", missingText = "No products have been liked, set is empty", callback = {viewModel.resetSearch()})
    }
}