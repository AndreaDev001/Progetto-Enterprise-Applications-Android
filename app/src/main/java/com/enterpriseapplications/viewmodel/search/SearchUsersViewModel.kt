package com.enterpriseapplications.viewmodel.search

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchUsersViewModel(val application: CustomApplication): BaseViewModel(application)
{
    private var _nameControl: FormControl<String?> = FormControl("", Validators.required())
    private var _surnameControl: FormControl<String?> = FormControl("",Validators.required())
    private var _usernameControl: FormControl<String?> = FormControl("",Validators.required())
    private var _genderControl: FormControl<String?> = FormControl("",Validators.required())
    private var _descriptionControl: FormControl<String?> = FormControl("", Validators.required())

    private var _currentUsers: MutableStateFlow<List<UserDetails>> = MutableStateFlow(emptyList())
    private var _currentPage: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentTotalElements: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentTotalPages: MutableStateFlow<Int> = MutableStateFlow(0);

    fun resetSearch() {

    }

    val nameControl: FormControl<String?> = _nameControl
    val surnameControl: FormControl<String?> = _surnameControl
    val usernameControl: FormControl<String?> = _usernameControl
    val genderControl: FormControl<String?> = _genderControl
    val descriptionControl: FormControl<String?> =_descriptionControl

    val currentUsers: StateFlow<List<UserDetails>> = _currentUsers.asStateFlow()
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    val currentTotalElements: StateFlow<Int> = _currentTotalElements.asStateFlow()
    val currentTotalPages: StateFlow<Int> = _currentTotalPages.asStateFlow()
}