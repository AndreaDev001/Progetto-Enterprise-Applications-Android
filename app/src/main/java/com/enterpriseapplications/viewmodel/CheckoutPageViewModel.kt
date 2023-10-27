package com.enterpriseapplications.viewmodel

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.model.Address
import com.enterpriseapplications.model.PaymentMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CheckoutPageViewModel(val application: CustomApplication): BaseViewModel(application)
{
    private var _currentPaymentMethods: MutableStateFlow<List<PaymentMethod>> = MutableStateFlow(emptyList());
    private var _currentPaymentMethodsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);
    private var _currentAddresses: MutableStateFlow<List<Address>> = MutableStateFlow(emptyList())
    private var _currentAddressesSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun init() {
        this.initialize()
    }

    fun initialize() {
        this._currentPaymentMethodsSearching.value = true
        this._currentAddressesSearching.value = true
        this.makeRequest(this.retrofitConfig.paymentMethodController.getPaymentMethods(
            AuthenticationManager.currentUser.value!!.userID),{
             this._currentPaymentMethods.value = it
             this._currentPaymentMethodsSearching.value = false
        },{this._currentPaymentMethodsSearching.value = false})
        this.makeRequest(this.retrofitConfig.addressController.getAddresses(AuthenticationManager.currentUser.value!!.userID),{
            this._currentAddresses.value = it
            this._currentAddressesSearching.value = false
        },{this._currentAddressesSearching.value = false})
    }

    val currentPaymentMethods: StateFlow<List<PaymentMethod>> = _currentPaymentMethods.asStateFlow()
    val currentPaymentMethodsSearching: StateFlow<Boolean> = _currentPaymentMethodsSearching.asStateFlow()
    val currentAddresses: StateFlow<List<Address>> = _currentAddresses.asStateFlow()
    val currentAddressesSearching: StateFlow<Boolean> = _currentAddressesSearching.asStateFlow()
}