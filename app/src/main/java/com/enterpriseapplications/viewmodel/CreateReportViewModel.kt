package com.enterpriseapplications.viewmodel

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators

class CreateReportViewModel : ViewModel() {

    var userID: Number? = null
    var productID: Number? = null
    var messageID: Number? = null
    var update: Boolean = false
    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _reasonControl: FormControl<String?> = FormControl("",Validators.required())
    private var _formGroup: FormGroup = FormGroup(_descriptionControl,_reasonControl)


    val descriptionControl: FormControl<String?> = _descriptionControl
    val reasonControl: FormControl<String?> =_reasonControl
    val formGroup: FormGroup = _formGroup
}