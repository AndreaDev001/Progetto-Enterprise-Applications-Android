package com.enterpriseapplications.viewmodel.profile

import android.provider.Telephony.Sms.Conversations
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.model.Conversation
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ConversationPageViewModel(val application: CustomApplication) : BaseViewModel(application)
{
    var userID: UUID? = AuthenticationManager.currentUser.value!!.userID;
    private var _currentConversations: MutableStateFlow<List<Conversation>> = MutableStateFlow(emptyList())
    private var _currentConversationsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init
    {
        this.updateCurrentConversations();
    }
    fun initialize() {
        this.updateCurrentConversations();
    }

    private fun updateCurrentConversations() {
        this._currentConversationsSearching.value = true
        this.makeRequest(this.retrofitConfig.conversationController.getConversations(userID!!),{
            this._currentConversations.value = it
            this._currentConversationsSearching.value = false
        })
    }

    val currentConversations: StateFlow<List<Conversation>> = _currentConversations.asStateFlow()
    val currentConversationsSearching: StateFlow<Boolean> = _currentConversationsSearching.asStateFlow()
}