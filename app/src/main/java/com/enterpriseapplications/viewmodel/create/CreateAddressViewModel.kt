package com.enterpriseapplications.viewmodel.create

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Address
import com.enterpriseapplications.model.AddressItem
import com.enterpriseapplications.model.create.CreateAddress
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateAddressViewModel(val application: CustomApplication): BaseViewModel(application) {

    private var _currentCountries: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _currentCountriesSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _currentCandidates: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _currentCandidatesSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);
    private var _createdAddress: MutableStateFlow<Address?> = MutableStateFlow(null)

    private var _queryControl: FormControl<String?> = FormControl("",Validators.required())
    private var _selectedStreetControl: FormControl<String?> = FormControl("",Validators.required())
    private var _streetControl: FormControl<String?> = FormControl("",Validators.required())
    private var _countryCodeControl: FormControl<String?> = FormControl("",Validators.required())
    private var _localityControl: FormControl<String?> = FormControl("",Validators.required())
    private var _postalCodeControl: FormControl<String?> = FormControl("",Validators.required())
    private var _ownerNameControl: FormControl<String?> = FormControl("",Validators.required())
    private var _formGroup: FormGroup = FormGroup(_queryControl,_selectedStreetControl,_streetControl,_countryCodeControl,_postalCodeControl,_ownerNameControl)

    init
    {
        this.initialize()
    }
    fun initialize() {
        this._currentCountriesSearching.value = true
        this.makeRequest(this.retrofitConfig.addressController.getCountries(),{
            this._currentCountries.value = it
            this._currentCountriesSearching.value = false
        },{this._currentCountriesSearching.value = false})
    }

    fun searchForAddresses() {
        this._currentCandidatesSearching.value = true
        if(_countryCodeControl.currentValue.value != null && _queryControl.currentValue.value != null) {
            this.makeRequest(this.retrofitConfig.addressController.searchForAddresses(_countryCodeControl.currentValue.value!!,_queryControl.currentValue.value!!),{
                this._currentCandidatesSearching.value = false
                val candidates: List<AddressItem> = it.candidates
                val values: MutableList<String> = mutableListOf()
                for(current in candidates) {
                    val value: String = "${current.street},${current.locality},${current.postal_code}";
                    values.add(value);
                }
                this._currentCandidates.value = values
            },{this._currentCandidatesSearching.value = false})
        }
    }

    fun resetSearch() {
        this._selectedStreetControl.updateValue("");
        this._streetControl.updateValue("");
        this._localityControl.updateValue("");
        this._postalCodeControl.updateValue("");
    }

    fun updateSelectedStreet(requiredValue: String) {
        val values: List<String> = requiredValue.split(",");
        this._streetControl.updateValue(values[0])
        this._localityControl.updateValue(values[1]);
        this._postalCodeControl.updateValue(values[2]);
    }

    fun createAddress() {
        val createAddress: CreateAddress = CreateAddress(postalCode = this._postalCodeControl.currentValue.value!!,
        countryCode = this._countryCodeControl.currentValue.value!!, street = this._streetControl.currentValue.value!!, locality = this._localityControl.currentValue.value!!,
        ownerName = this._ownerNameControl.currentValue.value!!)
        this.makeRequest(this.retrofitConfig.addressController.createAddress(createAddress),{
            this._createdAddress.value = it
        },{this._createdAddress.value = null})
    }
    fun reset() {
        this._formGroup.reset()
        this._createdAddress.value = null;
    }
    val currentCountries: StateFlow<List<String>> = _currentCountries.asStateFlow()
    val currentCountriesSearching: StateFlow<Boolean> = _currentCountriesSearching.asStateFlow()
    val currentCandidates: StateFlow<List<String>> = _currentCandidates.asStateFlow()
    val createdAddress: StateFlow<Address?> = _createdAddress.asStateFlow()
    val selectedStreetControl: FormControl<String?> = _selectedStreetControl;
    val ownerNameControl: FormControl<String?> = _ownerNameControl;
    val localityControl: FormControl<String?> = _localityControl
    val queryControl: FormControl<String?> = _queryControl
    val streetControl: FormControl<String?> = _streetControl
    val countryCodeControl: FormControl<String?> = _countryCodeControl
    val postalCodeControl: FormControl<String?> = _postalCodeControl
    val formGroup: FormGroup = _formGroup
}