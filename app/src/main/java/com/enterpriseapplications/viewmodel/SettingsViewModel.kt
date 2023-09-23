package com.enterpriseapplications.viewmodel

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.Validators

class SettingsViewModel : ViewModel() {

    private var _nameControl: FormControl<String?> = FormControl("",Validators.required())
    private var _surnameControl: FormControl<String?> = FormControl("",Validators.required())
    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _genderControl: FormControl<String?> = FormControl("",Validators.required())

    val nameControl: FormControl<String?> = _nameControl
    val surnameControl: FormControl<String?> = _surnameControl
    val descriptionControl: FormControl<String?> = _descriptionControl
    val genderControl: FormControl<String?> = _genderControl
}