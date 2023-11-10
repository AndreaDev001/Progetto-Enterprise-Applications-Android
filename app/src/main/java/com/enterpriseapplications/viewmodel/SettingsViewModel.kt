package com.enterpriseapplications.viewmodel

import android.net.Uri
import android.os.FileUtils
import android.util.Log
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import coil.memory.MemoryCache
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.FileHandler
import com.enterpriseapplications.config.RetrofitConfig
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.UserDetails
import com.enterpriseapplications.model.create.UpdateUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.util.UUID

class SettingsViewModel(val application: CustomApplication) : BaseViewModel(application) {

    private var _userDetails: MutableStateFlow<UserDetails?> = MutableStateFlow(null)
    private var _nameControl: FormControl<String?> = FormControl(null)
    private var _surnameControl: FormControl<String?> = FormControl(null)
    private var _descriptionControl: FormControl<String?> = FormControl(null)
    private var _genderControl: FormControl<String?> = FormControl(null)
    private var _visibilityControl: FormControl<String?> = FormControl(null)
    private var _formGroup = FormGroup(_nameControl,_surnameControl,_descriptionControl,_genderControl,_visibilityControl)

    private var _currentSelectedUri: MutableStateFlow<Uri?> = MutableStateFlow(null)
    private var _searching: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _genders: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _visibilities: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _successImages: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _successInfo: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var _defaultUserImage: Uri? = Uri.parse("http://${RetrofitConfig.resourceServerIpAddress}/api/v1/userImages/public/${AuthenticationManager.currentUser.value!!.userID}");


    init
    {
        this._currentSelectedUri.value = this._defaultUserImage;
        this.initialize()
    }

    fun initialize() {
        this._searching.value = true
        this.makeRequest(this.retrofitConfig.userController.getUserDetails(AuthenticationManager.currentUser.value!!.userID),{
            this._userDetails.value = it
            this._nameControl.updateValue(it.name)
            this._surnameControl.updateValue(it.surname)
            this._descriptionControl.updateValue(it.description)
            this._genderControl.updateValue(it.gender)
            this._visibilityControl.updateValue(it.visibility)
            this._searching.value = false
        })
        this.makeRequest(this.retrofitConfig.userController.getGenders(),{
            this._genders.value = it
        })
        this.makeRequest(this.retrofitConfig.userController.getVisibilities(),{
            this._visibilities.value = it
        })
    }

    @OptIn(ExperimentalCoilApi::class)
    fun updateUser() {
        if (_formGroup.validate()) {
            val updateUser: UpdateUser = UpdateUser(
                name = _nameControl.currentValue.value,
                surname = _surnameControl.currentValue.value,
                gender = _genderControl.currentValue.value,
                description = _descriptionControl.currentValue.value,
                visibility = _visibilityControl.currentValue.value
            )
            this.makeRequest(this.retrofitConfig.userController.updateUser(updateUser), {
                this._successInfo.value = true
                if (_currentSelectedUri.value != null && _currentSelectedUri.value != _defaultUserImage) {
                    val currentUri = this._currentSelectedUri.value!!
                    val value: String =
                        "http://${RetrofitConfig.resourceServerIpAddress}/api/v1/userImages/public/${AuthenticationManager.currentUser.value!!.userID}"
                    val image = FileHandler.getFile(application.applicationContext, currentUri);
                    val multiPartFile = MultipartBody.Part.createFormData(
                        "file",
                        image.name,
                        image.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )
                    this.makeRequest(
                        this.retrofitConfig.userImageController.updateUserImage(
                            multiPartFile
                        ), {
                            this._successImages.value = true
                            application.imageLoader.memoryCache?.remove(MemoryCache.Key(value))
                            application.imageLoader.diskCache?.remove(value)
                        }, { this._successImages.value = false })
                }
            })
        }
    }
    fun reset() {
        this._formGroup.reset()
        this._currentSelectedUri.value = null
    }

    fun updateCurrentSelectedUri(uri: Uri?) {
        this._currentSelectedUri.value = uri
    }

    fun isValid(): Boolean {
        return this._formGroup.validate() && this._currentSelectedUri.value != null;
    }

    val successImages: StateFlow<Boolean> = _successImages.asStateFlow()
    val successInfo: StateFlow<Boolean> = _successInfo.asStateFlow()
    val currentSelectedUri: StateFlow<Uri?> = _currentSelectedUri.asStateFlow()
    val nameControl: FormControl<String?> = _nameControl
    val surnameControl: FormControl<String?> = _surnameControl
    val descriptionControl: FormControl<String?> = _descriptionControl
    val genderControl: FormControl<String?> = _genderControl
    val visibilityControl: FormControl<String?> = _visibilityControl
    val genders: StateFlow<List<String>> = _genders.asStateFlow()
    val visibilities: StateFlow<List<String>> = _visibilities.asStateFlow()
    val formGroup: FormGroup = _formGroup
    val searching: StateFlow<Boolean> = _searching.asStateFlow()
}