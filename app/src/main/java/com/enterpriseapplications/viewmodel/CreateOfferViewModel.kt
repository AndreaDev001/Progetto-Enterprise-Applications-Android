package com.enterpriseapplications.viewmodel

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators

class CreateOfferViewModel : BaseViewModel()
{
    var productID: Number? = null
    var update: Boolean = false
    private var _priceControl: FormControl<String?> = FormControl("", Validators.required())
    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _formGroup: FormGroup = FormGroup(_priceControl,_descriptionControl)


    val priceControl: FormControl<String?> = _priceControl
    val descriptionControl: FormControl<String?> = _descriptionControl
    val formGroup: FormGroup = _formGroup
}