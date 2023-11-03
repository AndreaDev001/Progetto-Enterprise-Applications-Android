package com.enterpriseapplications.viewmodel

import androidx.compose.runtime.State
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.create.CreateConversation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ProductDetailsViewModel(val application: CustomApplication): BaseViewModel(application) {
    var productID: UUID? = null;

    private var _hasLike: MutableStateFlow<Boolean> = MutableStateFlow(false);
    private var _currentProductDetails: MutableStateFlow<Product?> = MutableStateFlow(null);
    private var _currentSelectedIndex: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentProductImagesAmount: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _similarProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private var _sellerProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private var _similarProductsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _sellerProductsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _conversationCreated: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun initialize()
    {
        if(this.productID != null)
        {
            this.makeRequest(this.retrofitConfig.productController.getDetails(this.productID!!),{
                this._currentProductDetails.value = it
                this.updateProductImages()
                this.updateCurrentSimilarProducts(false)
                this.updateCurrentSellerProducts(false)
                this.makeRequest(this.retrofitConfig.likeController.getLike(AuthenticationManager.currentUser.value!!.userID,productID!!),{
                    this._hasLike.value = true;
                }, errorCallback = {this._hasLike.value = false})
            })
        }
    }
    
    private fun updateProductImages() {
        this.makeRequest(this.retrofitConfig.productImageController.getAmount(this.productID!!),{
            this._currentProductImagesAmount.value = it
        })
    }

    private fun updateCurrentSimilarProducts(page: Boolean) {
        this.makeRequest(this.retrofitConfig.productController.getSimilarProducts(UUID.fromString(_currentProductDetails.value!!.id),_similarProductsPage.value.number,20),{
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
        this.makeRequest(this.retrofitConfig.productController.getSellerProducts(UUID.fromString(_currentProductDetails.value!!.seller.id)),{
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
        this.makeRequest(this.retrofitConfig.likeController.createLike(productID!!),{
            this._hasLike.value = true
        })
    }

    fun removeLike() {
        this.makeRequest(this.retrofitConfig.likeController.deleteLikeByProduct(productID!!),{
            this._hasLike.value = false
        })
    }

    fun createConversation() {
        if(this._currentProductDetails.value != null) {
            val createConversation: CreateConversation = CreateConversation(UUID.fromString(this._currentProductDetails.value!!.id))
            this.makeRequest(this.retrofitConfig.conversationController.createConversation(createConversation),{
               this._conversationCreated.value = true
            },{this._conversationCreated.value = false})
        }
    }

    fun updateCreateConversation(value: Boolean) {
        this._conversationCreated.value = value
    }

    val hasLike: StateFlow<Boolean> = _hasLike.asStateFlow()
    val currentProductDetails: StateFlow<Product?> = _currentProductDetails.asStateFlow()
    val currentSelectedIndex: StateFlow<Int> = _currentSelectedIndex.asStateFlow()
    val currentProductImagesAmount: StateFlow<Int> = _currentProductImagesAmount.asStateFlow()
    val similarProducts: StateFlow<List<Product>> = _similarProducts.asStateFlow()
    val sellerProducts: StateFlow<List<Product>> = _sellerProducts.asStateFlow()
    val createdConversation: StateFlow<Boolean> = _conversationCreated.asStateFlow()
}