package com.enterpriseapplications.viewmodel.search

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.viewmodel.BaseViewModel

class SearchBansViewModel(val application: CustomApplication) : BaseViewModel(application)
{
    private var _bannerEmail: FormControl<String?> = FormControl("", Validators.required())
    private var _bannedEmail: FormControl<String?> = FormControl("", Validators.required())
    private var _bannerUsername: FormControl<String?> = FormControl("", Validators.required())
    private var _bannedUsername: FormControl<String?> = FormControl("", Validators.required())
    private var _reason: FormControl<String?> = FormControl("", Validators.required())

    val bannerEmail: FormControl<String?> = _bannerEmail
    val bannedEmail: FormControl<String?> = _bannedEmail
    val bannerUsername: FormControl<String?> = _bannerUsername
    val bannedUsername: FormControl<String?> = _bannedUsername
    val reason: FormControl<String?> = _reason
}