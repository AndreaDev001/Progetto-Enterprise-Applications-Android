package com.enterpriseapplications.viewmodel.profile

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.model.Conversation
import com.enterpriseapplications.model.Message
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class MessagePageViewModel(val application: CustomApplication): BaseViewModel(application) {

    var conversationID: UUID? = null;
    var userID: UUID? = null;
    private var _currentText: FormControl<String?> = FormControl("");
    private var _currentConversation: MutableStateFlow<Conversation?> = MutableStateFlow(null);
    private var _currentMessages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    private var _currentMessagesPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));

    fun initialize() {
        if(this.conversationID != null && userID != null) {
            this.makeRequest(this.retrofitConfig.conversationController.getConversationById(conversationID!!
            ),{
                this._currentConversation.value = it
                this.updateCurrentMessages(false)
            })
        }
    }

    private fun updateCurrentMessages(page: Boolean) {
        this.makeRequest(this.retrofitConfig.messageController.getConversation(this.conversationID!!,this._currentMessagesPage.value.number,20),{
            if(it._embedded != null) {
                if(!page)
                    this._currentMessages.value = it._embedded.content;
                else
                {
                    val mutableList: MutableList<Message> = mutableListOf();
                    mutableList.addAll(this._currentMessages.value);
                    mutableList.addAll(mutableList);
                    this._currentMessages.value = mutableList;
                }
            }
            this._currentMessagesPage.value = this._currentMessagesPage.value.copy(size = it.page.size, totalElements = it.page.totalElements, totalPages = it.page.totalPages,number = it.page.number)
        })
    }

    fun updateCurrentPage() {
        if(this._currentMessagesPage.value.number + 1 >= this._currentMessagesPage.value.totalPages)
            return;
        this.updateCurrentMessages(true);
    }

    fun handleMessage() {

    }

    val currentConversation: StateFlow<Conversation?> = _currentConversation.asStateFlow();
    val currentMessages: StateFlow<List<Message>> = _currentMessages.asStateFlow();
    val currentMessagesPage: StateFlow<Page> = _currentMessagesPage.asStateFlow()
    val currentText: FormControl<String?> = _currentText;
}