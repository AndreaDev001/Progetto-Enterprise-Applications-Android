package com.enterpriseapplications.viewmodel.search

import android.util.Log
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
    private var _emailControl: FormControl<String?> = FormControl("",Validators.required())
    private var _nameControl: FormControl<String?> = FormControl("", Validators.required())
    private var _surnameControl: FormControl<String?> = FormControl("",Validators.required())
    private var _usernameControl: FormControl<String?> = FormControl("",Validators.required())
    private var _genderControl: FormControl<String?> = FormControl("",Validators.required())
    private var _descriptionControl: FormControl<String?> = FormControl("", Validators.required())
    private var _minRatingControl: FormControl<String?> = FormControl("",Validators.required())
    private var _maxRatingControl: FormControl<String?> = FormControl("",Validators.required())

    private var _genders: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _currentUsers: MutableStateFlow<List<UserDetails>> = MutableStateFlow(emptyList())
    private var _currentPage: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentTotalElements: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentTotalPages: MutableStateFlow<Int> = MutableStateFlow(0);

    init
    {
       this.makeRequest(this.retrofitConfig.userController.getGenders(),{
            this._genders.value = it
        });
       this.resetSearch();
    }

    fun updateCurrentUsers(page: Boolean) {
        /***this.makeRequest(this.retrofitConfig.userController.getUsers(_emailControl.currentValue.value,_usernameControl.currentValue.value,
            _nameControl.currentValue.value,_surnameControl.currentValue.value,genderControl.currentValue.value,_descriptionControl.currentValue.value,0,10,
            _currentPage.value,20),{
            if(it._embedded != null)
            {
                if(!page)
                    this._currentUsers.value = it._embedded.content
                else {

                }
            }
            else
                this._currentUsers.value = emptyList()
            this._currentPage.value = it.page.number
            this._currentTotalPages.value = it.page.totalPages
            this._currentTotalElements.value = it.page.totalElements
        })***/
    }
    fun resetSearch() {
        this.makeRequest(this.retrofitConfig.userController.getUsers(null,null,
            null,null,null,null,null,null,
            0,20),{
            this._currentUsers.value = it._embedded!!.content;
            this._currentPage.value = it.page.number
            this._currentTotalPages.value = it.page.totalPages
            this._currentTotalElements.value = it.page.totalElements
        })
    }

    fun updateCurrentPage() {
        if(this._currentPage.value + 1 >= this._currentTotalPages.value)
            return;
        this._currentPage.value = this._currentPage.value + 1;
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

    val genders: StateFlow<List<String>> = _genders.asStateFlow()
    val currentUsers: StateFlow<List<UserDetails>> = _currentUsers.asStateFlow()
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    val currentTotalElements: StateFlow<Int> = _currentTotalElements.asStateFlow()
    val currentTotalPages: StateFlow<Int> = _currentTotalPages.asStateFlow()
}