package com.enterpriseapplications.views.pages.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Person4
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
import com.enterpriseapplications.config.authentication.AuthenticatedUser
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Follow
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.viewmodel.profile.FollowPageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.UserCard
import com.enterpriseapplications.views.pages.search.MissingItems
import com.enterpriseapplications.views.pages.search.PageShower
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowPage(navController: NavHostController) {
    val viewModel: FollowPageViewModel = viewModel(factory = viewModelFactory)
    val currentSelectedTab: State<Int> = viewModel.currentSelectedTab.collectAsState()
    val authenticatedUser: State<AuthenticatedUser?> = AuthenticationManager.currentUser.collectAsState()
    viewModel.userID = authenticatedUser.value!!.userID;
    viewModel.initialize()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 2.dp)
    ) {
        TopAppBar(title = {
            Text(text = "Follows", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }, modifier = Modifier.fillMaxWidth())
        val refreshState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
        SwipeRefresh(state = refreshState , onRefresh = {viewModel.initialize()}) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)) {
                TabRow(selectedTabIndex = currentSelectedTab.value, modifier = Modifier.fillMaxWidth()) {
                    Tab(icon = {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = null)
                    }, selected = currentSelectedTab.value == 0, onClick = { viewModel.updateCurrentSelectedTab(0);viewModel.resetTab(1)}, text = {
                        Text(text = "Followers", fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(10.dp))
                    })
                    Tab(icon = {
                        Icon(imageVector = Icons.Filled.Person,contentDescription = null)
                    },selected = currentSelectedTab.value == 1, onClick = { viewModel.updateCurrentSelectedTab(1);viewModel.resetTab(0)}, text = {
                        Text(text = "Followed", fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(10.dp))
                    })
                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {
                    if(currentSelectedTab.value == 0)
                        FollowersList(navController = navController)
                    else
                        FollowedList(navController = navController)
                }
            }
        }
    }
}
@Composable
private fun FollowersList(navController: NavHostController) {
    val viewModel: FollowPageViewModel = viewModel(factory = viewModelFactory)
    val currentFollowers: State<List<Follow>> = viewModel.currentFollowers.collectAsState()
    val currentFollowersPage: State<Page> = viewModel.currentFollowersPage.collectAsState()
    val currentFollowersSearching: State<Boolean> = viewModel.currentFollowersSearching.collectAsState()
    val lazyGridState: LazyGridState = rememberLazyGridState()
    val bottomReached by remember {
        derivedStateOf {
            lazyGridState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage(0)
    }
    Column(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()) {
        PageShower(page = currentFollowersPage.value)
        if(currentFollowersSearching.value)
            ProgressIndicator()
        else
        {
            if(currentFollowersPage.value.totalElements > 0) {
                LazyVerticalGrid(modifier = Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxWidth(),state = lazyGridState, columns = GridCells.Fixed(2)) {
                    itemsIndexed(items = currentFollowers.value) {index,item ->
                        Box(modifier = Modifier.padding(2.dp)) {
                            UserCard(navHostController = navController,item.follower)
                        }
                    }
                }
            }
            else
                MissingItems(callback = {}, missingText = "No followers found, set is empty")
        }
    }
}
@Composable
private fun FollowedList(navController: NavHostController) {
    val viewModel: FollowPageViewModel = viewModel(factory = viewModelFactory)
    val currentFollowed: State<List<Follow>> = viewModel.currentFollows.collectAsState()
    val currentFollowedPage: State<Page> = viewModel.currentFollowsPage.collectAsState()
    val currentFollowedSearching: State<Boolean> = viewModel.currentFollowsSearching.collectAsState()
    val lazyGridState: LazyGridState = rememberLazyGridState();
    val bottomReached by remember {
        derivedStateOf {
            lazyGridState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage(1)
    }
    Column(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()) {
        PageShower(page = currentFollowedPage.value)
        if(currentFollowedSearching.value)
            ProgressIndicator()
        else
        {
            if(currentFollowedPage.value.totalElements > 0) {
                LazyVerticalGrid(modifier = Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxWidth(),state = lazyGridState,columns = GridCells.Fixed(2)){
                    itemsIndexed(items = currentFollowed.value) {index,item ->
                        Box(modifier = Modifier.padding(2.dp)) {
                            UserCard(navHostController = navController, user = item.followed)
                        }
                    }
                }
            }
            else
                MissingItems(callback = {viewModel.resetSearch(1)})
        }
    }
}