package com.enterpriseapplications.viewmodel.create

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Ban
import com.enterpriseapplications.model.create.CreateReport
import com.enterpriseapplications.model.reports.MessageReport
import com.enterpriseapplications.model.reports.ProductReport
import com.enterpriseapplications.model.reports.Report
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
    private var _creatingReport: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _createdProductReport: MutableStateFlow<ProductReport?> = MutableStateFlow(null)
    private var _createdMessageReport: MutableStateFlow<MessageReport?> = MutableStateFlow(null);
    private var _createdUserReport: MutableStateFlow<Report?> = MutableStateFlow(null)

    init
    {
        this.makeRequest(this.retrofitConfig.reportController.getReasons(),{
            this._reasons.value = it
        })
    }

    fun reset() {
        this._formGroup.reset()
        this._createdMessageReport.value = null
        this._createdProductReport.value = null
        this._createdUserReport.value = null
    }

    fun createReport() {
        this._creatingReport.value = true
        val createReport: CreateReport = CreateReport(this._descriptionControl.currentValue.value!!,this._reasonControl.currentValue.value!!)
        if(this.productID != null) {
            this.makeRequest(this.retrofitConfig.productReportController.createProductReport(createReport,productID!!),{
                this._createdProductReport.value = it
                this._creatingReport.value = false
            },{
                this._creatingReport.value = false
                this._createdUserReport.value = null
            })
        }
        if(this.messageID != null) {
            this.makeRequest(this.retrofitConfig.messageReportController.createMessageReport(createReport,messageID!!),{
                this._createdMessageReport.value = it
                this._creatingReport.value = false
            },{
                this._creatingReport.value = false
                this._createdMessageReport.value = null
            })
        }
        if(this.userID != null) {
            this.makeRequest(this.retrofitConfig.reportController.createReport(createReport,userID!!),{
                this._createdUserReport.value = it
                this._creatingReport.value = false
            },{this._createdUserReport.value = null
            this._creatingReport.value = false})
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
    val createdUserReport: StateFlow<Report?> = _createdUserReport.asStateFlow()
    val createdMessageReport: StateFlow<MessageReport?> = _createdMessageReport.asStateFlow()
    val createdProductReport: StateFlow<ProductReport?> = _createdProductReport.asStateFlow()
    val creatingReport: StateFlow<Boolean> = _creatingReport.asStateFlow()
}