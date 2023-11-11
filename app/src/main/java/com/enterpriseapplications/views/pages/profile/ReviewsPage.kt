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
import com.enterpriseapplications.config.authentication.AuthenticatedUser
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Review
import com.enterpriseapplications.viewmodel.profile.ReviewPageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.ReviewCard
import com.enterpriseapplications.views.UserCard
import com.enterpriseapplications.views.pages.search.MissingItems
import com.enterpriseapplications.views.pages.search.PageShower
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.net.Authenticator
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsPage(navController: NavHostController) {
    val viewModel: ReviewPageViewModel = viewModel(factory = viewModelFactory)
    val authenticatedUser: State<AuthenticatedUser?> = AuthenticationManager.currentUser.collectAsState()
    viewModel.userID = authenticatedUser.value!!.userID;
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
        val refreshState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
        SwipeRefresh(state = refreshState, onRefresh = {viewModel.initialize()}) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp))
            {
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
                        WrittenReviews(authenticatedUser.value)
                    else
                        ReceivedReviews(authenticatedUser.value)
                }
            }
        }
    }
}
@Composable
private fun WrittenReviews(authenticatedUser: AuthenticatedUser?) {
    val viewModel: ReviewPageViewModel = viewModel(factory = viewModelFactory)
    val currentWrittenReviews: State<List<Review>> = viewModel.writtenReviews.collectAsState()
    val currentWrittenReviewsPage: State<Page> = viewModel.writtenReviewsPage.collectAsState()
    val currentWrittenReviewsSearching: State<Boolean> = viewModel.writtenReviewsSearching.collectAsState()
    val lazyListState: LazyListState = rememberLazyListState()
    val bottomReached by remember {
        derivedStateOf {
            lazyListState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage(0)
    }
    Column(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()) {
        PageShower(page = currentWrittenReviewsPage.value)
        if(currentWrittenReviewsSearching.value)
            ProgressIndicator()
        else
        {
            if(currentWrittenReviewsPage.value.totalElements > 0) {
                LazyColumn(
                    state = lazyListState, modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    itemsIndexed(items = currentWrittenReviews.value) { index, item ->
                        ReviewCard(review = item, receiver = authenticatedUser!!.userID == item.receiver.id)
                    }
                }
            }
            else
                MissingItems(callback = {viewModel.resetTab(0);viewModel.initialize()}, missingText = "No followers found, set is empty")
        }
    }
}
@Composable
private fun ReceivedReviews(authenticatedUser: AuthenticatedUser?) {
    val viewModel: ReviewPageViewModel = viewModel(factory = viewModelFactory)
    val currentReceivedReviews: State<List<Review>> = viewModel.receivedReviews.collectAsState()
    val currentReceivedReviewsPage: State<Page> = viewModel.receivedReviewsPage.collectAsState()
    val currentReceivedReviewsSearching: State<Boolean> = viewModel.receivedReviewsSearching.collectAsState()
    val lazyListState: LazyListState = rememberLazyListState()
    val bottomReached by remember {
        derivedStateOf {
            lazyListState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage(1)
    }
    Column(modifier = Modifier.padding(5.dp).fillMaxWidth()) { PageShower(page = currentReceivedReviewsPage.value)
        if(currentReceivedReviewsSearching.value)
            ProgressIndicator()
        else
        {
            if(currentReceivedReviewsPage.value.totalElements > 0) {
                LazyColumn(state = lazyListState, modifier = Modifier.fillMaxWidth().padding(5.dp)
                ) {
                    itemsIndexed(items = currentReceivedReviews.value) { index, item ->
                        ReviewCard(review = item, confirmCallback = {viewModel.resetTab(1);viewModel.initialize()}, receiver = authenticatedUser!!.userID == item.receiver.id)
                    }
                }
            }
            else
                MissingItems(callback = {viewModel.resetTab(1);viewModel.initialize()}, missingText = "No followers found, set is empty")
        }
    }
}