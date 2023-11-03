package com.enterpriseapplications.viewmodel.search

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Page
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchUsersViewModel(val application: CustomApplication): BaseViewModel(application)
{
    private var _emailControl: FormControl<String?> = FormControl(null,Validators.required())
    private var _nameControl: FormControl<String?> = FormControl(null, Validators.required())
    private var _surnameControl: FormControl<String?> = FormControl(null,Validators.required())
    private var _usernameControl: FormControl<String?> = FormControl(null,Validators.required())
    private var _genderControl: FormControl<String?> = FormControl(null,Validators.required())
    private var _descriptionControl: FormControl<String?> = FormControl(null, Validators.required())
    private var _minRatingControl: FormControl<String?> = FormControl(null,Validators.required())
    private var _maxRatingControl: FormControl<String?> = FormControl(null,Validators.required())
    private var _formGroup: FormGroup = FormGroup(_emailControl,_nameControl,_surnameControl,_usernameControl,_genderControl,_descriptionControl,_minRatingControl,_maxRatingControl)

    private var _genders: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _currentUsers: MutableStateFlow<List<UserDetails>> = MutableStateFlow(emptyList())
    private var _currentUsersPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
    private var _currentUsersSearching: MutableStateFlow<Boolean> = MutableStateFlow(false);

    init
    {
        this.initialize()
    }

    fun initialize() {
        this.makeRequest(this.retrofitConfig.userController.getGenders(),{
            this._genders.value = it
        });
        this.resetSearch();
    }

    fun updateCurrentUsers(page: Boolean) {
        this._currentUsersSearching.value = !page;
        this.makeRequest(this.retrofitConfig.userController.getUsers(_emailControl.currentValue.value,_usernameControl.currentValue.value,
            _nameControl.currentValue.value,_surnameControl.currentValue.value,_genderControl.currentValue.value,_descriptionControl.currentValue.value,0,10,
            _currentUsersPage.value.number,20),{
            if(it._embedded != null)
            {
                if(!page)
                    this._currentUsers.value = it._embedded.content
                else
                {
                    val mutableList: MutableList<UserDetails> = mutableListOf()
                    mutableList.addAll(this._currentUsers.value)
                    mutableList.addAll(it._embedded.content)
                    this._currentUsers.value = mutableList
                }
            }
            this._currentUsersSearching.value = false;
            this._currentUsersPage.value = this._currentUsersPage.value.copy(size = it.page.size,totalElements = it.page.totalElements,totalPages = it.page.totalPages,number = it.page.number)
        })
    }
    fun resetSearch() {
        this._currentUsersPage.value = this._currentUsersPage.value.copy(size =20,totalElements = 0,totalPages = 0,number = 0)
        this._formGroup.reset()
        this.updateCurrentUsers(false)
    }

    fun updateCurrentPage() {
        if(this._currentUsersPage.value.number + 1 >= this._currentUsersPage.value.number)
            return;
        this._currentUsersPage.value = this._currentUsersPage.value.copy(size = this._currentUsersPage.value.size, totalElements = this._currentUsersPage.value.totalElements,totalPages = this._currentUsersPage.value.totalPages,number = this._currentUsersPage.value.number + 1)
        this.updateCurrentUsers(true);
    }

    val nameControl: FormControl<String?> = _nameControl
    val surnameControl: FormControl<String?> = _surnameControl
    val usernameControl: FormControl<String?> = _usernameControl
    val genderControl: FormControl<String?> = _genderControl
    val descriptionControl: FormControl<String?> =_descriptionControl
    val emailControl: FormControl<String?> = _emailControl
    val minRatingControl: FormControl<String?> = _minRatingControl
    val maxRatingControl: FormControl<String?> = _maxRatingControl
    val formGroup: FormGroup = _formGroup

    val genders: StateFlow<List<String>> = _genders.asStateFlow()
    val currentUsers: StateFlow<List<UserDetails>> = _currentUsers.asStateFlow()
    val currentUsersPage: StateFlow<Page> = _currentUsersPage.asStateFlow()
    val currentUsersSearching: StateFlow<Boolean> = _currentUsersSearching.asStateFlow()
}