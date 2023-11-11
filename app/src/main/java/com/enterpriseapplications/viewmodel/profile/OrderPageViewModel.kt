package com.enterpriseapplications.viewmodel.profile

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.model.Order
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class OrderPageViewModel(val application: CustomApplication): BaseViewModel(application) {
    var userID: UUID? = AuthenticationManager.currentUser.value!!.userID;
    private var _orders: MutableStateFlow<List<Order>> = MutableStateFlow(emptyList())
    private var _ordersPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0))
    private var _ordersSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);

    init
    {
        this.initialize()
    }

    fun initialize() {
        this.updateOrders(false)
    }
    private fun updateOrders(page: Boolean) {
        this._ordersSearching.value = !page;
        this.makeRequest(this.retrofitConfig.orderController.getOrders(userID!!,_ordersPage.value.number,20),{
            if(it._embedded != null)
            {
                if(!page)
                    this._orders.value = it._embedded.content;
                else
                {
                    val mutableList: MutableList<Order> = mutableListOf()
                    mutableList.addAll(this._orders.value)
                    mutableList.addAll(it._embedded.content)
                    this._orders.value = mutableList
                }
            }
            this._ordersSearching.value = false;
            this._ordersPage.value = this._ordersPage.value.copy(size = it.page.size,totalElements = it.page.totalElements,totalPages = it.page.totalPages,number = it.page.number)
        })
    }

    fun resetSearch() {
        this._ordersPage.value = Page(20,0,0,0)
        this.updateOrders(page = false)
    }
    fun updateCurrentPage() {
        if(this._ordersPage.value.number + 1 >= this._ordersPage.value.totalPages)
            return;
        this._ordersPage.value = this._ordersPage.value.copy(size = this._ordersPage.value.size, totalElements = this._ordersPage.value.totalElements,totalPages = this._ordersPage.value.totalPages,number = this._ordersPage.value.number + 1)
        this.updateOrders(page = true)
    }

    val orders: StateFlow<List<Order>> = _orders
    val ordersPage: StateFlow<Page> = _ordersPage
    val ordersSearching: StateFlow<Boolean> = _ordersSearching.asStateFlow()
}