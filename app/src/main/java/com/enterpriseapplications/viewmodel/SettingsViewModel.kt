package com.enterpriseapplications.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.create.UpdateUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class SettingsViewModel(val application: CustomApplication) : BaseViewModel(application) {

    var userID: UUID? = null;
    private var _nameControl: FormControl<String?> = FormControl(null,Validators.required())
    private var _surnameControl: FormControl<String?> = FormControl(null,Validators.required())
    private var _descriptionControl: FormControl<String?> = FormControl(null,Validators.required())
    private var _genderControl: FormControl<String?> = FormControl(null,Validators.required())
    private var _visibilityControl: FormControl<String?> = FormControl(null,Validators.required())
    private var _formGroup = FormGroup(_nameControl,_surnameControl,_descriptionControl,_genderControl,_visibilityControl)


    private var _genders: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _visibilities: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    init
    {
        this.initialize()
    }

    fun initialize() {
        this.makeRequest(this.retrofitConfig.userController.getGenders(),{
            this._genders.value = it
        })
        this.makeRequest(this.retrofitConfig.userController.getVisibilities(),{
            this._visibilities.value = it
        })
    }

    fun updateUser() {
        val updateUser: UpdateUser = UpdateUser(name = _nameControl.currentValue.value!!,surname = _surnameControl.currentValue.value!!, gender = _genderControl.currentValue.value!!,
        description = _descriptionControl.currentValue.value!!,visibility = _visibilityControl.currentValue.value!!)
        this.makeRequest(this.retrofitConfig.userController.updateUser(updateUser),{
            Log.d("TEST","SUCCESS")
        })
    }

    fun reset() {
        this._formGroup.reset()
    }


    val nameControl: FormControl<String?> = _nameControl
    val surnameControl: FormControl<String?> = _surnameControl
    val descriptionControl: FormControl<String?> = _descriptionControl
    val genderControl: FormControl<String?> = _genderControl
    val visibilityControl: FormControl<String?> = _visibilityControl
    val genders: StateFlow<List<String>> = _genders.asStateFlow()
    val visibilities: StateFlow<List<String>> = _visibilities.asStateFlow()
    val formGroup: FormGroup = _formGroup
}