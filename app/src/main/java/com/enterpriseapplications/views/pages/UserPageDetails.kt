package com.enterpriseapplications.views.pages

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.enterpriseapplications.config.authentication.AuthenticatedUser
import com.enterpriseapplications.config.authentication.AuthenticationManager
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
import com.enterpriseapplications.views.UserImage
import com.enterpriseapplications.views.alerts.create.CreateReport
import com.enterpriseapplications.views.alerts.create.CreateReview
import com.enterpriseapplications.views.pages.search.MissingItems
import com.enterpriseapplications.views.pages.search.PageShower
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPageDetails(navController: NavHostController,userID: String?) {
    val viewModel: UserDetailsViewModel = viewModel(factory = viewModelFactory)
    val currentUsersDetails: State<UserDetails?> = viewModel.currentUserDetails.collectAsState()
    val authenticatedUser: State<AuthenticatedUser?> = AuthenticationManager.currentUser.collectAsState()
    viewModel.userID = UUID.fromString(userID)
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
                                UserImage(contentScale = ContentScale.Crop, userID = userID!!,size = 120.dp)
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
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    if (currentUsersDetails.value!!.rating != null)
                                        RatingComponent(
                                            rating = currentUsersDetails.value!!.rating!!.toInt(),
                                            iconSize = 20.dp
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
                            val amountOfFollowers: State<Int?> = viewModel.currentAmountOfFollowers.collectAsState()
                            Column(modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 10.dp)) {
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
                                            amountOfFollowers.value!!.toString()
                                        );
                                        val followedAmount = DescriptionItem(
                                            "Followed",
                                            currentUsersDetails.value!!.amountOfFollowed.toString()
                                        )
                                        DescriptionItem(descriptionItem = followerAmount, headerFontSize = 15.sp, contentTextSize = 15.sp)
                                        DescriptionItem(descriptionItem = followedAmount, headerFontSize = 15.sp, contentTextSize = 15.sp)
                                    }
                                    val createReport = remember { mutableStateOf(false) }
                                    if(createReport.value)
                                        CreateReport(userID = UUID.fromString(userID!!), confirmReportCallback = {createReport.value = false}, dismissCallback = {createReport.value = false}, cancelCallback = {createReport.value = false})
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        val hasFollow: State<Boolean> = viewModel.hasFollow.collectAsState()
                                        val hasReport: State<Boolean> = viewModel.hasReport.collectAsState()
                                        val currentText: String = if(hasFollow.value) "Remove Follow" else "Add Follow";
                                        if(authenticatedUser.value!!.userID.toString() != userID) {
                                            Button(onClick = {
                                                if(hasFollow.value)
                                                    viewModel.removeFollow();
                                                else
                                                    viewModel.addFollow();
                                            }) {
                                                Text(text = currentText, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                            }
                                            if(!hasReport.value) {
                                                Button(onClick = {createReport.value = true}) {
                                                    Text(text = "Report", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
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
                val createReview = remember {mutableStateOf(false)}
                val searchingReview: State<Boolean> = viewModel.searchingReview.collectAsState()
                val hasReview: State<Boolean> = viewModel.hasReview.collectAsState()
                if(createReview.value)
                    CreateReview(userID = UUID.fromString(userID!!), update = false, confirmCallback = {createReview.value = false}, cancelCallback = {createReview.value = false})
                if(searchingReview.value)
                    ProgressIndicator()
                else
                {
                    if(authenticatedUser.value!!.userID.toString() != userID && !hasReview.value) {
                        Button(modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),shape = RoundedCornerShape(5.dp),onClick = {createReview.value = true}) {
                            Text(text = "Write a review", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                if(currentSelectedTab.value == 0)
                    ReviewList(viewModel = viewModel)
                else
                    ProductList(navController = navController,viewModel = viewModel)
            }
        }
    }
}
@Composable
private fun ReviewList(viewModel: UserDetailsViewModel) {
    val currentReviews: State<List<Review>> = viewModel.currentReviews.collectAsState()
    val currentReviewsSearching: State<Boolean> = viewModel.currentReviewsSearching.collectAsState()
    val authenticatedUser: State<AuthenticatedUser?> = AuthenticationManager.currentUser.collectAsState()
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
            if (currentReviews.value.isNotEmpty()) {
                LazyColumn(
                    state = lazyState,
                    modifier = Modifier.padding(vertical = 2.dp),
                    content = {
                        itemsIndexed(items = currentReviews.value) { _, item ->
                            Box(modifier = Modifier.padding(5.dp)) {
                                ReviewCard(item, confirmCallback = {viewModel.initialize()}, receiver = authenticatedUser.value!!.userID == UUID.fromString(item.receiver.id));
                            }
                        }
                    })
            } else
                MissingItems(buttonText = "Reset search", callback = { viewModel.resetTab(1)})
        }
    }
}
@Composable
private fun ProductList(navController: NavHostController,viewModel: UserDetailsViewModel) {
    val currentProducts: State<List<Product>> = viewModel.currentProducts.collectAsState()
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
            if (currentProducts.value.isNotEmpty()) {
                LazyVerticalGrid(
                    state = lazyGridState,
                    modifier = Modifier.padding(vertical = 2.dp),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.Top,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    content = {
                        itemsIndexed(items = currentProducts.value) { _, item ->
                            Box(modifier = Modifier.padding(5.dp)) {
                                ProductCard(navController,item)
                            }
                        }
                    })
            } else
                MissingItems(buttonText = "Reset search", callback = { viewModel.resetTab(1)})
        }
    }
}