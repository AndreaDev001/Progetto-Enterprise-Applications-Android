package com.enterpriseapplications.views.pages.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Review
import com.enterpriseapplications.viewmodel.profile.ReviewPageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.ReviewCard
import com.enterpriseapplications.views.UserCard
import com.enterpriseapplications.views.pages.search.MissingItems
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsPage(navController: NavHostController) {
    val viewModel: ReviewPageViewModel = viewModel(factory = viewModelFactory)
    val userID: String = "064a18ac-3fd9-40d5-9ed9-ac9d682852c6";
    viewModel.userID = UUID.fromString(userID);
    viewModel.initialize()
    val currentSelectedTab: State<Int> = viewModel.currentSelectedTab.collectAsState()
    Column(modifier = Modifier
        .padding(vertical = 2.dp)
        .fillMaxSize()) {
        TopAppBar(title = {
            Text(text = "Reviews", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },modifier = Modifier.fillMaxWidth())
        TabRow(selectedTabIndex = currentSelectedTab.value,modifier = Modifier.fillMaxWidth()) {
            Tab(icon = {
                Icon(imageVector = Icons.Filled.Person, contentDescription = null)
            }, selected = currentSelectedTab.value == 0, onClick = {
                viewModel.updateSelectedTab(0);viewModel.resetTab(1)}, text = {
                Text(text = "Written Reviews", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            })
            Tab(icon = {
                Icon(imageVector = Icons.Filled.Person,contentDescription = null)
            }, selected = currentSelectedTab.value == 1, onClick = {viewModel.updateSelectedTab(1);viewModel.resetTab(0)},text = {
                Text(text = "Received Reviews", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            })
        }
        Column(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()) {
            if(currentSelectedTab.value == 0)
                WrittenReviews(viewModel = viewModel)
            else
                ReceivedReviews(viewModel = viewModel)
        }
    }
}
@Composable
private fun WrittenReviews(viewModel: ReviewPageViewModel) {
    val currentWrittenReviews: State<List<Review>> = viewModel.writtenReviews.collectAsState()
    val currentWrittenReviewsPage: State<Page> = viewModel.writtenReviewsPage.collectAsState()
    val lazyListState: LazyListState = rememberLazyListState()
    val bottomReached by remember {
        derivedStateOf {
            lazyListState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {

    }
    Column(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = "${currentWrittenReviewsPage.value.number + 1} page", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            Text(text = "${currentWrittenReviewsPage.value.totalPages} total pages", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            Text(text = "${currentWrittenReviewsPage.value.totalElements} total elements", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
        }
        if(currentWrittenReviewsPage.value.totalElements > 0) {
            LazyColumn(
                state = lazyListState, modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                itemsIndexed(items = currentWrittenReviews.value) { index, item ->
                    ReviewCard(review = item)
                }
            }
        }
        else
            MissingItems(callback = {}, missingText = "No followers found, set is empty")
    }
}
@Composable
private fun ReceivedReviews(viewModel: ReviewPageViewModel) {
    val currentReceivedReviews: State<List<Review>> = viewModel.receivedReviews.collectAsState()
    val currentReceivedReviewsPage: State<Page> = viewModel.receivedReviewsPage.collectAsState()
    val lazyListState: LazyListState = rememberLazyListState()
    val bottomReached by remember {
        derivedStateOf {
            lazyListState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {

    }
    Column(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = "${currentReceivedReviewsPage.value.number + 1} page", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            Text(text = "${currentReceivedReviewsPage.value.totalPages} total pages", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            Text(text = "${currentReceivedReviewsPage.value.totalElements} total elements", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
        }
        if(currentReceivedReviewsPage.value.totalElements > 0) {
            LazyColumn(
                state = lazyListState, modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                itemsIndexed(items = currentReceivedReviews.value) { index, item ->
                    ReviewCard(review = item)
                }
            }
        }
        else
            MissingItems(callback = {}, missingText = "No followers found, set is empty")
    }
}