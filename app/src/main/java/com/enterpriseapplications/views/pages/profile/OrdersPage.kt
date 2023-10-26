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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.config.authentication.AuthenticatedUser
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Order
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.viewmodel.profile.OrderPageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.OrderCard
import com.enterpriseapplications.views.ProductCard
import com.enterpriseapplications.views.pages.search.MissingItems
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersPage(navController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 2.dp)) {
        val viewModel: OrderPageViewModel = viewModel(factory = viewModelFactory)
        TopAppBar(title = {
            Text(text = "Orders", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },modifier = Modifier.fillMaxWidth())
        val refreshState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
        SwipeRefresh(state = refreshState, onRefresh = {viewModel.initialize()}) {
            Column(modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()) {
                Text(text = "Here you can see all of the orders you have created", fontSize = 15.sp, fontWeight = FontWeight.Normal)
                ItemList(viewModel = viewModel)
            }
        }
    }
}
@Composable
private fun ItemList(viewModel: OrderPageViewModel) {
    val currentOrders: State<List<Order>> = viewModel.orders.collectAsState()
    val currentOrdersPage: State<Page> = viewModel.ordersPage.collectAsState()
    val currentOrdersSearching: State<Boolean> = viewModel.ordersSearching.collectAsState()
    val lazyState: LazyListState = rememberLazyListState()
    val bottomReached by remember {
        derivedStateOf {
            lazyState.isScrolledToEnd()
        }
    }
    LaunchedEffect(bottomReached) {
        viewModel.updateCurrentPage()
    }
    if(currentOrdersSearching.value)
        ProgressIndicator()
    else
    {
        Column(modifier = Modifier.padding(2.dp)) {
            Column(modifier = Modifier.padding(5.dp)) {
                Text(text = "${currentOrdersPage.value.number + 1} page", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
                Text(text = "${currentOrdersPage.value.totalPages} total pages", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
                Text(text = "${currentOrdersPage.value.totalElements} total elements", fontSize = 15.sp,modifier = Modifier.padding(vertical = 2.dp))
            }
            if(currentOrdersPage.value.totalElements > 0) {
                LazyColumn(state = lazyState,modifier = Modifier.padding(vertical = 2.dp), verticalArrangement = Arrangement.Top,content = {
                    itemsIndexed(items = currentOrders.value) {index,item ->
                        Box(modifier = Modifier.padding(2.dp)) {
                            OrderCard(order = item)
                        }
                    }
                })
            }
            else
                MissingItems(buttonText = "Reload", missingText = "No orders created, set is empty", callback = {viewModel.resetSearch()})
        }
    }
}