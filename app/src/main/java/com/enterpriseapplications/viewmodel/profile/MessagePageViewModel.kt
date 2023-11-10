package com.enterpriseapplications.viewmodel.profile

import androidx.compose.runtime.State
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Conversation
import com.enterpriseapplications.model.Message
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.create.CreateMessage
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class MessagePageViewModel(val application: CustomApplication): BaseViewModel(application) {

    var conversationID: UUID? = null;
    var userID: UUID? = null;
    private var _searchingMessages: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _currentText: FormControl<String?> = FormControl("", Validators.required())
    private var _canReport: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private var _searchingReport: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _currentConversation: MutableStateFlow<Conversation?> = MutableStateFlow(null);
    private var _currentProduct: MutableStateFlow<Product?> = MutableStateFlow(null);
    private var _currentMessages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    private var _currentMessagesPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _formGroup: FormGroup = FormGroup(_currentText)

    fun initialize() {
        if(this.conversationID != null && userID != null) {
            this.makeRequest(this.retrofitConfig.conversationController.getConversationById(conversationID!!
            ),{
                this._currentConversation.value = it
                this.updateCurrentMessages(false)
                this.updateProductDetails(it.product.id)
            })
        }
    }

    private fun updateProductDetails(productID: UUID?) {
        this.makeRequest(this.retrofitConfig.productController.getDetails(productID!!),{
            this._currentProduct.value = it
        },{this._currentProduct.value = null})
    }
    private fun updateCurrentMessages(page: Boolean) {
        this._searchingMessages.value = true
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
            this._searchingMessages.value = false
            this._currentMessagesPage.value = this._currentMessagesPage.value.copy(size = it.page.size, totalElements = it.page.totalElements, totalPages = it.page.totalPages,number = it.page.number)
        },{
            this._searchingMessages.value = false
        })
    }

    fun updateCurrentPage() {
        if(this._currentMessagesPage.value.number + 1 >= this._currentMessagesPage.value.totalPages)
            return;
        this._currentMessagesPage.value = this._currentMessagesPage.value.copy(size = this._currentMessagesPage.value.size,totalElements = this._currentMessagesPage.value.totalElements, totalPages = this._currentMessagesPage.value.totalPages,number = this._currentMessagesPage.value.number + 1)
        this.updateCurrentMessages(true);
    }

    fun createMessage() {
        if(this._formGroup.validate()) {
            if(this._currentConversation.value != null) {
                val receiverID: UUID = if(this._currentConversation.value!!.starter.id == AuthenticationManager.currentUser.value!!.userID) this._currentConversation.value!!.product.seller.id else this._currentConversation.value!!.starter.id
                val createMessage: CreateMessage = CreateMessage(this._currentConversation.value!!.id,receiverID,this._currentText.currentValue.value!!)
                this.makeRequest(this.retrofitConfig.messageController.createMessage(createMessage),{
                    val mutableList: MutableList<Message> = mutableListOf()
                    mutableList.addAll(this._currentMessages.value)
                    mutableList.add(it)
                    this._currentMessages.value = mutableList
                    this._formGroup.reset()
                })
            }
        }
    }

    fun getReport(messageID: UUID) {
        this._searchingReport.value = true
        this.makeRequest(this.retrofitConfig.messageReportController.getMessageReport(AuthenticationManager.currentUser.value!!.userID,messageID),{
            this._searchingReport.value = false
            this._canReport.value = false
        },{
            this._searchingReport.value = false
            this._canReport.value = true
        })
    }

    val searchingMessages: StateFlow<Boolean> = _searchingMessages.asStateFlow()
    val currentProduct: StateFlow<Product?> = _currentProduct.asStateFlow()
    val currentConversation: StateFlow<Conversation?> = _currentConversation.asStateFlow();
    val currentMessages: StateFlow<List<Message>> = _currentMessages.asStateFlow();
    val currentMessagesPage: StateFlow<Page> = _currentMessagesPage.asStateFlow()
    val currentText: FormControl<String?> = _currentText;
    val searchingReport: StateFlow<Boolean> = _searchingReport.asStateFlow()
    val canReport: StateFlow<Boolean> = _canReport.asStateFlow()
    val formGroup: FormGroup = _formGroup
}