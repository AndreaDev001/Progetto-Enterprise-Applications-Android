package com.enterpriseapplications.viewmodel.search

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Page
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
    private var _currentReportsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));

    init
    {
        this.makeRequest(this.retrofitConfig.reportController.getReasons(),{
            this._reasons.value = it
        })
        this.makeRequest(this.retrofitConfig.reportController.getTypes(),{
            this._types.value = it
        })
        this.resetSearch();
    }
    fun updateCurrentReports(page: Boolean) {
        this.makeRequest(this.retrofitConfig.reportController.getReports(_reporterEmail.currentValue.value,
            _reportedEmail.currentValue.value,_reporterUsername.currentValue.value,_reportedUsername.currentValue.value,
            _descriptionControl.currentValue.value,_reason.currentValue.value,_type.currentValue.value,_currentReportsPage.value.number,20),{
            if(it._embedded != null)
            {
                if(!page)
                    this._currentReports.value = it._embedded.content;
                else
                {
                    val mutableList: MutableList<Report> = mutableListOf()
                    mutableList.addAll(this._currentReports.value)
                    mutableList.addAll(it._embedded.content)
                    this._currentReports.value = mutableList
                }
            }
            this._currentReportsPage.value = this._currentReportsPage.value.copy(size = it.page.size, totalElements = it.page.totalElements,totalPages = it.page.totalPages,number = it.page.number)
        })
    }

    fun updateCurrentPage() {
        if(this._currentReportsPage.value.number + 1 >= this._currentReportsPage.value.totalPages)
            return;
        this._currentReportsPage.value = this._currentReportsPage.value.copy(size = this._currentReportsPage.value.size, totalElements = this._currentReportsPage.value.totalElements, totalPages = this._currentReportsPage.value.totalPages, number = 0)
        this.updateCurrentReports(true);
    }
    fun resetSearch() {
        this._currentReportsPage.value = this._currentReportsPage.value.copy(20,0,0,0)
        this.updateCurrentReports(false)
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
    val currentReportsPage: StateFlow<Page> = _currentReportsPage.asStateFlow()
}