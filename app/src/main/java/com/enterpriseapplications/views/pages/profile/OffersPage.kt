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
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.config.authentication.AuthenticatedUser
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Offer
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.viewmodel.profile.OfferPageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.OfferCard
import com.enterpriseapplications.views.ProductCard
import com.enterpriseapplications.views.pages.search.MissingItems
import com.enterpriseapplications.views.pages.search.PageShower
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersPage(navController: NavHostController)
{
    val viewModel: OfferPageViewModel = viewModel(factory = viewModelFactory)
    val refreshState: SwipeRefreshState = SwipeRefreshState(isRefreshing = false)
    val currentSelectedTab: State<Int> = viewModel.currentSelectedTab.collectAsState()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 2.dp)) {
        TopAppBar(title = {
            Text(text = "Offers", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },modifier = Modifier.fillMaxWidth())
        SwipeRefresh(state = refreshState, onRefresh = {viewModel.initialize()}) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)) {
                TabRow(selectedTabIndex = currentSelectedTab.value) {
                    Tab(icon = {
                        Icon(imageVector = Icons.Filled.LocalOffer, contentDescription = null)
                    }, selected = currentSelectedTab.value == 0, onClick = {
                        viewModel.updateCurrentSelectedTab(0)}, text = {
                        Text(text = "Created Offers", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    })
                    Tab(icon = {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = null)
                    }, selected = currentSelectedTab.value == 1, onClick = {
                        viewModel.updateCurrentSelectedTab(1)}, text = {
                        Text(text = "Received Offers", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    })
                }
                if(currentSelectedTab.value == 0)
                    CreatedOffers(navController,viewModel)
                else
                    ReceivedOffers(navController,viewModel)
            }
        }
    }
}
@Composable
private fun CreatedOffers(navController: NavHostController,viewModel: OfferPageViewModel) {
    val currentOffers: State<List<Offer>> = viewModel.currentCreatedOffers.collectAsState()
    val currentOffersPage: State<Page> = viewModel.currentCreatedOffersPage.collectAsState()
    val currentOffersSearching: State<Boolean> = viewModel.currentCreatedOffersSearching.collectAsState()
    val lazyState: LazyListState = rememberLazyListState()
    val bottomReached by remember {
        derivedStateOf {
            lazyState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage(0)
    }
    Column(modifier = Modifier.padding(2.dp)) {
        PageShower(page = currentOffersPage.value)
        if (currentOffersSearching.value)
            ProgressIndicator()
        else {
            if (currentOffersPage.value.totalElements > 0) {
                LazyColumn(
                    state = lazyState,
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalArrangement = Arrangement.Top,
                    content = {
                        itemsIndexed(items = currentOffers.value) { index, item ->
                            Box(modifier = Modifier.padding(2.dp)) {
                                OfferCard(navController,item, receiver = false)
                            }
                        }
                    })
            } else
                MissingItems(
                    buttonText = "Reload",
                    missingText = "No created offers found, set is empty",
                    callback = { viewModel.resetTab(0)})
        }
    }
}
@Composable
private fun ReceivedOffers(navController: NavHostController,viewModel: OfferPageViewModel) {
    val currentOffers: State<List<Offer>> = viewModel.currentReceivedOffers.collectAsState()
    val currentOffersPage: State<Page> = viewModel.currentReceivedOffersPage.collectAsState()
    val currentOffersSearching: State<Boolean> = viewModel.currentReceivedOffersSearching.collectAsState()
    val lazyState: LazyListState = rememberLazyListState()
    val bottomReached by remember {
        derivedStateOf {
            lazyState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage(1)
    }
    Column(modifier = Modifier.padding(2.dp)) {
        PageShower(modifier = Modifier.padding(vertical = 10.dp),page = currentOffersPage.value)
        if (currentOffersSearching.value)
            ProgressIndicator()
        else {
            if (currentOffersPage.value.totalElements > 0) {
                LazyColumn(
                    state = lazyState,
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalArrangement = Arrangement.Top,
                    content = {
                        itemsIndexed(items = currentOffers.value) { index, item ->
                            Box(modifier = Modifier.padding(2.dp)) {
                                OfferCard(navController,item, receiver = true)
                            }
                        }
                    })
            } else
                MissingItems(
                    buttonText = "Reload",
                    missingText = "No received offers have been found, set is empty",
                    callback = { viewModel.resetTab(1)})
        }
    }
}