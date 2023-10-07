package com.enterpriseapplications.viewmodel.create

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class CreateReportViewModel(val application: CustomApplication) : BaseViewModel(application) {

    var userID: UUID? = null
    var productID: UUID? = null
    var messageID: UUID? = null
    var update: Boolean = false

    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _reasonControl: FormControl<String?> = FormControl("",Validators.required())
    private var _formGroup: FormGroup = FormGroup(_descriptionControl,_reasonControl)

    private var _reasons: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _types: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())


    val descriptionControl: FormControl<String?> = _descriptionControl
    val reasonControl: FormControl<String?> =_reasonControl
    val formGroup: FormGroup = _formGroup
    val reasons: StateFlow<List<String>> = _reasons.asStateFlow()
    val types: StateFlow<List<String>> = _types.asStateFlow()
}