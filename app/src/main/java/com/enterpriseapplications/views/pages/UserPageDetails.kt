package com.enterpriseapplications.views.pages

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.Review
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.viewmodel.UserDetailsViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.DescriptionItem
import com.enterpriseapplications.views.ProductCard
import com.enterpriseapplications.views.RatingComponent
import com.enterpriseapplications.views.ReviewCard
import com.enterpriseapplications.views.pages.search.MissingItems
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPageDetails(navController: NavHostController) {
    val userID: UUID = UUID.fromString("196967df-d0ec-44db-9042-39abffdf3fa2");
    val viewModel: UserDetailsViewModel = viewModel(factory = viewModelFactory)
    val currentUsersDetails: State<UserDetails?> = viewModel.currentUserDetails.collectAsState()
    viewModel.userID = userID
    viewModel.initialize()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 2.dp, horizontal = 5.dp)
    ) {
        TopAppBar(title = {
            Text(text = "User Details", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }, modifier = Modifier.fillMaxWidth())
        val refreshState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
        SwipeRefresh(state = refreshState, onRefresh = {}) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                if (currentUsersDetails.value != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                AsyncImage(
                                    model = "https://as1.ftcdn.net/v2/jpg/03/46/83/96/1000_F_346839683_6nAPzbhpSkIpb8pmAwufkC7c5eD7wYws.jpg",
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(60))
                                        .size(140.dp)
                                )
                                Column(modifier = Modifier.padding(2.dp)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(2.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = currentUsersDetails.value!!.username,
                                            modifier = Modifier.padding(vertical = 1.dp),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    if (currentUsersDetails.value!!.rating != null)
                                        RatingComponent(
                                            rating = currentUsersDetails.value!!.rating!!.toInt(),
                                            iconSize = 35.dp
                                        )
                                    if (currentUsersDetails.value!!.name != null && currentUsersDetails.value!!.surname != null) {
                                        Row(
                                            modifier = Modifier
                                                .padding(2.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = currentUsersDetails.value!!.name!!,
                                                modifier = Modifier.padding(2.dp)
                                            )
                                            Text(
                                                text = currentUsersDetails.value!!.surname!!,
                                                modifier = Modifier.padding(2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            Column(modifier = Modifier.weight(1f).padding(vertical = 10.dp)) {
                                Column(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val followerAmount = DescriptionItem(
                                            "Followers",
                                            currentUsersDetails.value!!.amountOfFollowers.toString()
                                        );
                                        val followedAmount = DescriptionItem(
                                            "Followed",
                                            currentUsersDetails.value!!.amountOfFollowed.toString()
                                        )
                                        DescriptionItem(descriptionItem = followerAmount, headerFontSize = 15.sp, contentTextSize = 15.sp)
                                        DescriptionItem(descriptionItem = followedAmount, headerFontSize = 15.sp, contentTextSize = 15.sp)
                                    }
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Button(onClick = {}) {
                                            Text(text = "Follow", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Button(onClick = {}) {
                                            Text(text = "Report", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                val currentSelectedTab: State<Int> = viewModel.currentSelectedTab.collectAsState()
                TabRow(selectedTabIndex = currentSelectedTab.value,modifier = Modifier.fillMaxWidth()) {
                    Tab(selected = currentSelectedTab.value == 0, onClick = {viewModel.updateCurrentTab(0)},text = {
                        Text(text = "Reviews", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }, icon = {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = null)
                    })
                    Tab(selected = currentSelectedTab.value == 1,onClick = {viewModel.updateCurrentTab(1)}, text = {
                        Text(text = "Products", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    },icon = {
                        Icon(imageVector = Icons.Filled.ShoppingCart,contentDescription = null)
                    })
                }
                if(currentSelectedTab.value == 0)
                    ReviewList(viewModel = viewModel)
                else
                    ProductList(viewModel = viewModel)
            }
        }
    }
}
@Composable
private fun ReviewList(viewModel: UserDetailsViewModel) {
    val currentReviews: State<List<Review>> = viewModel.currentReviews.collectAsState()
    val currentReviewsPage: State<Page> = viewModel.currentReviewsPage.collectAsState()
    val currentReviewsSearching: State<Boolean> = viewModel.currentReviewsSearching.collectAsState()
    val lazyState: LazyListState = rememberLazyListState()
    val bottomReached by remember {
        derivedStateOf {
            lazyState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage(1);
    }
    if(currentReviewsSearching.value)
        ProgressIndicator()
    else {
        Column(modifier = Modifier.padding(5.dp)) {
            Column(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = "${currentReviewsPage.value.number + 1} page",
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = "${currentReviewsPage.value.totalPages} total pages",
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = "${currentReviewsPage.value.totalElements} total elements",
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            if (currentReviewsPage.value.totalElements > 0) {
                LazyColumn(
                    state = lazyState,
                    modifier = Modifier.padding(vertical = 2.dp),
                    content = {
                        itemsIndexed(items = currentReviews.value) { _, item ->
                            Box(modifier = Modifier.padding(5.dp)) {
                                ReviewCard(item)
                            }
                        }
                    })
            } else
                MissingItems(buttonText = "Reset search", callback = { viewModel.resetTab(1)})
        }
    }
}
@Composable
private fun ProductList(viewModel: UserDetailsViewModel) {
    val currentProducts: State<List<Product>> = viewModel.currentProducts.collectAsState()
    val currentProductsPage: State<Page> = viewModel.currentProductsPage.collectAsState()
    val currentProductsSearching: State<Boolean> = viewModel.currentProductsSearching.collectAsState()
    val lazyGridState: LazyGridState = rememberLazyGridState()
    val bottomReached by remember {
        derivedStateOf {
            lazyGridState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage(1);
    }
    if(currentProductsSearching.value)
        ProgressIndicator()
    else {
        Column(modifier = Modifier.padding(5.dp)) {
            Column(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = "${currentProductsPage.value.number + 1} page",
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = "${currentProductsPage.value.totalPages} total pages",
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = "${currentProductsPage.value.totalElements} total elements",
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            if (currentProductsPage.value.totalElements > 0) {
                LazyVerticalGrid(
                    state = lazyGridState,
                    modifier = Modifier.padding(vertical = 2.dp),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.Top,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    content = {
                        itemsIndexed(items = currentProducts.value) { _, item ->
                            Box(modifier = Modifier.padding(5.dp)) {
                                ProductCard(product = item)
                            }
                        }
                    })
            } else
                MissingItems(buttonText = "Reset search", callback = { viewModel.resetTab(1)})
        }
    }
}