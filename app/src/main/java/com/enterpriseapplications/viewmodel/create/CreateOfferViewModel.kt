package com.enterpriseapplications.viewmodel.create

import androidx.compose.runtime.collectAsState
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Offer
import com.enterpriseapplications.model.create.CreateOffer
import com.enterpriseapplications.model.update.UpdateOfferBuyer
import com.enterpriseapplications.model.update.UpdateOfferSeller
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
    var offerID: UUID? = null;
    private var _buyer: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _searching: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _creatingOffer: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var _priceControl: FormControl<String?> = FormControl("", Validators.required())
    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _offerStatus: FormControl<String?> = FormControl("",Validators.required())

    private var _newOffer: MutableStateFlow<Offer?> = MutableStateFlow(null)
    private var _formGroup: FormGroup = FormGroup()

    fun reset() {
        this._formGroup.reset()
        this._formGroup.clearControls()
        this._newOffer.value = null
        if(!update && offerID == null) {
            this._buyer.value = true
            this._formGroup.addControl(_descriptionControl);
            this._formGroup.addControl(_priceControl);
        }
        if(update && offerID != null) {
            this.makeRequest(this.retrofitConfig.offerController.getOffer(offerID!!),{
                this._buyer.value = it.buyer.id == AuthenticationManager.currentUser.value!!.userID
                this._descriptionControl.updateValue(it.description)
                this._priceControl.updateValue(it.price.toString())
                this._searching.value = false
                _formGroup.clearControls()
                if(buyer.value) {
                    _formGroup.addControl(_descriptionControl);
                    _formGroup.addControl(_priceControl)
                }
                else
                    _formGroup.addControl(_offerStatus)
            },{this._searching.value = false})
        }
    }

    fun createOffer() {
        this._creatingOffer.value = true
        val requiredValue: BigDecimal = this._priceControl.currentValue.value!!.toBigDecimal()
        val createOffer: CreateOffer = CreateOffer(requiredValue,_descriptionControl.currentValue.value!!,productID!!);
        this.makeRequest(this.retrofitConfig.offerController.createOffer(createOffer),{
            this._newOffer.value = it
            this._creatingOffer.value = false
        },{_newOffer.value = null;
        this._creatingOffer.value = false})
    }
    fun updateOffer() {
        if(offerID != null) {
            this._creatingOffer.value = true
            if(buyer.value) {
                val updateOfferBuyer: UpdateOfferBuyer = UpdateOfferBuyer(offerID!!,_descriptionControl.currentValue.value!!,_priceControl.currentValue.value!!.toBigDecimal())
                this.makeRequest(this.retrofitConfig.offerController.updateOfferBuyer(updateOfferBuyer),{
                    this._newOffer.value = it
                    this._creatingOffer.value = false
                },{this._newOffer.value = null;this._creatingOffer.value = false})
            }
            else {
                val updateOfferSeller: UpdateOfferSeller = UpdateOfferSeller(offerID!!,productID!!,_offerStatus.currentValue.value!!);
                this.makeRequest(this.retrofitConfig.offerController.updateOfferSeller(updateOfferSeller),{
                    this._newOffer.value = it
                    this._creatingOffer.value = false
                },{this._newOffer.value = null
                this._creatingOffer.value = false})
            }
        }
    }

    val creatingOffer: StateFlow<Boolean> = _creatingOffer.asStateFlow()
    val priceControl: FormControl<String?> = _priceControl
    val descriptionControl: FormControl<String?> = _descriptionControl
    val offerStatus: FormControl<String?> = _offerStatus
    val formGroup: FormGroup = _formGroup
    val newOffer: StateFlow<Offer?> = _newOffer.asStateFlow()
    val buyer: StateFlow<Boolean> = _buyer.asStateFlow()
}