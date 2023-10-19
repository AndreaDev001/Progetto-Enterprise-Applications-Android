package com.enterpriseapplications.views.pages.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import com.enterpriseapplications.isScrolledToEnd
import com.enterpriseapplications.model.Conversation
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.viewmodel.profile.ConversationPageViewModel
import com.enterpriseapplications.viewmodel.viewModelFactory
import com.enterpriseapplications.views.ConversationCard
import com.enterpriseapplications.views.OfferCard
import com.enterpriseapplications.views.pages.search.MissingItems
import com.enterpriseapplications.views.pages.search.PageShower
import com.enterpriseapplications.views.pages.search.ProgressIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationPage(navController: NavHostController) {
    val viewModel: ConversationPageViewModel = viewModel(factory = viewModelFactory)
    val refreshState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    val userID: UUID = UUID.fromString("196967df-d0ec-44db-9042-39abffdf3fa2")
    viewModel.userID = userID
    viewModel.initialize()
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp)) {
        TopAppBar(title = {
            Text(text = "Conversations", fontSize = 20.sp)
        }, navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },modifier = Modifier.fillMaxWidth())
        SwipeRefresh(state = refreshState, onRefresh = {viewModel.initialize()}) {
            Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                Text(text = "Here you can see all of your conversations, click on them to continue to chat", fontSize = 17.sp, fontWeight = FontWeight.Normal,modifier = Modifier.padding(vertical = 2.dp))
                ConversationList(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ConversationList(viewModel: ConversationPageViewModel) {
    val conversations: State<List<Conversation>> = viewModel.currentConversations.collectAsState()
    val conversationsSearching: State<Boolean> = viewModel.currentConversationsSearching.collectAsState()
    val lazyState: LazyListState = rememberLazyListState()
    Column(modifier = Modifier.padding(2.dp)) {
        if (conversationsSearching.value)
            ProgressIndicator()
        else {
            if (conversations.value.isNotEmpty()) {
                LazyColumn(
                    state = lazyState,
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalArrangement = Arrangement.Top,
                    content = {
                        itemsIndexed(items = conversations.value) { index, item ->
                            Box(modifier = Modifier.padding(2.dp)) {
                                var receiver: Boolean = item.second.id.equals(viewModel.userID)
                                ConversationCard(item,receiver)
                            }
                        }
                    })
            } else
                MissingItems(
                    buttonText = "Reload",
                    missingText = "No conversations have been found, set is empty",
                    callback = { viewModel.initialize()})
        }
    }
}