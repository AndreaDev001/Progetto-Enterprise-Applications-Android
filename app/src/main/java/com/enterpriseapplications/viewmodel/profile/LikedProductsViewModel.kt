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
    private var _currentLikedProductsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);

    fun initialize() {
        if(userID != null)
            this.updateLikedProducts(page = false)
    }
    private fun updateLikedProducts(page: Boolean) {
        this._currentLikedProductsSearching.value = !page;
        this.makeRequest(this.retrofitConfig.likeController.getLikedProducts(userID!!,this._currentLikedProductsPage.value.number,20),{
            if(it._embedded != null) {
                if(!page)
                    this._currentLikedProducts.value = it._embedded.content
                else
                {
                    val mutableList: MutableList<Like> = mutableListOf()
                    mutableList.addAll(this._currentLikedProducts.value)
                    mutableList.addAll(it._embedded.content)
                    this._currentLikedProducts.value = mutableList
                }
            }
            this._currentLikedProductsSearching.value = false;
            this._currentLikedProductsPage.value = this._currentLikedProductsPage.value.copy(size = it.page.size,number = it.page.number, totalPages = it.page.totalPages, totalElements =  it.page.totalElements)
        })
    }
    fun resetSearch() {
        this._currentLikedProductsPage.value = this._currentLikedProductsPage.value.copy(size = 20, totalElements = 0,totalPages = 0,number = 0)
        this.updateLikedProducts(page = false)
    }
    fun updateCurrentPage() {
        if(this._currentLikedProductsPage.value.number + 1 >= this._currentLikedProductsPage.value.totalPages)
            return;
        this._currentLikedProductsPage.value = this._currentLikedProductsPage.value.copy(size = this._currentLikedProductsPage.value.size, totalElements = this._currentLikedProductsPage.value.totalElements, totalPages = this._currentLikedProductsPage.value.totalPages, number = this._currentLikedProductsPage.value.number + 1)
        this.updateLikedProducts(page = true)
    }
    val currentLikedProducts: StateFlow<List<Like>> = _currentLikedProducts.asStateFlow();
    val currentLikedProductsPage: StateFlow<Page> = _currentLikedProductsPage.asStateFlow()
    val currentLikedProductsSearching: StateFlow<Boolean> = _currentLikedProductsSearching.asStateFlow()
}