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
    private var _currentRecentProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private var _currentMostLikedProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private var _currentMostExpensiveProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private var _currentRecentProductsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _currentMostLikedProductsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _currentMostExpensiveProductsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _currentRecentProductsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);
    private var _currentMostLikedProductsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);
    private var _currentMostExpensiveProductsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);

    init
    {
        this.initialize()
    }

    fun initialize() {
        this.updateMostLikedProducts(page = false);
        this.updateRecentProducts(page = false);
        this.updateMostExpensiveProducts(page = false);
    }

    private fun updateMostLikedProducts(page: Boolean) {
        this._currentMostLikedProductsSearching.value = !page;
        this.makeRequest(this.retrofitConfig.productController.getMostLiked(this._currentMostLikedProductsPage.value.number,20),{
            if(it._embedded != null) {
                if(!page)
                    this._currentMostLikedProducts.value = it._embedded.content;
                else
                {
                    val mutableList: MutableList<Product> = mutableListOf()
                    mutableList.addAll(this._currentMostLikedProducts.value)
                    mutableList.addAll(it._embedded.content)
                    this._currentMostLikedProducts.value = mutableList
                }
            }
            this._currentMostLikedProductsSearching.value = false;
            this._currentMostLikedProductsPage.value = this._currentMostLikedProductsPage.value.copy(size = it.page.size, totalElements = it.page.totalElements, totalPages = it.page.totalPages, number = it.page.number)
        })
    }
    private fun updateRecentProducts(page: Boolean) {
        this._currentRecentProductsSearching.value = !page;
        this.makeRequest(this.retrofitConfig.productController.getRecentlyCreated(this._currentRecentProductsPage.value.number,20),{
            if(it._embedded != null)
            {
                if(!page)
                    this._currentRecentProducts.value = it._embedded.content;
                else
                {
                    val mutableList: MutableList<Product> = mutableListOf()
                    mutableList.addAll(this._currentRecentProducts.value)
                    mutableList.addAll(it._embedded.content)
                    this._currentRecentProducts.value = mutableList
                }
            }
            this._currentRecentProductsSearching.value = false;
            this._currentRecentProductsPage.value =  this._currentRecentProductsPage.value.copy(size = it.page.size, totalElements = it.page.totalElements, totalPages = it.page.totalPages,number = it.page.number)
        })
    }
    private fun updateMostExpensiveProducts(page: Boolean) {
        this._currentMostExpensiveProductsSearching.value = !page;
        this.makeRequest(this.retrofitConfig.productController.getMostExpensive(this._currentMostExpensiveProductsPage.value.number,20),{
            if(it._embedded != null) {
                if(!page)
                    this._currentMostExpensiveProducts.value = it._embedded.content;
                else
                {
                    val mutableList: MutableList<Product> = mutableListOf()
                    mutableList.addAll(this._currentMostExpensiveProducts.value)
                    mutableList.addAll(it._embedded.content)
                    this._currentMostExpensiveProducts.value = mutableList
                }
            }
            this._currentMostExpensiveProductsSearching.value = false;
            this._currentMostExpensiveProductsPage.value =  this._currentMostExpensiveProductsPage.value.copy(size = it.page.size, totalElements = it.page.totalElements, totalPages = it.page.totalPages,number = it.page.number)
        })
    }

    fun updateCurrentPage(index: Int) {
        val currentPage: MutableStateFlow<Page> = if(index == 0) _currentRecentProductsPage else if(index == 1) _currentMostLikedProductsPage else _currentMostExpensiveProductsPage;
        if(currentPage.value.number + 1 >= currentPage.value.totalPages)
            return;
        currentPage.value = currentPage.value.copy(size = currentPage.value.size,number = currentPage.value.number + 1, totalPages = currentPage.value.totalPages, totalElements = currentPage.value.totalElements)
        when(index) {
            0 -> this.updateRecentProducts(true);
            1 -> this.updateMostLikedProducts(true);
            2 -> this.updateMostExpensiveProducts(true);
        }
    }
    fun resetPage(index: Int) {
        val currentPage: MutableStateFlow<Page> = if(index == 0) _currentRecentProductsPage else if(index == 1) _currentMostLikedProductsPage else _currentMostExpensiveProductsPage;
        currentPage.value = currentPage.value.copy(size = 20,totalPages = 0, totalElements = 0,number = 0)
        when(index) {
            0 -> this.updateRecentProducts(false);
            1 -> this.updateMostLikedProducts(false);
            2 -> this.updateMostExpensiveProducts(false);
        }
    }

    val currentRecentProducts: StateFlow<List<Product>> = _currentRecentProducts.asStateFlow();
    val currentMostLikedProducts: StateFlow<List<Product>> = _currentMostLikedProducts.asStateFlow()
    val currentMostExpensiveProducts: StateFlow<List<Product>> = _currentMostExpensiveProducts.asStateFlow()
    val currentRecentProductsPage: StateFlow<Page> = _currentRecentProductsPage.asStateFlow()
    val currentMostLikedProductsPage: StateFlow<Page> = _currentMostLikedProductsPage.asStateFlow()
    val currentMostExpensiveProductsPage: StateFlow<Page> = _currentMostExpensiveProductsPage.asStateFlow()
    val currentRecentProductsSearching: StateFlow<Boolean> = _currentRecentProductsSearching.asStateFlow()
    val currentMostLikedProductsSearching: StateFlow<Boolean> = _currentMostLikedProductsSearching.asStateFlow()
    val currentMostExpensiveProductsSearching: StateFlow<Boolean> = _currentMostExpensiveProductsSearching.asStateFlow()
}