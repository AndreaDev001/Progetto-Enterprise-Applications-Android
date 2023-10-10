package com.enterpriseapplications.viewmodel.search

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.reports.Report
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchReportsViewModel(val application: CustomApplication): BaseViewModel(application)
{
    private var _reporterEmail: FormControl<String?> = FormControl("",Validators.required())
    private var _reportedEmail: FormControl<String?> = FormControl("",Validators.required())
    private var _reporterUsername: FormControl<String?> = FormControl("",Validators.required())
    private var _reportedUsername: FormControl<String?> = FormControl("",Validators.required())
    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _reason: FormControl<String?> = FormControl("",Validators.required())
    private var _type: FormControl<String?> = FormControl("",Validators.required())

    private var _reasons: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _types: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    private var _currentReports: MutableStateFlow<List<Report>> = MutableStateFlow(emptyList())
    private var _currentPage: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentTotalPages: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentTotalElements: MutableStateFlow<Int> = MutableStateFlow(0);

    init
    {
        this.makeRequest(this.retrofitConfig.reportController.getReasons(),{
            this._reasons.value = it
        })
        this.makeRequest(this.retrofitConfig.reportController.getTypes(),{
            this._types.value = it
        })
        this.updateCurrentReports();
    }
    fun updateCurrentReports() {
        this.makeRequest(this.retrofitConfig.reportController.getReports(_reporterEmail.currentValue.value,
            _reportedEmail.currentValue.value,_reporterUsername.currentValue.value,_reportedUsername.currentValue.value,
            _descriptionControl.currentValue.value,_reason.currentValue.value,_type.currentValue.value,_currentPage.value,20),{
            this._currentReports.value = it._embedded.content
            this._currentPage.value = it.page.number
            this._currentTotalPages.value = it.page.totalPages
            this._currentTotalElements.value = it.page.totalElements
        })
    }
    fun resetSearch() {
        this.makeRequest(this.retrofitConfig.reportController.getReports(null,null,null,null,
            null,null,null,0,20),{
            this._currentReports.value = it._embedded.content
            this._currentPage.value = it.page.number
            this._currentTotalPages.value = it.page.totalPages
            this._currentTotalElements.value = it.page.totalElements
        })
    }

    val reporterEmail: FormControl<String?> = _reporterEmail
    val reportedEmail: FormControl<String?> = _reportedEmail
    val reporterUsername: FormControl<String?> = _reporterUsername
    val reportedUsername: FormControl<String?> = _reportedUsername
    val descriptionControl: FormControl<String?> = _descriptionControl
    val reason: FormControl<String?> = _reason
    val type: FormControl<String?> = _type

    val reasons: StateFlow<List<String>> = _reasons.asStateFlow();
    val types: StateFlow<List<String>> = _types.asStateFlow();
    val currentReports: StateFlow<List<Report>> = _currentReports.asStateFlow()
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow();
    val currentTotalElements: StateFlow<Int> = _currentTotalElements.asStateFlow()
    val currentTotalPages: StateFlow<Int> = _currentTotalPages.asStateFlow()
}