package com.enterpriseapplications.viewmodel.search

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.Validators

class SearchReportsViewModel : ViewModel() {
    private var _reporterEmail: FormControl<String?> = FormControl("",Validators.required())
    private var _reportedEmail: FormControl<String?> = FormControl("",Validators.required())
    private var _reporterUsername: FormControl<String?> = FormControl("",Validators.required())
    private var _reportedUsername: FormControl<String?> = FormControl("",Validators.required())
    private var _reason: FormControl<String?> = FormControl("",Validators.required())
    private var _type: FormControl<String?> = FormControl("",Validators.required())


    val reporterEmail: FormControl<String?> = _reporterEmail
    val reportedEmail: FormControl<String?> = _reportedEmail
    val reporterUsername: FormControl<String?> = _reporterUsername
    val reportedUsername: FormControl<String?> = _reportedUsername
    val reason: FormControl<String?> = _reason
    val type: FormControl<String?> = _type
}