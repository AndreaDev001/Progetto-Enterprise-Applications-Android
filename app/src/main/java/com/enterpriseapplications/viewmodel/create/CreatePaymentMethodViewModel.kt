package com.enterpriseapplications.viewmodel.create

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.PaymentMethod
import com.enterpriseapplications.model.create.CreatePaymentMethod
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreatePaymentMethodViewModel(val application: CustomApplication): BaseViewModel(application) {

    private var _currentPaymentMethodBrands: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _currentPaymentMethodBrandsSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _createdPaymentMethod: MutableStateFlow<PaymentMethod?> = MutableStateFlow(null);
    private var _creatingPaymentMethod: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var _holderNameControl: FormControl<String?> = FormControl("", Validators.required())
    private var _numberControl: FormControl<String?> = FormControl("",Validators.required(),Validators.minLength(15),Validators.maxLength(20))
    private var _brandControl: FormControl<String?> = FormControl("",Validators.required())
    private var _expirationControl: FormControl<String?> = FormControl("",Validators.required())
    private var _formGroup: FormGroup = FormGroup(_holderNameControl,_numberControl,_brandControl)


    init
    {
        this._currentPaymentMethodBrandsSearching.value = true
        this.makeRequest(this.retrofitConfig.paymentMethodController.getPaymentMethodBrands(),{
            this._currentPaymentMethodBrands.value = it
            this._currentPaymentMethodBrandsSearching.value = false
        },{this._currentPaymentMethodBrandsSearching.value = false})
    }

    fun createPaymentMethod() {
            this._creatingPaymentMethod.value = true
            val createPaymentMethod: CreatePaymentMethod = CreatePaymentMethod(_holderNameControl.currentValue.value!!,
            _numberControl.currentValue.value!!,_brandControl.currentValue.value!!,"2030-10-11")
            this.makeRequest(this.retrofitConfig.paymentMethodController.createPaymentMethod(createPaymentMethod),{
                this._createdPaymentMethod.value = it
                this._creatingPaymentMethod.value = false
            }, {this._createdPaymentMethod.value = null;this._creatingPaymentMethod.value = false})
    }

    fun reset() {
        this._formGroup.reset()
        this._createdPaymentMethod.value = null
    }


    val currentPaymentMethodsBrands: StateFlow<List<String>> = _currentPaymentMethodBrands.asStateFlow()
    val holderNameControl: FormControl<String?> = _holderNameControl
    val numberControl: FormControl<String?> = _numberControl
    val brandControl: FormControl<String?> = _brandControl
    val expirationControl: FormControl<String?> = _expirationControl;
    val createdPaymentMethod: StateFlow<PaymentMethod?> = _createdPaymentMethod.asStateFlow()
    val creatingPaymentMethod: StateFlow<Boolean> = _creatingPaymentMethod.asStateFlow()
    val formGroup: FormGroup = _formGroup
}