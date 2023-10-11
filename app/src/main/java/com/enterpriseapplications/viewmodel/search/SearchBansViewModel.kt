package com.enterpriseapplications.viewmodel.search

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Ban
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchBansViewModel(val application: CustomApplication) : BaseViewModel(application)
{
    private var _bannerEmail: FormControl<String?> = FormControl("", Validators.required())
    private var _bannedEmail: FormControl<String?> = FormControl("", Validators.required())
    private var _bannerUsername: FormControl<String?> = FormControl("", Validators.required())
    private var _bannedUsername: FormControl<String?> = FormControl("", Validators.required())
    private var _description: FormControl<String?> = FormControl("",Validators.required())
    private var _reason: FormControl<String?> = FormControl("", Validators.required())
    private var _expired: FormControl<Boolean> = FormControl(false)

    private var _reasons: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _currentBans: MutableStateFlow<List<Ban>> = MutableStateFlow(emptyList());
    private var _currentPage: MutableStateFlow<Int> = MutableStateFlow(0)
    private var _currentTotalPages: MutableStateFlow<Int> = MutableStateFlow(0)
    private var _currentTotalElements: MutableStateFlow<Int> = MutableStateFlow(0)

    init
    {
        this.makeRequest(this.retrofitConfig.reportController.getReasons(),{
            this._reasons.value = it
        })
        this.resetSearch();
    }
    fun updateCurrentBans(page: Boolean) {
        this.makeRequest(this.retrofitConfig.banController.getBans(_bannerEmail.currentValue.value,
            _bannedEmail.currentValue.value,_bannerUsername.currentValue.value,_bannedUsername.currentValue.value,_description.currentValue.value,_reason.currentValue.value,
            this._expired.currentValue.value,this._currentPage.value,20),{
            if(it._embedded != null)
            {
                if(!page)
                    this._currentBans.value = it._embedded.content
                else
                {
                    val mutableList: MutableList<Ban> = mutableListOf();
                    mutableList.addAll(this.currentBans.value)
                    mutableList.addAll(it._embedded.content)
                    this._currentBans.value = mutableList
                }
            }
            else
                this._currentBans.value = emptyList()
            this._currentPage.value = it.page.number
            this._currentTotalPages.value = it.page.totalPages
            this._currentTotalElements.value = it.page.totalElements
        })
    }

    fun updateCurrentPage() {
        if(this._currentPage.value + 1 >= this._currentTotalPages.value)
            return;
        this._currentPage.value = this._currentPage.value + 1;
        this.updateCurrentBans(true);
    }
    fun resetSearch() {
        this.makeRequest(this.retrofitConfig.banController.getBans(null,
            null,null,null,null,null,
            null,0,20),{
            this._currentBans.value = it._embedded!!.content
            this._currentPage.value = it.page.number
            this._currentTotalPages.value = it.page.totalPages
            this._currentTotalElements.value = it.page.totalElements
        })
    }

    val bannerEmail: FormControl<String?> = _bannerEmail
    val bannedEmail: FormControl<String?> = _bannedEmail
    val bannerUsername: FormControl<String?> = _bannerUsername
    val bannedUsername: FormControl<String?> = _bannedUsername
    val description: FormControl<String?> = _description
    val reason: FormControl<String?> = _reason

    val reasons: StateFlow<List<String>> = _reasons.asStateFlow()
    val currentBans: StateFlow<List<Ban>> = _currentBans.asStateFlow()
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    val currentTotalPages: StateFlow<Int> = _currentTotalPages.asStateFlow()
    val currentTotalElements: StateFlow<Int> = _currentTotalElements.asStateFlow()
}