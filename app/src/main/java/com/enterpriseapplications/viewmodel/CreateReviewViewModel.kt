package com.enterpriseapplications.viewmodel

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators

class CreateReviewViewModel : BaseViewModel()
{
    var userID: Number? = null
    var update: Boolean = false
    private var _textControl: FormControl<String?> = FormControl("",Validators.required())
    private var _ratingControl: FormControl<String?> = FormControl("",Validators.required())
    private var _formGroup: FormGroup = FormGroup(_textControl,_ratingControl)

    val textControl: FormControl<String?> = _textControl
    val ratingControl: FormControl<String?> = _ratingControl
    val formGroup: FormGroup = _formGroup
}