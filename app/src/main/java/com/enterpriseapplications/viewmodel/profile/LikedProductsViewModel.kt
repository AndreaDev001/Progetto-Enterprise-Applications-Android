package com.enterpriseapplications.viewmodel.profile

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.model.Like
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class LikedProductsViewModel(val application: CustomApplication): BaseViewModel(application) {
    var userID: UUID? = null;
    private var _currentLikedProducts: MutableStateFlow<List<Like>> = MutableStateFlow(emptyList())
    private var _currentLikedProductsPage: MutableStateFlow<Page> = MutableStateFlow(Page(size = 20,0,0,0));

    fun initialize() {
        if(userID != null)
            this.updateLikedProducts(page = false,first = true)
    }
    private fun updateLikedProducts(page: Boolean,first: Boolean = false) {
        if(page && !first)
            this._currentLikedProductsPage.value = this._currentLikedProductsPage.value.copy(size = this._currentLikedProductsPage.value.size, totalPages = this._currentLikedProductsPage.value.totalPages, totalElements = this._currentLikedProductsPage.value.totalElements,number = this._currentLikedProductsPage.value.number)
        if(first)
            this._currentLikedProductsPage.value = this._currentLikedProductsPage.value.copy(number = 0)
        this.makeRequest(this.retrofitConfig.likeController.getLikedProducts(userID!!,this._currentLikedProductsPage.value.number,20),{
            if(it._embedded != null) {
                val mutableList: MutableList<Like> = mutableListOf()
                mutableList.addAll(this._currentLikedProducts.value)
                mutableList.addAll(it._embedded.content)
                this._currentLikedProducts.value = mutableList
            }
            this._currentLikedProductsPage.value = this._currentLikedProductsPage.value.copy(size = it.page.size,number = it.page.number, totalPages = it.page.totalPages, totalElements =  it.page.totalElements)
        })
    }
    fun resetSearch() {
        this.updateLikedProducts(page = false,first = true);
    }
    fun updateCurrentPage() {
        if(this._currentLikedProductsPage.value.number + 1 >= this._currentLikedProductsPage.value.totalPages)
            return;
        this.updateLikedProducts(page = true,first = false);
    }
    val currentLikedProducts: StateFlow<List<Like>> = _currentLikedProducts.asStateFlow();
    val currentLikedProductsPage: StateFlow<Page> = _currentLikedProductsPage.asStateFlow()
}