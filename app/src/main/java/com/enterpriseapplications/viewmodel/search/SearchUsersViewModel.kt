package com.enterpriseapplications.viewmodel.search

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.viewmodel.BaseViewModel

class SearchUsersViewModel(val application: CustomApplication): BaseViewModel(application)
{
    private var _nameControl: FormControl<String?> = FormControl("", Validators.required())
    private var _surnameControl: FormControl<String?> = FormControl("",Validators.required())
    private var _usernameControl: FormControl<String?> = FormControl("",Validators.required())
    private var _genderControl: FormControl<String?> = FormControl("",Validators.required())
    private var _descriptionControl: FormControl<String?> = FormControl("", Validators.required())

    val nameControl: FormControl<String?> = _nameControl
    val surnameControl: FormControl<String?> = _surnameControl
    val usernameControl: FormControl<String?> = _usernameControl
    val genderControl: FormControl<String?> = _genderControl
    val descriptionControl: FormControl<String?> =_descriptionControl
}