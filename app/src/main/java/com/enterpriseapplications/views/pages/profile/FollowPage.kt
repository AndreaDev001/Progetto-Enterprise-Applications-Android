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
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Follow
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.viewmodel.profile.FollowPageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.UserCard
import com.enterpriseapplications.views.pages.search.MissingItems
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowPage(navController: NavHostController) {
    val viewModel: FollowPageViewModel = viewModel(factory = viewModelFactory)
    val userID: UUID = UUID.fromString("064a18ac-3fd9-40d5-9ed9-ac9d682852c6");
    val currentSelectedTab: State<Int> = viewModel.currentSelectedTab.collectAsState();
    viewModel.userID = userID
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
            if(currentSelectedTab.value == 0) {
                FollowersList(viewModel = viewModel)
            }
            else
            {
                FollowedList(viewModel = viewModel)
            }
        }
    }
}
@Composable
private fun FollowersList(viewModel: FollowPageViewModel) {
    val currentFollowers: State<List<Follow>> = viewModel.currentFollowers.collectAsState()
    val currentFollowersPage: State<Page> = viewModel.currentFollowersPage.collectAsState()
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
        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = "${currentFollowersPage.value.number + 1} page", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            Text(text = "${currentFollowersPage.value.totalPages} total pages", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            Text(text = "${currentFollowersPage.value.totalElements} total elements", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
        }
        if(currentFollowersPage.value.totalElements > 0) {
            LazyVerticalGrid(modifier = Modifier
                .padding(vertical = 2.dp)
                .fillMaxWidth(),state = lazyGridState, columns = GridCells.Fixed(2)) {
                itemsIndexed(items = currentFollowers.value) {index,item ->
                    Box(modifier = Modifier.padding(2.dp)) {
                        UserCard(user = item.follower)
                    }
                }
            }
        }
        else
            MissingItems(callback = {}, missingText = "No followers found, set is empty")
    }
}
@Composable
private fun FollowedList(viewModel: FollowPageViewModel) {

    val currentFollowed: State<List<Follow>> = viewModel.currentFollows.collectAsState()
    val currentFollowedPage: State<Page> = viewModel.currentFollowsPage.collectAsState()
    val lazyListState: LazyListState = rememberLazyListState()
    val bottomReached by remember {
        derivedStateOf {
            lazyListState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage(1)
    }
    Column(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = "${currentFollowedPage.value.number + 1} page", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            Text(text = "${currentFollowedPage.value.totalPages} total pages", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            Text(text = "${currentFollowedPage.value.totalElements} total elements", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
        }
        if(currentFollowedPage.value.totalElements > 0) {
            LazyColumn(modifier = Modifier
                .padding(vertical = 2.dp)
                .fillMaxWidth(),state = lazyListState, horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
                itemsIndexed(items = currentFollowed.value) {index,item ->
                    Box(modifier = Modifier.padding(2.dp)) {
                        UserCard(user = item.followed)
                    }
                }
            }
        }
        else
            MissingItems(callback = {viewModel.resetSearch(1)})
    }
}