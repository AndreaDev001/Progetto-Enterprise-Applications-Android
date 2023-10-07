package com.enterpriseapplications.viewmodel.create

import androidx.compose.runtime.collectAsState
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class CreateBanViewModel(val application: CustomApplication) : BaseViewModel(application)
{
    var userID: UUID? = null;
    var update: Boolean = false;
    private var _reasons: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _reasonControl: FormControl<String?> =  FormControl("",Validators.required())
    private var _expirationControl: FormControl<String?> = FormControl("",Validators.required())
    private var _formGroup: FormGroup = FormGroup(_descriptionControl,_reasonControl,_expirationControl)

    val descriptionControl: FormControl<String?> = _descriptionControl
    val reasonControl: FormControl<String?> = _reasonControl
    val expirationControl: FormControl<String?> = _expirationControl
    val formGroup: FormGroup = _formGroup
    val reasons: StateFlow<List<String>> = _reasons.asStateFlow()

}