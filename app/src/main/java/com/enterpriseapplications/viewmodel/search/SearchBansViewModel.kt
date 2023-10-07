package com.enterpriseapplications.viewmodel.search

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Ban
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
    private var _reason: FormControl<String?> = FormControl("", Validators.required())


    private var _currentBans: MutableStateFlow<List<Ban>> = MutableStateFlow(emptyList());
    private var _currentPage: MutableStateFlow<Int> = MutableStateFlow(0)
    private var _currentTotalPages: MutableStateFlow<Int> = MutableStateFlow(0)
    private var _currentTotalElements: MutableStateFlow<Int> = MutableStateFlow(0)

    fun resetSearch() {

    }

    val bannerEmail: FormControl<String?> = _bannerEmail
    val bannedEmail: FormControl<String?> = _bannedEmail
    val bannerUsername: FormControl<String?> = _bannerUsername
    val bannedUsername: FormControl<String?> = _bannedUsername
    val reason: FormControl<String?> = _reason

    val currentBans: StateFlow<List<Ban>> = _currentBans.asStateFlow()
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    val currentTotalPages: StateFlow<Int> = _currentTotalPages.asStateFlow()
    val currentTotalElements: StateFlow<Int> = _currentTotalElements.asStateFlow()
}