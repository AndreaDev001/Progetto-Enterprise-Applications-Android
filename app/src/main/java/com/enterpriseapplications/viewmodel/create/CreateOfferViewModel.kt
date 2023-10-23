package com.enterpriseapplications.viewmodel.create

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.create.CreateOffer
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal
import java.util.UUID

class CreateOfferViewModel(val application: CustomApplication) : BaseViewModel(application)
{
    var productID: UUID? = null
    var update: Boolean = false
    private var _priceControl: FormControl<String?> = FormControl("", Validators.required())
    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _success: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _formGroup: FormGroup = FormGroup(_priceControl,_descriptionControl)

    fun createOffer() {
        val requiredValue: BigDecimal = this._priceControl.currentValue.value!!.toBigDecimal()
        val createOffer: CreateOffer = CreateOffer(requiredValue,_descriptionControl.currentValue.value!!,productID!!);
        this.makeRequest(this.retrofitConfig.offerController.createOffer(createOffer),{
            this._success.value = true
        })
    }

    val priceControl: FormControl<String?> = _priceControl
    val descriptionControl: FormControl<String?> = _descriptionControl
    val formGroup: FormGroup = _formGroup
    val success: StateFlow<Boolean> = _success.asStateFlow()
}