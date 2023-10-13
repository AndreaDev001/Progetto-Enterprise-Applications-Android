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
        this.updateOrders(page = false,first = true)
    }
    fun updateOrders(page: Boolean,first: Boolean) {
        if(page && !first && this._ordersPage.value.number + 1 >= this._ordersPage.value.totalPages)
            return;
        this.makeRequest(this.retrofitConfig.orderController.getOrders(userID!!),{
            if(it._embedded != null)
                this._orders.value.toMutableList().addAll(it._embedded.content)
            this._ordersPage.value = this._ordersPage.value.copy(size = it.page.size,totalElements = it.page.totalElements,totalPages = it.page.totalPages,number = it.page.number)
        })
    }

    val orders: StateFlow<List<Order>> = _orders
    val ordersPage: StateFlow<Page> = _ordersPage
}