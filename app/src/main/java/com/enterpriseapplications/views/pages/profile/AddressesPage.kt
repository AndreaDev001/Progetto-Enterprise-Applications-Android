package com.enterpriseapplications.views.pages.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.model.Address
import com.enterpriseapplications.viewmodel.profile.AddressPageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.AddressCard
import com.enterpriseapplications.views.alerts.create.CreateAddress
import com.enterpriseapplications.views.pages.search.MissingItems
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressesPage(navController: NavHostController)
{
    val viewModel: AddressPageViewModel = viewModel(factory = viewModelFactory)
    val refreshState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 2.dp)) {
        TopAppBar(title = {
            Text(text = "Addresses", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },modifier = Modifier.fillMaxWidth())
        val creatingAddress: MutableState<Boolean> = remember { mutableStateOf(false) }
        val callback = {creatingAddress.value = false}
        if(creatingAddress.value)
            CreateAddress(navController = navController, confirmCallback = {
                viewModel.addAddress(it)
                callback()
            }, dismissCallback = callback, cancelCallback = callback)
        SwipeRefresh(state = refreshState, onRefresh = {viewModel.initialize()}) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
                Text(modifier = Modifier.padding(5.dp),text = "Here you can see your own addresses",fontSize = 15.sp, fontWeight = FontWeight.Normal)
                Button(modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),shape = RoundedCornerShape(5.dp), onClick = {creatingAddress.value = true}) {
                    Text(modifier = Modifier.padding(2.dp),text = "Add a new address",fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                AddressList(viewModel = viewModel)
            }
        }
    }
}
@Composable
private fun AddressList(viewModel: AddressPageViewModel) {
    val currentAddresses: State<List<Address>> = viewModel.currentAddresses.collectAsState()
    val currentAddressesSearching: State<Boolean> = viewModel.currentAddressesSearching.collectAsState()
    Column(modifier = Modifier.padding(5.dp)) {
        if(currentAddressesSearching.value)
            ProgressIndicator()
        else
        {
            if(currentAddresses.value.isNotEmpty()) {
                LazyColumn(modifier = Modifier.padding(2.dp)) {
                    itemsIndexed(currentAddresses.value) {index, item ->
                        Box(modifier = Modifier.padding(2.dp)) {
                            AddressCard(item)
                        }
                    }
                }
            }
            else
                MissingItems(callback = {viewModel.initialize()}, missingText = "No addresses found, set is empty")
        }
    }
}