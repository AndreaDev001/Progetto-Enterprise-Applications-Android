package com.enterpriseapplications.views.pages

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.viewmodel.ProductDetailsViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.DescriptionItem
import com.enterpriseapplications.views.UserCard
import com.enterpriseapplications.views.pages.search.ProductList
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductPageDetails(navController: NavHostController) {
    val viewModel: ProductDetailsViewModel = viewModel(factory = viewModelFactory)
    val productID: UUID = UUID.fromString("196967df-d0ec-44db-9042-39abffdf3fa2");
    viewModel.productID = productID
    viewModel.initialize()
    val refreshState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 2.dp, horizontal = 5.dp)
    ) {
        TopAppBar(title = {
            Text(text = "Product Details", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }, modifier = Modifier.fillMaxWidth())
        val currentSellerProducts: State<List<Product>> = viewModel.sellerProducts.collectAsState()
        val currentSellerProductsSearching: State<Boolean> = viewModel.sellerProductsSearching.collectAsState()
        SwipeRefresh(state = refreshState, onRefresh = {}) {
            Column(modifier = Modifier
                .padding(vertical = 5.dp)
                .verticalScroll(ScrollState(0))) {
                ProductDetails(viewModel = viewModel)
                ProductDescription(viewModel = viewModel)
                ProductDetailsButton(viewModel = viewModel)
                Column(modifier = Modifier.padding(horizontal = 5.dp)) {
                    ProductRow(searching = currentSellerProductsSearching.value, items = currentSellerProducts.value, icon = Icons.Filled.Person, headerText = "Seller's products", supportingText = "Here you can see some other products from the same seller")
                    ProductRow(searching = currentSellerProductsSearching.value, items = currentSellerProducts.value, icon = Icons.Filled.ShoppingCart, headerText = "Similar Products", supportingText = "Here you can see some similar products")
                }
            }
        }
    }
}
@Composable
private fun ProductRow(items: List<Product>,searching: Boolean = false,icon: ImageVector,headerText: String,supportingText: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 2.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = headerText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Icon(modifier = Modifier.padding(horizontal = 2.dp),imageVector = icon, contentDescription = null)
        }
        Text(text = supportingText, fontSize = 12.sp, fontWeight = FontWeight.Thin,modifier = Modifier.padding(vertical = 2.dp))
        ProductList(currentItems = items, searching = searching, vertical = false)
    }
}
@Composable
private fun ProductDetails(viewModel: ProductDetailsViewModel) {
    val productDetails: State<Product?> = viewModel.currentProductDetails.collectAsState()
    if(productDetails.value != null) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = productDetails.value!!.name,fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(model = "https://as1.ftcdn.net/v2/jpg/03/46/83/96/1000_F_346839683_6nAPzbhpSkIpb8pmAwufkC7c5eD7wYws.jpg", contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(60))
                            .size(50.dp)
                    )
                    Text(text = productDetails.value!!.seller.username, fontSize = 15.sp, fontWeight = FontWeight.Thin);
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)) {
                AsyncImage(contentScale = ContentScale.Crop,model = "https://t3.ftcdn.net/jpg/02/10/85/26/360_F_210852662_KWN4O1tjxIQt8axc2r82afdSwRSLVy7g.jpg", contentDescription = null, modifier = Modifier.fillMaxWidth())
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .horizontalScroll(ScrollState(0)), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Button(contentPadding = PaddingValues(0.dp),shape = RoundedCornerShape(5.dp),onClick = {},modifier = Modifier.size(80.dp)) {
                    AsyncImage(contentScale = ContentScale.Crop, model = "https://t3.ftcdn.net/jpg/02/10/85/26/360_F_210852662_KWN4O1tjxIQt8axc2r82afdSwRSLVy7g.jpg", contentDescription = null,modifier = Modifier.fillMaxSize())
                }
                Button(modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(80.dp),contentPadding = PaddingValues(0.dp),shape = RoundedCornerShape(5.dp),onClick = {}) {
                    AsyncImage(contentScale = ContentScale.Crop,model = "https://t3.ftcdn.net/jpg/02/10/85/26/360_F_210852662_KWN4O1tjxIQt8axc2r82afdSwRSLVy7g.jpg", contentDescription = null,modifier = Modifier.fillMaxSize())
                }
                Button(modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(80.dp),contentPadding = PaddingValues(0.dp),shape = RoundedCornerShape(5.dp),onClick = {}) {
                    AsyncImage(contentScale = ContentScale.Crop,model = "https://t3.ftcdn.net/jpg/02/10/85/26/360_F_210852662_KWN4O1tjxIQt8axc2r82afdSwRSLVy7g.jpg", contentDescription = null,modifier = Modifier.fillMaxSize())
                }
                Button(modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(80.dp),contentPadding = PaddingValues(0.dp),shape = RoundedCornerShape(5.dp),onClick = {}) {
                    AsyncImage(contentScale = ContentScale.Crop,model = "https://t3.ftcdn.net/jpg/02/10/85/26/360_F_210852662_KWN4O1tjxIQt8axc2r82afdSwRSLVy7g.jpg", contentDescription = null,modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
@Composable
private fun ProductDescription(viewModel: ProductDetailsViewModel) {
    val productDetails: State<Product?> = viewModel.currentProductDetails.collectAsState()
    if(productDetails.value != null) {
        val priceDescription: DescriptionItem = DescriptionItem("Price",productDetails.value!!.price.toString())
        val minPriceDescription: DescriptionItem = DescriptionItem("Minimum Offer Price",productDetails.value!!.minPrice.toString())
        val brand: DescriptionItem = DescriptionItem("Brand",productDetails.value!!.brand.toString())
        val condition: DescriptionItem = DescriptionItem("Condition",productDetails.value!!.condition.toString())
        Column(modifier = Modifier.padding(2.dp)) {
            Text(text = productDetails.value!!.description, fontSize = 18.sp, fontWeight = FontWeight.Normal,modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth())
            Row(modifier = Modifier.fillMaxWidth()) {
                DescriptionItem(modifier = Modifier.weight(1f),priceDescription,headerFontSize = 15.sp, contentTextSize = 15.sp)
                DescriptionItem(modifier = Modifier.weight(1f),minPriceDescription,headerFontSize = 15.sp, contentTextSize = 15.sp)
                DescriptionItem(modifier = Modifier.weight(1f),descriptionItem = brand,headerFontSize = 15.sp, contentTextSize = 15.sp)
                DescriptionItem(modifier = Modifier.weight(1f),descriptionItem = condition, headerFontSize = 15.sp, contentTextSize = 15.sp)
            }
        }
    }
}
@Composable
private fun ProductDetailsButton(viewModel: ProductDetailsViewModel) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp)
        .horizontalScroll(ScrollState(0))) {
        Button(onClick = {}) {
            Text(text = "Buy", fontSize = 15.sp, fontWeight = FontWeight.Normal)
        }
        Button(onClick = {},modifier = Modifier.padding(horizontal = 2.dp)) {
            Text(text = "Report", fontSize = 15.sp, fontWeight = FontWeight.Normal)
        }
        Button(onClick = {},modifier = Modifier.padding(horizontal = 2.dp)) {
            Text(text = "Add Like", fontSize = 15.sp, fontWeight = FontWeight.Normal)
        }
        Button(onClick = {},modifier = Modifier.padding(horizontal = 2.dp)) {
            Text(text = "Make an offer", fontSize = 15.sp, fontWeight = FontWeight.Normal)
        }
        Button(onClick = {},modifier = Modifier.padding(horizontal = 2.dp)) {
            Text(text = "Start conversation", fontSize = 15.sp, fontWeight = FontWeight.Normal)
        }
    }
}