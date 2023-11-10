package com.enterpriseapplications.viewmodel.create

import androidx.compose.runtime.collectAsState
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Ban
import com.enterpriseapplications.model.create.CreateBan
import com.enterpriseapplications.model.reports.MessageReport
import com.enterpriseapplications.model.reports.ProductReport
import com.enterpriseapplications.model.reports.Report
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class CreateBanViewModel(val application: CustomApplication) : BaseViewModel(application)
{
    var report: Report? = null;
    var update: Boolean = false;
    private var _reasons: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    private var _currentReport: MutableStateFlow<Report?> = MutableStateFlow(null)
    private var _currentProductReport: MutableStateFlow<ProductReport?> = MutableStateFlow(null);
    private var _currentMessageReport: MutableStateFlow<MessageReport?> = MutableStateFlow(null);

    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _reasonControl: FormControl<String?> =  FormControl("",Validators.required())
    private var _expirationControl: FormControl<String?> = FormControl("2030-11-10",Validators.required())
    private var _formGroup: FormGroup = FormGroup(_descriptionControl,_reasonControl)
    private var _createdBan: MutableStateFlow<Ban?> = MutableStateFlow(null)


    fun reset() {
        this._formGroup.reset()
        this._createdBan.value = null
        this.makeRequest(this.retrofitConfig.reportController.getReasons(),{
            this._reasons.value = it
        })
        if(report != null) {
            when(report!!.type) {
                "USER" -> {
                    this.makeRequest(this.retrofitConfig.reportController.getReport(report!!.id),{
                        this._currentReport.value = it
                    },{this._currentReport.value = null})
                };
                "PRODUCT" -> {
                    this.makeRequest(this.retrofitConfig.productReportController.getProductReport(this.report!!.id),{
                        this._currentProductReport.value = it
                    },{this._currentProductReport.value = null; })
                };
                "MESSAGE" -> {
                    this.makeRequest(this.retrofitConfig.messageReportController.getMessageReport(this.report!!.id),{
                        this._currentMessageReport.value = it
                    },{this._currentMessageReport.value = null; })
                };
            }
        }
    }

    fun createBan() {
        if(_formGroup.validate() && report != null) {
            val createBan: CreateBan = CreateBan(report!!.reported.id,_reasonControl.currentValue.value!!,_expirationControl.currentValue.value!!)
            this.makeRequest(this.retrofitConfig.banController.createBan(createBan),{
                this._createdBan.value = it
            },{this._createdBan.value = null})
        }
    }
    val descriptionControl: FormControl<String?> = _descriptionControl
    val reasonControl: FormControl<String?> = _reasonControl
    val currentReport: StateFlow<Report?> = _currentReport.asStateFlow()
    val currentProductReport: StateFlow<ProductReport?> = _currentProductReport.asStateFlow()
    val currentMessageReport: StateFlow<MessageReport?> = _currentMessageReport.asStateFlow()
    val createdBan: StateFlow<Ban?> = _createdBan.asStateFlow()
    val formGroup: FormGroup = _formGroup
    val reasons: StateFlow<List<String>> = _reasons.asStateFlow()

}