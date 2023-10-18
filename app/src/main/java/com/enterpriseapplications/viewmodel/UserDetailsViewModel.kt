package com.enterpriseapplications.viewmodel

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.Review
import com.enterpriseapplications.model.UserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class UserDetailsViewModel(val application: CustomApplication): BaseViewModel(application) {
    var userID: UUID? = null;
    private var _currentSelectedTab: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentUserDetails: MutableStateFlow<UserDetails?> = MutableStateFlow(null)
    private var _currentReviews: MutableStateFlow<List<Review>> = MutableStateFlow(emptyList())
    private var _currentProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private var _currentReviewsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _currentProductsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _currentReviewsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);
    private var _currentProductsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);

    fun initialize() {
        if(this.userID != null) {
            this.makeRequest(this.retrofitConfig.userController.getUserDetails(userID!!),{
                this._currentUserDetails.value = it
                this.updateReviews(false);
                //this.updateProducts(false);
            })
        }
    }
    private fun updateReviews(page: Boolean) {
        this._currentReviewsSearching.value = true;
        this.makeRequest(this.retrofitConfig.reviewController.getWrittenReviews(userID!!,this._currentReviewsPage.value.number,20),{
            if(it._embedded != null) {
                if(!page)
                    this._currentReviews.value = it._embedded.content
                else
                {
                    val mutableList: MutableList<Review> = mutableListOf()
                    mutableList.addAll(this._currentReviews.value)
                    mutableList.addAll(it._embedded.content)
                    this._currentReviews.value = mutableList
                }
            }
            this._currentReviewsSearching.value = false
            this._currentReviewsPage.value = this._currentReviewsPage.value.copy(size = it.page.size, totalPages = it.page.totalPages, totalElements = it.page.totalElements,number = it.page.number)
        })
    }
    private fun updateProducts(page: Boolean) {
        this._currentProductsSearching.value = true
        this.makeRequest(this.retrofitConfig.productController.getSellerProducts(userID!!,this._currentProductsPage.value.number,20),{
            if(it._embedded != null) {
                if(!page)
                    this._currentProducts.value = it._embedded.content
                else
                {
                    val mutableList: MutableList<Product> = mutableListOf()
                    mutableList.addAll(this._currentProducts.value)
                    mutableList.addAll(it._embedded.content)
                    this._currentProducts.value = mutableList
                }
            }
            this._currentProductsSearching.value = false
            this._currentProductsPage.value = this._currentProductsPage.value.copy(size = it.page.size,totalPages = it.page.totalPages, totalElements = it.page.totalElements,number = it.page.number)
        })
    }

    fun updateCurrentPage(index: Int) {
     val currentPage: MutableStateFlow<Page> = if(index == 0) _currentReviewsPage else _currentProductsPage
     if(currentPage.value.number + 1 >= currentPage.value.totalPages)
         return;
     when(index) {
         0 -> this.updateReviews(true);
     }
    }
    fun resetTab(index: Int) {
        val currentPage: MutableStateFlow<Page> = if(index == 0) _currentReviewsPage else _currentProductsPage
        currentPage.value = currentPage.value.copy(size = 20,totalElements = 0,totalPages = 0,number = 0)
        when(index) {
            0 -> this.updateReviews(false)
            1 -> this.updateProducts(false)
        }
    }
    fun updateCurrentTab(index: Int) {
        this._currentSelectedTab.value = index
        when(index) {
            0 -> this.updateReviews(false);
            1 -> this.updateProducts(false);
        }
    }
    val currentSelectedTab: StateFlow<Int> = _currentSelectedTab.asStateFlow();
    val currentUserDetails: StateFlow<UserDetails?> = _currentUserDetails.asStateFlow()
    val currentReviews: StateFlow<List<Review>> = _currentReviews.asStateFlow()
    val currentProducts: StateFlow<List<Product>> = _currentProducts.asStateFlow()
    val currentReviewsPage: StateFlow<Page> = _currentReviewsPage.asStateFlow()
    val currentProductsPage: StateFlow<Page> = _currentProductsPage.asStateFlow()
    val currentReviewsSearching: StateFlow<Boolean> = _currentReviewsSearching.asStateFlow()
    val currentProductsSearching: StateFlow<Boolean> = _currentProductsSearching.asStateFlow()
}