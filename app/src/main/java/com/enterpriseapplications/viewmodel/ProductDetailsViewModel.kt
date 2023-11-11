package com.enterpriseapplications.viewmodel

import androidx.compose.runtime.State
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.model.Conversation
import com.enterpriseapplications.model.Like
import com.enterpriseapplications.model.Offer
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.create.CreateConversation
import com.enterpriseapplications.model.reports.ProductReport
import com.enterpriseapplications.model.reports.Report
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ProductDetailsViewModel(val application: CustomApplication): BaseViewModel(application) {
    var productID: UUID? = null;

    private var _currentProductDetails: MutableStateFlow<Product?> = MutableStateFlow(null);
    private var _currentSelectedIndex: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentProductImagesAmount: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _similarProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private var _sellerProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private var _similarProductsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _sellerProductsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _conversationCreated: MutableStateFlow<Conversation?> = MutableStateFlow(null)
    private var _creatingConversations: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var _hasLike: MutableStateFlow<Like?> = MutableStateFlow(null)
    private var _hasOffer: MutableStateFlow<Offer?> = MutableStateFlow(null)
    private var _hasReport: MutableStateFlow<ProductReport?> = MutableStateFlow(null)
    private var _hasConversation: MutableStateFlow<Conversation?> = MutableStateFlow(null);
    private var _searchingLike: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _searchingOffer: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _searchingReport: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _searchingConversation: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun initialize()
    {
        if(this.productID != null)
        {
            this.makeRequest(this.retrofitConfig.productController.getDetails(this.productID!!),{
                this._currentProductDetails.value = it
                this.updateCurrentSimilarProducts(false)
                this.updateCurrentSellerProducts(false)
                this.makeRequest(this.retrofitConfig.likeController.getLike(AuthenticationManager.currentUser.value!!.userID,productID!!),{
                    this._hasLike.value = it
                }, errorCallback = {this._hasLike.value = null;})
            })
            this.updateProductImages()
            this.getLike()
            this.getReport()
            this.getOffer()
            this.getConversation()
        }
    }

    fun getLike() {
        this._searchingLike.value = true;
        this.makeRequest(this.retrofitConfig.likeController.getLike(AuthenticationManager.currentUser.value!!.userID,productID!!),{
           this._searchingLike.value = false;
           this._hasLike.value = it
        },{
            this._searchingLike.value = false;
            this._hasLike.value = null
        })
    }

    fun getOffer() {
        this._searchingOffer.value = true;
        this.makeRequest(this.retrofitConfig.offerController.getOffer(AuthenticationManager.currentUser.value!!.userID,productID!!),{
            this._searchingOffer.value = false
            this._hasOffer.value = it
        },{
            this._searchingOffer.value = false
            this._hasOffer.value = null
        })
    }

    fun getReport() {
        this._searchingReport.value = true;
        this.makeRequest(this.retrofitConfig.productReportController.getProductReport(AuthenticationManager.currentUser.value!!.userID,productID!!),{
           this._hasReport.value = it
           this._searchingReport.value = false
        },{
            this._searchingReport.value = false
            this._hasReport.value = null
        })
    }

    fun getConversation() {
        this._searchingConversation.value = true;
        this.makeRequest(this.retrofitConfig.conversationController.getConversationByStarter(AuthenticationManager.currentUser.value!!.userID,productID!!),{
           this._searchingConversation.value = false;
           this._hasConversation.value = it
        },{
            this._searchingConversation.value = false;
            this._hasConversation.value = null
        })
    }

    private fun updateProductImages() {
        this.makeRequest(this.retrofitConfig.productImageController.getAmount(this.productID!!),{
            this._currentProductImagesAmount.value = it
        })
    }

    private fun updateCurrentSimilarProducts(page: Boolean) {
        this.makeRequest(this.retrofitConfig.productController.getSimilarProducts(currentProductDetails.value!!.id,_similarProductsPage.value.number,20),{
            if(it._embedded != null) {
                if(!page)
                    this._similarProducts.value = it._embedded.content;
                else
                {
                    val mutableList: MutableList<Product> = mutableListOf()
                    mutableList.addAll(this._similarProducts.value)
                    mutableList.addAll(it._embedded.content)
                    this._similarProducts.value = mutableList
                }
                this._similarProductsPage.value = this._sellerProductsPage.value.copy(size = it.page.size, totalElements = it.page.totalElements,totalPages = it.page.totalPages,number = it.page.number)
            }
        })
    }

    private fun updateCurrentSellerProducts(page: Boolean) {
        this.makeRequest(this.retrofitConfig.productController.getSellerProducts(_currentProductDetails.value!!.seller.id),{
            if(it._embedded != null)
            {
                if(!page)
                    this._sellerProducts.value = it._embedded.content;
                else
                {
                    val mutableList: MutableList<Product> = mutableListOf()
                    mutableList.addAll(this._sellerProducts.value)
                    mutableList.addAll(it._embedded.content);
                    this._sellerProducts.value = mutableList
                }
            }
            this._sellerProductsPage.value = this._sellerProductsPage.value.copy(size = it.page.size, totalElements = it.page.totalElements, totalPages = it.page.totalPages,number = it.page.number)
        })
    }

    fun updateCurrentPage(index: Int) {
        val currentPage: MutableStateFlow<Page> = if(index == 0) _similarProductsPage else _sellerProductsPage;
        if(currentPage.value.number + 1 >= currentPage.value.totalPages)
            return;
        when(index) {
            0 -> this.updateCurrentSimilarProducts(true);
            1 -> this.updateCurrentSellerProducts(true);
        }
    }

    fun updateCurrentIndex(index: Int) {
        this._currentSelectedIndex.value = index;
    }

    fun addLike() {
        this._searchingLike.value = true
        this.makeRequest(this.retrofitConfig.likeController.createLike(productID!!),{
            this._hasLike.value = it
            this._searchingLike.value = false
        },{
            this._hasLike.value = null
            this._searchingLike.value = false
        })
    }

    fun removeLike() {
        if(this._hasLike.value != null) {
            this._searchingLike.value = true
            this.makeDeleteRequest(this.retrofitConfig.likeController.deleteLike(this._hasLike.value!!.id),{
                this._hasLike.value = null
                this._searchingLike.value = false
            },{
                this._searchingLike.value = false
            })
        }
    }

    fun createConversation() {
        this._creatingConversations.value = true
        if(this._currentProductDetails.value != null) {
            val createConversation: CreateConversation = CreateConversation(currentProductDetails.value!!.id)
            this.makeRequest(this.retrofitConfig.conversationController.createConversation(createConversation),{
               this._conversationCreated.value = it
            },{this._conversationCreated.value = null})
        }
    }

    fun closeConversationAlert() {
        this._creatingConversations.value = false
    }

    val hasLike: StateFlow<Like?> = _hasLike.asStateFlow()
    val hasReport: StateFlow<ProductReport?> = _hasReport.asStateFlow()
    val hasOffer: StateFlow<Offer?> = _hasOffer.asStateFlow()
    val hasConversation: StateFlow<Conversation?> = _hasConversation.asStateFlow()
    val searchingLike: StateFlow<Boolean> = _searchingLike.asStateFlow()
    val searchingReport: StateFlow<Boolean> = _searchingReport.asStateFlow()
    val searchingOffer: StateFlow<Boolean> = _searchingOffer.asStateFlow()
    val searchingConversation: StateFlow<Boolean> = _searchingConversation.asStateFlow()
    val currentProductDetails: StateFlow<Product?> = _currentProductDetails.asStateFlow()
    val currentSelectedIndex: StateFlow<Int> = _currentSelectedIndex.asStateFlow()
    val currentProductImagesAmount: StateFlow<Int> = _currentProductImagesAmount.asStateFlow()
    val similarProducts: StateFlow<List<Product>> = _similarProducts.asStateFlow()
    val sellerProducts: StateFlow<List<Product>> = _sellerProducts.asStateFlow()
    val createdConversation: StateFlow<Conversation?> = _conversationCreated.asStateFlow()
    val creatingConversation: StateFlow<Boolean> = _creatingConversations.asStateFlow()
}