package com.enterpriseapplications.viewmodel

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomePageViewModel(val application: CustomApplication): BaseViewModel(application)
{
    private var _currentRecentProducts: MutableStateFlow<MutableList<Product>> = MutableStateFlow(
        mutableListOf()
    )
    private var _currentMostLikedProducts: MutableStateFlow<MutableList<Product>> = MutableStateFlow(
        mutableListOf()
    )
    private var _currentMostExpensiveProducts: MutableStateFlow<MutableList<Product>> = MutableStateFlow(
        mutableListOf()
    )
    private var _currentRecentProductsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _currentMostLikedProductsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _currentMostExpensiveProductsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));

    init
    {
        this.updateMostLikedProducts(page = false,first = true)
        this.updateRecentProducts(page = false,first = true);
        this.updateMostExpensiveProducts(page = false,first = true);
    }

    fun updateMostLikedProducts(page: Boolean,first: Boolean = false) {
        if(first)
            this._currentMostLikedProducts.value = mutableListOf()
        if(page)
            this._currentMostLikedProductsPage.value = this._currentMostLikedProductsPage.value.copy(number = this._currentMostLikedProductsPage.value.number + 1)
        if(!first && this._currentMostLikedProductsPage.value.number + 1 >= this._currentMostLikedProductsPage.value.totalPages)
            return;
        this.makeRequest(this.retrofitConfig.productController.getMostLiked(this._currentMostLikedProductsPage.value.number,20),{
            if(it._embedded != null) {
                val mutableList: MutableList<Product> = mutableListOf()
                mutableList.addAll(this._currentMostLikedProducts.value)
                mutableList.addAll(it._embedded.content)
                this._currentMostLikedProducts.value = mutableList
            }
            this._currentMostLikedProductsPage.value = this._currentMostLikedProductsPage.value.copy(size = it.page.size, totalElements = it.page.totalElements, totalPages = it.page.totalPages, number = it.page.number)
        })
    }
    fun updateRecentProducts(page: Boolean,first: Boolean = false) {
        if(first)
            this._currentRecentProducts.value = mutableListOf()
        if(page)
            this._currentRecentProductsPage.value = this._currentRecentProductsPage.value.copy(number = this._currentRecentProductsPage.value.number + 1)
        if(!first && this._currentRecentProductsPage.value.totalPages + 1 >= this._currentRecentProductsPage.value.totalPages)
            return;
        this.makeRequest(this.retrofitConfig.productController.getRecentlyCreated(this._currentRecentProductsPage.value.number,20),{
            if(it._embedded != null)
            {
                val mutableList: MutableList<Product> = mutableListOf()
                mutableList.addAll(this._currentRecentProducts.value)
                mutableList.addAll(it._embedded.content)
                this._currentRecentProducts.value = mutableList
            }
            this._currentRecentProductsPage.value =  this._currentRecentProductsPage.value.copy(size = it.page.size, totalElements = it.page.totalElements, totalPages = it.page.totalPages,number = it.page.number)
        })
    }
    fun updateMostExpensiveProducts(page: Boolean,first: Boolean) {
        if(first)
            this._currentMostExpensiveProducts.value = mutableListOf()
        if(page)
            this._currentMostExpensiveProductsPage.value = this._currentMostExpensiveProductsPage.value.copy(number = this._currentMostExpensiveProductsPage.value.number + 1)
        if(!first && this._currentMostExpensiveProductsPage.value.totalPages + 1 >= this._currentMostExpensiveProductsPage.value.totalPages)
            return;
        this.makeRequest(this.retrofitConfig.productController.getMostExpensive(this._currentMostExpensiveProductsPage.value.number,20),{
            if(it._embedded != null) {
                val mutableList: MutableList<Product> = mutableListOf()
                mutableList.addAll(this._currentMostExpensiveProducts.value)
                mutableList.addAll(it._embedded.content)
                this._currentMostExpensiveProducts.value = mutableList
            }
            this._currentMostExpensiveProductsPage.value =  this._currentMostExpensiveProductsPage.value.copy(size = it.page.size, totalElements = it.page.totalElements, totalPages = it.page.totalPages,number = it.page.number)
        })
    }
    val currentRecentProducts: StateFlow<List<Product>> = _currentRecentProducts.asStateFlow();
    val currentMostLikedProducts: StateFlow<List<Product>> = _currentMostLikedProducts.asStateFlow()
    val currentMostExpensiveProducts: StateFlow<List<Product>> = _currentMostExpensiveProducts.asStateFlow()
    val currentRecentProductsPage: StateFlow<Page> = _currentRecentProductsPage.asStateFlow()
    val currentMostLikedProductsPage: StateFlow<Page> = _currentMostLikedProductsPage.asStateFlow()
    val currentMostExpensiveProductsPage: StateFlow<Page> = _currentMostExpensiveProductsPage.asStateFlow()
}