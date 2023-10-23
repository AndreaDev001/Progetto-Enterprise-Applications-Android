package com.enterpriseapplications.viewmodel.create

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.create.CreateReport
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
    private var _success: MutableStateFlow<Boolean> = MutableStateFlow(false);

    init
    {
        this.makeRequest(this.retrofitConfig.reportController.getReasons(),{
            this._reasons.value = it
        })
    }

    fun createReport() {
        val createReport: CreateReport = CreateReport(this._descriptionControl.currentValue.value!!,this._reasonControl.currentValue.value!!)
        if(this.productID != null) {
            this.makeRequest(this.retrofitConfig.productReportController.createProductReport(createReport,productID!!),{
                this._success.value = true;
            })
        }
        if(this.messageID != null) {
            this.makeRequest(this.retrofitConfig.messageReportController.createMessageReport(createReport,messageID!!),{
                this._success.value = true;
            })
        }
        if(this.userID != null) {
            this.makeRequest(this.retrofitConfig.reportController.createReport(createReport,userID!!),{
                this._success.value = true;
            })
        }
    }


    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _reasonControl: FormControl<String?> = FormControl("",Validators.required())
    private var _formGroup: FormGroup = FormGroup(_descriptionControl,_reasonControl)

    private var _reasons: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())


    val descriptionControl: FormControl<String?> = _descriptionControl
    val reasonControl: FormControl<String?> =_reasonControl
    val formGroup: FormGroup = _formGroup
    val reasons: StateFlow<List<String>> = _reasons.asStateFlow()
    val success: StateFlow<Boolean> = _success.asStateFlow()
}