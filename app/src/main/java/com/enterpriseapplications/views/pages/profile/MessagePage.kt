package com.enterpriseapplications.views.pages.profile

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.enterpriseapplications.viewmodel.profile.MessagePageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.MessageCard
import com.enterpriseapplications.views.UserImage
import com.enterpriseapplications.views.pages.search.CustomTextField
import com.enterpriseapplications.views.pages.search.MissingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
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
                val requiredUser = if(currentConversation.value!!.starter.id == authenticatedUser.value!!.userID.toString()) currentConversation.value!!.starter else currentConversation.value!!.product.seller;
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                        UserImage(userID = requiredUser.id,contentScale = ContentScale.Crop)
                        Column(modifier = Modifier.padding(horizontal = 2.dp)) {
                            Text(text = requiredUser.username,modifier = Modifier.padding(horizontal = 2.dp),fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(text = currentConversation.value!!.createdDate,modifier = Modifier.padding(horizontal = 2.dp),fontSize = 15.sp, fontWeight = FontWeight.Thin)
                        }
                    }
                    Row(modifier = Modifier.padding(horizontal = 2.dp)) {
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Filled.Report, contentDescription = null)
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
                    MessageList(viewModel = viewModel)
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                    CustomTextField(formControl = viewModel.currentText,modifier = Modifier.weight(0.75f), supportingText = "Write a message", placeHolder = "Text...")
                    IconButton(onClick = {viewModel.createMessage()},modifier = Modifier.padding(horizontal = 1.dp)) {
                        Icon(imageVector = Icons.Filled.Send, contentDescription = null)
                    }
                }
            }
        }
    }
}
@Composable
private fun MessageList(viewModel: MessagePageViewModel) {
    val currentMessages: State<List<Message>> = viewModel.currentMessages.collectAsState()
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
    Column(modifier = Modifier.fillMaxWidth().padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        if(currentMessagesPage.value.totalElements > 0) {
            LazyColumn(state = lazyListState) {
                itemsIndexed(currentMessages.value) {index,item ->
                    MessageCard(message = item)
                }
            }
        }
        else
            MissingItems(callback = {viewModel.initialize()}, missingText = "No messages found, set is empty", buttonText = "Reload")
    }
}