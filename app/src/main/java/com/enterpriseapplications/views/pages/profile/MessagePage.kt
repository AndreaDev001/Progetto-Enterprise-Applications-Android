package com.enterpriseapplications.views.pages.profile

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.enterpriseapplications.config.authentication.AuthenticatedUser
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Conversation
import com.enterpriseapplications.model.Message
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.create.CreateMessage
import com.enterpriseapplications.viewmodel.profile.MessagePageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.MessageCard
import com.enterpriseapplications.views.ProductCard
import com.enterpriseapplications.views.UserImage
import com.enterpriseapplications.views.alerts.create.CreateReport
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.MissingItems
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagePage(navController: NavHostController,conversationID: String) {
    val conversation: UUID? = UUID.fromString(conversationID);
    val authenticatedUser: State<AuthenticatedUser?> = AuthenticationManager.currentUser.collectAsState()
    val refreshState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    val viewModel: MessagePageViewModel = viewModel(factory = viewModelFactory);
    viewModel.conversationID = conversation
    viewModel.userID = authenticatedUser.value!!.userID
    viewModel.initialize()
    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
            val currentConversation: State<Conversation?> = viewModel.currentConversation.collectAsState()
            if(currentConversation.value != null) {
                val requiredUser =
                    if (currentConversation.value!!.starter.id == authenticatedUser.value!!.userID) currentConversation.value!!.product.seller else currentConversation.value!!.starter;
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UserImage(userID = requiredUser.id, contentScale = ContentScale.Crop)
                        Column(modifier = Modifier.padding(horizontal = 2.dp)) {
                            Text(
                                text = requiredUser.username,
                                modifier = Modifier.padding(horizontal = 2.dp),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentConversation.value!!.createdDate.toString(),
                                modifier = Modifier.padding(horizontal = 2.dp),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Thin
                            )
                        }
                    }
                    val expanded = remember { mutableStateOf(false) }
                    val currentProduct: State<Product?> = viewModel.currentProduct.collectAsState()
                    Row(modifier = Modifier.padding(horizontal = 2.dp)) {
                        IconButton(onClick = {expanded.value = true}) {
                            Icon(imageVector = Icons.Default.ShoppingCart,contentDescription = null, modifier = Modifier.padding(2.dp))
                        }
                        DropdownMenu(expanded = expanded.value, onDismissRequest = {expanded.value = false} ) {
                            DropdownMenuItem(text = {
                                Column(modifier = Modifier.padding(5.dp)) {
                                    Text(text = "This conversation was created about this product",fontSize = 15.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
                                    ProductCard(navHostController = navController, product = currentProduct.value!!)
                                }
                            }, onClick = {})
                        }
                    }
                }
            }
        }, navigationIcon = {
            if (navController.previousBackStackEntry != null)
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
        })
        SwipeRefresh(state = refreshState, onRefresh = { viewModel.initialize() }) {
            Column(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .weight(1f)) {
                    MessageList(navController,viewModel = viewModel)
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                    val enabled: State<Boolean> = viewModel.formGroup.valid.collectAsState()
                    CustomTextField(formControl = viewModel.currentText,modifier = Modifier.weight(0.75f), supportingText = "Write a message", placeHolder = "Text...")
                    IconButton(enabled = enabled.value,onClick = {viewModel.createMessage()},modifier = Modifier.padding(horizontal = 1.dp)) {
                        Icon(imageVector = Icons.Filled.Send, contentDescription = null)
                    }
                }
            }
        }
    }
}
@Composable
private fun MessageList(navController: NavHostController,viewModel: MessagePageViewModel) {
    val searchingMessages: State<Boolean> = viewModel.searchingMessages.collectAsState()
    val currentMessages: State<List<Message>> = viewModel.currentMessages.collectAsState()
    val authenticatedUser: State<AuthenticatedUser?> = AuthenticationManager.currentUser.collectAsState()
    val currentMessagesPage: State<Page> = viewModel.currentMessagesPage.collectAsState()
    val lazyListState: LazyListState = rememberLazyListState()
    val endReached by remember {
        derivedStateOf {
            lazyListState.isScrolledToEnd()
        }
    }
    LaunchedEffect(endReached) {
        viewModel.updateCurrentPage()
    }
    if(searchingMessages.value)
        ProgressIndicator()
    else
    {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if(currentMessages.value.isNotEmpty()) {
                LazyColumn(state = lazyListState) {
                    itemsIndexed(currentMessages.value) {index,item ->
                        MessageCard(message = item,received = item.receiver.id == authenticatedUser.value!!.userID, options = {
                            MessageOptions(
                                navController = navController,
                                message = item,
                                viewModel = viewModel
                            )
                        })
                    }
                }
            }
            else
                MissingItems(callback = {viewModel.initialize()}, missingText = "No messages found, set is empty", buttonText = "Reload")
        }
    }
}
@Composable
private fun MessageOptions(navController: NavHostController,message: Message,viewModel: MessagePageViewModel) {
    viewModel.getReport(message.id)
    val searchingReport: State<Boolean> = viewModel.searchingReport.collectAsState()
    val creatingReport: MutableState<Boolean> = remember { mutableStateOf(false) }
    val canReport: State<Boolean> = viewModel.canReport.collectAsState()
    val callback: () -> Unit = {creatingReport.value = false}
    if(creatingReport.value)
        CreateReport(messageID = message.id, update = false, confirmMessageReport = {callback()}, dismissCallback = {callback()}, cancelCallback = {callback()})
    if(searchingReport.value)
        ProgressIndicator()
    else
    {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)) {
            if(canReport.value) {
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp), shape = RectangleShape, contentPadding = PaddingValues(0.dp), onClick = {creatingReport.value = true}) {
                    Text(text = "Report",modifier = Modifier.padding(2.dp), fontSize = 15.sp, fontWeight = FontWeight.Normal)
                }
            }
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),shape = RectangleShape, contentPadding = PaddingValues(0.dp), onClick = {
                navController.navigate("profilePage/${message.sender.id}")
            }) {
                Text(text = "Visit profile",modifier = Modifier.padding(2.dp), fontSize = 15.sp, fontWeight = FontWeight.Normal)
            }
        }
    }
}