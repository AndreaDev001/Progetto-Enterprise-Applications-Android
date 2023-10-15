package com.enterpriseapplications.viewmodel.profile

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.model.Order
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class OrderPageViewModel(val application: CustomApplication): BaseViewModel(application) {
    var userID: UUID? = null;
    private var _orders: MutableStateFlow<List<Order>> = MutableStateFlow(emptyList())
    private var _ordersPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0))

    fun initialize() {
        if(this.userID != null)
             this.updateOrders(page = false)
    }
    fun updateOrders(page: Boolean) {
        this.makeRequest(this.retrofitConfig.orderController.getOrders(userID!!,_ordersPage.value.number,_ordersPage.value.totalPages),{
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
            this._ordersPage.value = this._ordersPage.value.copy(size = it.page.size,totalElements = it.page.totalElements,totalPages = it.page.totalPages,number = it.page.number)
        })
    }

    fun resetSearch() {
        this._ordersPage.value = this._ordersPage.value.copy(size = this._ordersPage.value.size,totalElements = 0,totalPages = this._ordersPage.value.totalPages,number = 0)
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
}