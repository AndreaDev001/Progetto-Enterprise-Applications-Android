package com.enterpriseapplications.views.pages

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
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
import com.enterpriseapplications.config.RetrofitConfig
import com.enterpriseapplications.config.authentication.AuthenticatedUser
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.model.Conversation
import com.enterpriseapplications.model.Like
import com.enterpriseapplications.model.Offer
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.model.refs.ProductRef
import com.enterpriseapplications.model.reports.ProductReport
import com.enterpriseapplications.model.reports.Report
import com.enterpriseapplications.viewmodel.ProductDetailsViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.DescriptionItem
import com.enterpriseapplications.views.ProductImage
import com.enterpriseapplications.views.UserCard
import com.enterpriseapplications.views.UserImage
import com.enterpriseapplications.views.alerts.create.CreateOffer
import com.enterpriseapplications.views.alerts.create.CreateReport
import com.enterpriseapplications.views.pages.search.CustomButton
import com.enterpriseapplications.views.pages.search.ProductList
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import com.enterpriseapplications.views.pages.search.SearchingDialog
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductPageDetails(navController: NavHostController,productID: String?) {
    val viewModel: ProductDetailsViewModel = viewModel(factory = viewModelFactory)
    val refreshState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    viewModel.productID = UUID.fromString(productID);
    viewModel.initialize()
    val currentSellerProducts: State<List<Product>> = viewModel.sellerProducts.collectAsState()
    val currentSimilarProducts: State<List<Product>> = viewModel.similarProducts.collectAsState()
    val productDetails: State<Product?> = viewModel.currentProductDetails.collectAsState()
    val conversationCreated: State<Conversation?> = viewModel.createdConversation.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 2.dp, horizontal = 5.dp)
    ) {
        TopAppBar(title = {
            val text: String = if(productDetails.value != null) productDetails.value!!.name else "Product Details";
            Text(text = "Product Details", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }, modifier = Modifier.fillMaxWidth())
        SwipeRefresh(state = refreshState, onRefresh = {viewModel.initialize()}) {
            Column(modifier = Modifier
                .padding(vertical = 5.dp)
                .verticalScroll(ScrollState(0))) {
                if(conversationCreated.value != null)
                    ConversationCreated(navController = navController,conversationCreated.value!!)
                if(productDetails.value != null) {
                    ProductDetails(productDetails = productDetails.value!!)
                    ProductDescription(productDetails = productDetails.value!!)
                    ProductDetailsButton(navController = navController,productDetails = productDetails.value!!)
                }
                Column(modifier = Modifier.padding(horizontal = 5.dp)) {
                    ProductRow(navController = navController, items = currentSellerProducts.value, icon = Icons.Filled.Person, headerText = "Seller's products", supportingText = "Here you can see some other products from the same seller")
                    ProductRow(navController = navController, items = currentSimilarProducts.value, icon = Icons.Filled.ShoppingCart, headerText = "Similar Products", supportingText = "Here you can see some similar products")
                }
            }
        }
    }
}
@Composable
private fun ProductRow(navController: NavHostController,items: List<Product>,searching: Boolean = false,icon: ImageVector,headerText: String,supportingText: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 2.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = headerText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Icon(modifier = Modifier.padding(horizontal = 2.dp),imageVector = icon, contentDescription = null)
        }
        Text(text = supportingText, fontSize = 12.sp, fontWeight = FontWeight.Thin,modifier = Modifier.padding(vertical = 2.dp))
        ProductList(navController = navController, currentItems = items, searching = searching, vertical = false)
    }
}
@Composable
private fun ProductDetails(productDetails: Product) {
    val viewModel: ProductDetailsViewModel = viewModel(factory = viewModelFactory)
    val currentImagesAmount: State<Int> = viewModel.currentProductImagesAmount.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = productDetails.name, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                UserImage(userID = productDetails.seller.id, size = 50.dp, contentScale = ContentScale.Crop)
                Text(
                    modifier = Modifier.padding(horizontal = 2.dp),
                    text = productDetails.seller.username,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Thin
                );
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            val currentIndex: State<Int> = viewModel.currentSelectedIndex.collectAsState()
            val source: String = if(currentImagesAmount.value > 0) "http://${RetrofitConfig.resourceServerIpAddress}/api/v1/productImages/public/${productDetails.id}/${currentIndex.value}" else "https://img.freepik.com/premium-vector/default-image-icon-vector-missing-picture-page-website-design-mobile-app-no-photo-available_87543-11093.jpg";
            AsyncImage(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), contentScale = ContentScale.Crop, model = source, contentDescription = null)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .horizontalScroll(ScrollState(0)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until currentImagesAmount.value) {
                Button(contentPadding = PaddingValues(0.dp), onClick = { viewModel.updateCurrentIndex(i) }, modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(60.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                        model = "http://${RetrofitConfig.resourceServerIpAddress}/api/v1/productImages/public/${productDetails.id}/${i}",
                        contentDescription = null
                    )
                }
            }
        }
    }
}
@Composable
private fun ProductDescription(productDetails: Product) {
    val priceDescription: DescriptionItem =
        DescriptionItem("Price", productDetails.price.toString())
    val minPriceDescription: DescriptionItem =
        DescriptionItem("Minimum Offer Price", productDetails.minPrice.toString())
    val brand: DescriptionItem = DescriptionItem("Brand", productDetails.brand.toString())
    val condition: DescriptionItem =
        DescriptionItem("Condition", productDetails.condition.toString())
    Column(modifier = Modifier.padding(2.dp)) {
        Text(
            text = productDetails.description,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            DescriptionItem(
                modifier = Modifier.weight(1f),
                priceDescription,
                headerFontSize = 15.sp,
                contentTextSize = 15.sp
            )
            DescriptionItem(
                modifier = Modifier.weight(1f),
                minPriceDescription,
                headerFontSize = 15.sp,
                contentTextSize = 15.sp
            )
            DescriptionItem(
                modifier = Modifier.weight(1f),
                descriptionItem = brand,
                headerFontSize = 15.sp,
                contentTextSize = 15.sp
            )
            DescriptionItem(
                modifier = Modifier.weight(1f),
                descriptionItem = condition,
                headerFontSize = 15.sp,
                contentTextSize = 15.sp
            )
        }
    }
}

@Composable
private fun ConversationCreated(navController: NavHostController,conversation: Conversation) {
    val viewModel: ProductDetailsViewModel = viewModel(factory = viewModelFactory)
    val creatingConversation: State<Boolean> = viewModel.creatingConversation.collectAsState()
    val callback: () -> Unit = {viewModel.closeConversationAlert()}
    if(creatingConversation.value) {
        AlertDialog(icon = {
            Icon(imageVector = Icons.Default.Message, contentDescription = null, modifier = Modifier
                .padding(2.dp)
                .size(60.dp))
        }, onDismissRequest = {}, confirmButton = {}, dismissButton = {}, text = {
            Column(modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(modifier = Modifier.padding(2.dp), text = "Conversation created", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(modifier = Modifier.padding(2.dp), text = "The required conversation has been successfully created", fontSize = 15.sp, fontWeight = FontWeight.Thin)
                CustomButton(text = "Go to conversation", clickCallback = {navController.navigate("messagePage/${conversation.id}")})
                CustomButton(text = "Cancel", clickCallback = {callback()})
            }
        })
    }
}
@Composable
private fun ProductDetailsButton(navController: NavHostController,productDetails: Product) {
    val viewModel: ProductDetailsViewModel = viewModel(factory = viewModelFactory)
    val searchingLike: State<Boolean> = viewModel.searchingLike.collectAsState()
    val searchingReport: State<Boolean> = viewModel.searchingReport.collectAsState()
    val searchingOffer: State<Boolean> = viewModel.searchingOffer.collectAsState()
    val searchingConversation: State<Boolean> = viewModel.searchingConversation.collectAsState()

    val hasLike: State<Like?> = viewModel.hasLike.collectAsState()
    val hasReport: State<ProductReport?> = viewModel.hasReport.collectAsState()
    val hasOffer: State <Offer?> = viewModel.hasOffer.collectAsState()
    val hasConversation: State<Conversation?> = viewModel.hasConversation.collectAsState()

    val authenticatedUser: State<AuthenticatedUser?> = AuthenticationManager.currentUser.collectAsState()
    val currentLikeText: String = if(hasLike.value != null) "Remove Like" else "Add Like"
    val currentOfferText: String = if(hasOffer.value != null) "Update offer" else "Make an offer";
    val currentConversationText: String = if(hasConversation.value != null) "Go to conversation" else "Create a conversation";

    val offerVisible = remember { mutableStateOf(false) }
    val reportVisible = remember { mutableStateOf(false) }

    if(offerVisible.value)
        CreateOffer(productID = viewModel.productID!!, update = hasOffer.value != null, offerID = if(hasOffer.value != null) hasOffer.value!!.id else null, confirmCallback = {offerVisible.value = false;viewModel.getOffer()}, cancelCallback = {offerVisible.value = false})
    if(reportVisible.value)
        CreateReport(productID = viewModel.productID!!, update = false, confirmProductReport = {reportVisible.value = false;viewModel.getReport()}, cancelCallback = {reportVisible.value = false})
    if(authenticatedUser.value!!.userID != productDetails.seller.id)
    {
        val scrollState = rememberScrollState()
        if(productDetails.status == "BOUGHT") {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "This product has already been sold", fontSize = 20.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
            }
        }
        else
        {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .horizontalScroll(scrollState)) {
                CustomButton(text = "Buy", clickCallback = {navController.navigate("checkoutPage/${productDetails.id}/${productDetails.price}")})
                if(searchingReport.value)
                    ProgressIndicator()
                else if(hasReport.value == null)
                    CustomButton(text = "Report", clickCallback = {reportVisible.value = true})
                if(searchingLike.value)
                    ProgressIndicator()
                else
                {
                    CustomButton(text = currentLikeText, clickCallback = {
                        if(hasLike.value != null)
                            viewModel.removeLike()
                        else
                            viewModel.addLike()
                    })
                }
                if(searchingOffer.value)
                    ProgressIndicator()
                else
                    CustomButton(text = currentOfferText, clickCallback = {offerVisible.value = true})
                if(searchingConversation.value)
                    ProgressIndicator()
                else
                    CustomButton(text = currentConversationText, clickCallback = {
                        if(hasConversation.value != null)
                            navController.navigate("messagePage/${hasConversation.value!!.id}")
                        else
                            viewModel.createConversation()
                    })
            }
        }
    }
}