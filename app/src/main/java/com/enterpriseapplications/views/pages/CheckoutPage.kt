package com.enterpriseapplications.views.pages

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CreditCardOff
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.enterpriseapplications.model.Address
import com.enterpriseapplications.model.PaymentMethod
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.viewmodel.CheckoutPageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.AddressCard
import com.enterpriseapplications.views.DescriptionItem
import com.enterpriseapplications.views.PaymentMethodCard
import com.enterpriseapplications.views.ProductImage
import com.enterpriseapplications.views.pages.search.CustomButton
import com.enterpriseapplications.views.pages.search.MissingItems
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.math.BigDecimal
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutPage(navController: NavHostController,productID: String? = null,price: String? = null) {
    val viewModel: CheckoutPageViewModel = viewModel(factory = viewModelFactory)
    val refreshState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    viewModel.reset()
    viewModel.productID = UUID.fromString(productID);
    viewModel.price = price!!.toBigDecimal()
    viewModel.initialize()
    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
            Text(text = "Checkout Page", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }, navigationIcon = {
            if(navController.previousBackStackEntry != null)
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
        })
        SwipeRefresh(state = refreshState, onRefresh = {
            viewModel.initialize()
            viewModel.reset()
        }) {
            val currentAddresses: State<List<Address>> = viewModel.currentAddresses.collectAsState()
            val currentPaymentMethods: State<List<PaymentMethod>> = viewModel.currentPaymentMethods.collectAsState()
            val addressSearching: State<Boolean> = viewModel.currentAddressesSearching.collectAsState()
            val paymentMethodsSearching: State<Boolean> = viewModel.currentPaymentMethodsSearching.collectAsState()
            Column(modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .verticalScroll(ScrollState(0))) {
                if(!addressSearching.value && !paymentMethodsSearching.value && (currentAddresses.value.isEmpty() || currentPaymentMethods.value.isEmpty())) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if(currentPaymentMethods.value.isEmpty()) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Missing payment method", fontSize = 17.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
                                Icon(imageVector = Icons.Default.CreditCardOff,contentDescription = null,modifier = Modifier.padding(2.dp))
                            }
                            Text(modifier = Modifier.padding(2.dp),text = "You must register a payment method before buying a product", fontSize = 15.sp, fontWeight = FontWeight.Normal)
                            CustomButton(modifier = Modifier.fillMaxWidth(),text = "Add a payment method",clickCallback = {navController.navigate("paymentMethodsPage")})
                        }
                        if(currentAddresses.value.isEmpty()) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Missing address", fontSize = 17.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
                                Icon(imageVector = Icons.Default.LocationOff,contentDescription = null,modifier = Modifier.padding(2.dp))
                            }
                            Text(modifier = Modifier.padding(2.dp),text = "You must register an address before buying a product",fontSize = 15.sp, fontWeight = FontWeight.Normal)
                            CustomButton(modifier = Modifier.fillMaxWidth(),text = "Add an address", clickCallback = {navController.navigate("addresses")})
                        }
                    }
                }
                else
                {
                    val success: State<Boolean> = viewModel.success.collectAsState()
                        Column(modifier = Modifier.padding(vertical = 10.dp)) {
                            ProductDetails(viewModel = viewModel,price = price)
                            Spacer(modifier = Modifier.height(2.dp))
                            PaymentMethodsList(viewModel = viewModel)
                            Spacer(modifier = Modifier.height(2.dp))
                            AddressesList(viewModel = viewModel)
                            Spacer(modifier = Modifier.height(2.dp))
                            OptionsSelected(viewModel = viewModel)
                            Spacer(modifier = Modifier.height(2.dp))
                            ButtonSection(viewModel = viewModel)
                        }
                    if(success.value)
                        SuccessHandler(navController = navController)
                }
            }
        }
    }
}
@Composable
private fun ProductDetails(viewModel: CheckoutPageViewModel,price: String?) {
    val currentProduct: State<Product?> = viewModel.currentProductDetails.collectAsState()
    val currentProductSearching: State<Boolean> = viewModel.currentProductSearching.collectAsState()
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(modifier = Modifier.padding(2.dp),text = "Product Details", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Icon(imageVector = Icons.Default.ShoppingCart,contentDescription = null,modifier = Modifier.size(20.dp))
    }
    if(currentProductSearching.value)
        ProgressIndicator()
    else
    {
        if(currentProduct.value != null) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)) {
                val nameDescription: DescriptionItem = DescriptionItem("Name",currentProduct.value!!.name);
                val description: DescriptionItem = DescriptionItem("Description",currentProduct.value!!.description);
                val priceDescription: DescriptionItem = DescriptionItem("Price",price!!);
                Column(modifier = Modifier
                    .padding(2.dp)
                    .weight(0.70f)
                    .height(200.dp)) {
                    ProductImage(productID = currentProduct.value!!.id)
                }
                Column(modifier = Modifier
                    .padding(2.dp)
                    .weight(0.30f)) {
                    DescriptionItem(descriptionItem = nameDescription, headerFontSize = 15.sp, contentTextSize = 15.sp)
                    DescriptionItem(descriptionItem = description, headerFontSize = 15.sp, contentTextSize = 15.sp)
                    DescriptionItem(descriptionItem = priceDescription, headerFontSize = 15.sp, contentTextSize = 15.sp);
                }
            }
        }
    }
}
@Composable
private fun PaymentMethodsList(viewModel: CheckoutPageViewModel) {
    val paymentMethods: State<List<PaymentMethod>> = viewModel.currentPaymentMethods.collectAsState()
    val paymentMethodsSearching: State<Boolean> = viewModel.currentPaymentMethodsSearching.collectAsState()
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(modifier = Modifier.padding(2.dp),text = "Payment Methods", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Icon(imageVector = Icons.Default.CreditCard,contentDescription = null,modifier = Modifier.size(20.dp))
    }
    Text(modifier = Modifier.padding(2.dp),text = "Choose one of the available payment methods", fontSize = 15.sp, fontWeight = FontWeight.Normal)
    if(paymentMethodsSearching.value)
        ProgressIndicator()
    else
    {
        if(paymentMethods.value.isNotEmpty()) {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(paymentMethods.value) { index,item ->
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp), contentPadding = PaddingValues(1.dp), onClick = {viewModel.updateCurrentSelectedPaymentMethod(item)}) {
                        PaymentMethodCard(paymentMethod = item)
                    }
                }
            }
        }
    }
}

@Composable
private fun AddressesList(viewModel: CheckoutPageViewModel) {
    val addresses: State<List<Address>> = viewModel.currentAddresses.collectAsState()
    val addressesSearching: State<Boolean> = viewModel.currentAddressesSearching.collectAsState()
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(modifier = Modifier.padding(2.dp),text = "Addresses", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Icon(imageVector = Icons.Default.LocationOn,contentDescription = null,modifier = Modifier.size(20.dp))
    }
    Text(modifier = Modifier.padding(2.dp),text = "Choose one of the available addresses", fontSize = 15.sp, fontWeight = FontWeight.Normal)
    if(addressesSearching.value)
        ProgressIndicator()
    else
    {
        if(addresses.value.isNotEmpty()) {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(addresses.value) { index,item ->
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp), contentPadding = PaddingValues(1.dp), onClick = {viewModel.updateCurrentSelectedAddress(item)}) {
                        AddressCard(address = item)
                    }
                }
            }
        }
    }
}
@Composable
private fun OptionsSelected(viewModel: CheckoutPageViewModel) {
    val currentSelectedPaymentMethod: State<PaymentMethod?> = viewModel.currentSelectedPaymentMethod.collectAsState()
    val currentSelectedAddress: State<Address?> = viewModel.currentSelectedAddress.collectAsState()
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Options Selected", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if(currentSelectedPaymentMethod.value == null)
                Text(text = "You have to choose one payment method",fontSize = 15.sp, fontWeight = FontWeight.Normal)
            else
                PaymentMethodCard(paymentMethod = currentSelectedPaymentMethod.value!!)
            if(currentSelectedAddress.value == null)
                Text(text = "You have to choose one address", fontSize = 15.sp, fontWeight = FontWeight.Normal)
            else
                AddressCard(address = currentSelectedAddress.value!!)
        }
    }
}
@Composable
private fun SuccessHandler(navController: NavHostController) {
    AlertDialog(text = {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null, modifier = Modifier.size(80.dp))
            Text(text = "Order created successfully", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(2.dp))
            Text(text = "Your order has been created successfully",fontSize = 15.sp, fontWeight = FontWeight.Thin,modifier = Modifier.padding(2.dp))
            Column(modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                CustomButton(text = "Go to order page",clickCallback = {navController.navigate("orders")})
                CustomButton(text = "Go to home page",clickCallback = {navController.navigate("home")})
            }
        }
    }, onDismissRequest = { navController.navigate("home") }, confirmButton = {})
}
@Composable
private fun ButtonSection(viewModel: CheckoutPageViewModel) {
    val isValid: State<Boolean> = viewModel.isValid.collectAsState()
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Button(enabled = isValid.value,shape = RoundedCornerShape(5.dp),modifier = Modifier.padding(5.dp), onClick = {viewModel.createOrder()}) {
            Text(text = "Confirm",modifier = Modifier.padding(2.dp),fontSize = 15.sp, fontWeight = FontWeight.Normal)
        }
        Button(shape = RoundedCornerShape(5.dp),modifier = Modifier.padding(5.dp), onClick = {viewModel.reset()}) {
            Text(text = "Reset",modifier = Modifier.padding(2.dp), fontSize = 15.sp, fontWeight = FontWeight.Normal)
        }
    }
}