package com.enterpriseapplications.viewmodel.create

import androidx.compose.runtime.collectAsState
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.create.CreateBan
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
    private var _success: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _reasonControl: FormControl<String?> =  FormControl("",Validators.required())
    private var _expirationControl: FormControl<String?> = FormControl("",Validators.required())
    private var _formGroup: FormGroup = FormGroup(_descriptionControl,_reasonControl,_expirationControl)

    init
    {
        this.makeRequest(this.retrofitConfig.reportController.getReasons(),{
            this._reasons.value = it
        })
    }

    fun createBan() {
        val createBan: CreateBan = CreateBan(userID!!,_reasonControl.currentValue.value!!,_expirationControl.currentValue.value!!)
        this.makeRequest(this.retrofitConfig.banController.createBan(createBan),{
            this._success.value = true;
        })
    }

    val descriptionControl: FormControl<String?> = _descriptionControl
    val reasonControl: FormControl<String?> = _reasonControl
    val expirationControl: FormControl<String?> = _expirationControl
    val formGroup: FormGroup = _formGroup
    val reasons: StateFlow<List<String>> = _reasons.asStateFlow()

}