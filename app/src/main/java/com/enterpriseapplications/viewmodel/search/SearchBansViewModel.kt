package com.enterpriseapplications.viewmodel.search

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Ban
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchBansViewModel(val application: CustomApplication) : BaseViewModel(application)
{
    private var _bannerEmail: FormControl<String?> = FormControl(null)
    private var _bannedEmail: FormControl<String?> = FormControl(null)
    private var _bannerUsername: FormControl<String?> = FormControl(null)
    private var _bannedUsername: FormControl<String?> = FormControl(null)
    private var _description: FormControl<String?> = FormControl(null)
    private var _reason: FormControl<String?> = FormControl(null)
    private var _expired: FormControl<Boolean> = FormControl(false)
    private var _formGroup: FormGroup = FormGroup(_bannerEmail,_bannedEmail,_bannerUsername,_bannedUsername,_description,_reason,_expired)

    private var _reasons: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _currentBans: MutableStateFlow<List<Ban>> = MutableStateFlow(emptyList());
    private var _currentBansPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _currentBansSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);

    init
    {
        this.initialize()
    }

    fun initialize() {
        this.makeRequest(this.retrofitConfig.reportController.getReasons(),{
            this._reasons.value = it
        })
        this.resetSearch()
    }

    fun updateCurrentBans(page: Boolean) {
        this._currentBansSearching.value = !page;
        this.makeRequest(this.retrofitConfig.banController.getBans(_bannerEmail.currentValue.value,
            _bannedEmail.currentValue.value,_bannerUsername.currentValue.value,_bannedUsername.currentValue.value,_description.currentValue.value,_reason.currentValue.value,
            this._expired.currentValue.value,this._currentBansPage.value.number,20),{
            if(it._embedded != null)
            {
                if(!page)
                    this._currentBans.value = it._embedded.content
                else
                {
                    val mutableList: MutableList<Ban> = mutableListOf()
                    mutableList.addAll(this._currentBans.value)
                    mutableList.addAll(it._embedded.content)
                    this._currentBans.value = mutableList
                }
            }
            this._currentBansSearching.value = false;
            this._currentBansPage.value = this._currentBansPage.value.copy(size = it.page.size,totalElements = it.page.totalElements, totalPages = it.page.totalPages,number = it.page.number)
        })
    }

    fun updateCurrentPage() {
        if(this._currentBansPage.value.number + 1 >= this._currentBansPage.value.totalPages)
            return;
        this._currentBansPage.value = this._currentBansPage.value.copy(size = this._currentBansPage.value.size, totalElements = this._currentBansPage.value.totalElements,totalPages = this._currentBansPage.value.totalPages,number = this._currentBansPage.value.number + 1)
        this.updateCurrentBans(true);
    }
    fun resetSearch() {
        this._formGroup.reset()
        this.updateCurrentBans(false)
    }

    val bannerEmail: FormControl<String?> = _bannerEmail
    val bannedEmail: FormControl<String?> = _bannedEmail
    val bannerUsername: FormControl<String?> = _bannerUsername
    val bannedUsername: FormControl<String?> = _bannedUsername
    val description: FormControl<String?> = _description
    val reason: FormControl<String?> = _reason

    val reasons: StateFlow<List<String>> = _reasons.asStateFlow()
    val currentBans: StateFlow<List<Ban>> = _currentBans.asStateFlow()
    val currentBansPage: StateFlow<Page> = _currentBansPage.asStateFlow()
    val currentBansSearching: StateFlow<Boolean> = _currentBansSearching.asStateFlow()
}