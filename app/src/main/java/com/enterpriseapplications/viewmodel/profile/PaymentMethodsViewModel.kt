package com.enterpriseapplications.viewmodel.profile

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.model.PaymentMethod
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaymentMethodsViewModel(val application: CustomApplication): BaseViewModel(application)
{
    private var _currentPaymentMethods: MutableStateFlow<List<PaymentMethod>> = MutableStateFlow(emptyList())
    private var _currentPaymentMethodsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init
    {
        this.initialize()
    }

    fun initialize() {
        this._currentPaymentMethodsSearching.value = true;
        this.makeRequest(this.retrofitConfig.paymentMethodController.getPaymentMethods(AuthenticationManager.currentUser.value!!.userID),{
            this._currentPaymentMethods.value = it
            this._currentPaymentMethodsSearching.value = false
        },{this._currentPaymentMethodsSearching.value = false})
    }
    fun addPaymentMethod(paymentMethod: PaymentMethod) {
        val mutableList: MutableList<PaymentMethod> = _currentPaymentMethods.value.toMutableList()
        mutableList.add(paymentMethod)
        this._currentPaymentMethods.value = mutableList
    }
    val currentPaymentMethods: StateFlow<List<PaymentMethod>> = _currentPaymentMethods.asStateFlow()
    val currentPaymentMethodSearching: StateFlow<Boolean> = _currentPaymentMethodsSearching.asStateFlow()
}