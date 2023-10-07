package com.enterpriseapplications.viewmodel.create

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.viewmodel.BaseViewModel
import java.util.UUID

class CreateReplyViewModel(val application: CustomApplication) : BaseViewModel(application)
{
    var reviewID: UUID? = null;
    var update: Boolean = false;

    private var _textControl: FormControl<String?> = FormControl("",Validators.required())
    private var _ratingControl: FormControl<String?> = FormControl("",Validators.required())
    private var _formGroup: FormGroup = FormGroup(_textControl,_ratingControl)

    val textControl: FormControl<String?> = _textControl
    val ratingControl: FormControl<String?> = _ratingControl
    val formGroup: FormGroup = _formGroup
}