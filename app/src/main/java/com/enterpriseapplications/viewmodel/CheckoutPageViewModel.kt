package com.enterpriseapplications.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.model.Address
import com.enterpriseapplications.model.PaymentMethod
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.create.CreateOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal
import java.util.UUID

class CheckoutPageViewModel(val application: CustomApplication): BaseViewModel(application)
{
    var productID: UUID? = null
    var price: BigDecimal? = null
    private var _currentProductDetails: MutableStateFlow<Product?> = MutableStateFlow(null)
    private var _currentPaymentMethods: MutableStateFlow<List<PaymentMethod>> = MutableStateFlow(emptyList());
    private var _currentProductSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _currentPaymentMethodsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);
    private var _currentAddresses: MutableStateFlow<List<Address>> = MutableStateFlow(emptyList())
    private var _currentAddressesSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _currentSelectedAddress: MutableStateFlow<Address?> = MutableStateFlow(null);
    private var _currentSelectedPaymentMethod: MutableStateFlow<PaymentMethod?> = MutableStateFlow(null);
    private var _isValid: MutableStateFlow<Boolean> = MutableStateFlow(false);
    private var _success: MutableStateFlow<Boolean> = MutableStateFlow(false);

    fun initialize() {
        if(this.productID == null)
            return;
        this._currentPaymentMethodsSearching.value = true
        this._currentAddressesSearching.value = true
        this._currentProductSearching.value = true
        this.makeRequest(this.retrofitConfig.productController.getDetails(productID!!),{
            this._currentProductDetails.value = it
            this._currentProductSearching.value = false
        },{this._currentProductSearching.value  = false})
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

    fun createOrder() {
        if(this._currentProductDetails.value != null && this._isValid.value) {
            val createOrder: CreateOrder = CreateOrder(productID!!,price!!,this._currentSelectedAddress.value!!.id,this._currentSelectedPaymentMethod.value!!.id);
            this.makeRequest(this.retrofitConfig.orderController.createOrder(createOrder),{
                this._success.value = true;
            },{this._success.value = false})
        }
    }

    fun reset() {
        this._currentSelectedPaymentMethod.value = null;
        this._currentSelectedAddress.value = null;
        this._isValid.value = false
    }
    fun updateCurrentSelectedAddress(address: Address) {
        this._currentSelectedAddress.value = address
        this._isValid.value = this._currentSelectedAddress.value != null && this._currentSelectedPaymentMethod.value != null;
    }
    fun updateCurrentSelectedPaymentMethod(paymentMethod: PaymentMethod) {
        this._currentSelectedPaymentMethod.value = paymentMethod
        this._isValid.value = this._currentSelectedAddress.value != null && this._currentSelectedPaymentMethod.value != null;
    }

    val currentProductDetails: StateFlow<Product?> = _currentProductDetails.asStateFlow()
    val currentPaymentMethods: StateFlow<List<PaymentMethod>> = _currentPaymentMethods.asStateFlow()
    val currentPaymentMethodsSearching: StateFlow<Boolean> = _currentPaymentMethodsSearching.asStateFlow()
    val currentAddresses: StateFlow<List<Address>> = _currentAddresses.asStateFlow()
    val currentAddressesSearching: StateFlow<Boolean> = _currentAddressesSearching.asStateFlow()
    val currentSelectedPaymentMethod: StateFlow<PaymentMethod?> = _currentSelectedPaymentMethod.asStateFlow()
    val currentSelectedAddress: StateFlow<Address?> = _currentSelectedAddress.asStateFlow()
    val currentProductSearching: StateFlow<Boolean> = _currentProductSearching.asStateFlow()
    val isValid: StateFlow<Boolean> = _isValid.asStateFlow()
    val success: StateFlow<Boolean> = _success.asStateFlow()
}