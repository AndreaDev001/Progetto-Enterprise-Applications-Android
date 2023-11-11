package com.enterpriseapplications.viewmodel.profile

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.model.Address
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddressPageViewModel(val application: CustomApplication): BaseViewModel(application) {

    private var _currentAddresses: MutableStateFlow<List<Address>> = MutableStateFlow(emptyList())
    private var _currentAddressesSearching: MutableStateFlow<Boolean> = MutableStateFlow((false))

    init
    {
        this.initialize()
    }

    fun initialize() {
        this._currentAddressesSearching.value = true;
        this.makeRequest(this.retrofitConfig.addressController.getAddresses(AuthenticationManager.currentUser.value!!.userID),{
            this._currentAddresses.value = it
            this._currentAddressesSearching.value = false
        }, {this._currentAddressesSearching.value = false})
    }
    fun addAddress(address: Address) {
        val mutableList: MutableList<Address> = mutableListOf(address);
        mutableList.addAll(this._currentAddresses.value)
        this._currentAddresses.value  = mutableList
    }
    val currentAddresses: StateFlow<List<Address>> = _currentAddresses.asStateFlow()
    val currentAddressesSearching: StateFlow<Boolean> = _currentAddressesSearching.asStateFlow()
}