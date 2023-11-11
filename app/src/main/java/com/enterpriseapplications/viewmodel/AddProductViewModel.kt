package com.enterpriseapplications.viewmodel

import android.net.Uri
import android.os.FileUtils
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import coil.imageLoader
import coil.memory.MemoryCache
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.FileHandler
import com.enterpriseapplications.config.RetrofitConfig
import com.enterpriseapplications.config.authentication.AuthenticationManager
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.model.create.CreateProduct
import com.enterpriseapplications.model.create.UpdateUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.math.BigInteger
import java.util.UUID
import kotlin.streams.toList

class AddProductViewModel(val application: CustomApplication) : BaseViewModel(application)
{
    private var _nameControl: FormControl<String?> = FormControl("",Validators.required(),Validators.minLength(3),Validators.maxLength(20))
    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required(),Validators.minLength(10),Validators.maxLength(20))
    private var _brandControl: FormControl<String?> = FormControl("",Validators.required(),Validators.minLength(5),Validators.maxLength(20))
    private var _conditionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _visibilityControl: FormControl<String?> = FormControl("",Validators.required())
    private var _priceControl: FormControl<String?> = FormControl("",Validators.required(),Validators.min(BigInteger.valueOf(0)),Validators.max(BigInteger.valueOf(1000)))
    private var _minPriceControl: FormControl<String?> = FormControl("",Validators.required(),Validators.min(BigInteger.valueOf(0)),Validators.required())

    private var _primaryCategoryControl: FormControl<String?> = FormControl("",Validators.required())
    private var _secondaryCategoryControl: FormControl<String?> = FormControl("",Validators.required())
    private var _tertiaryCategoryControl: FormControl<String?> = FormControl("",Validators.required())
    private var _currentSelectedUris: MutableStateFlow<List<Uri?>> = MutableStateFlow(emptyList())

    private var _formGroup: FormGroup = FormGroup(_nameControl,_descriptionControl,_brandControl,_conditionControl,_priceControl,_minPriceControl,_primaryCategoryControl,_secondaryCategoryControl,_tertiaryCategoryControl)

    private var _conditions: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _visibilities: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _primaryCategories: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _secondaryCategories: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _tertiaryCategories: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    private var _createdProduct: MutableStateFlow<Product?> = MutableStateFlow(null)
    private var _isValid: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init
    {
        this.initialize()
    }

    private fun initialize() {
        this.makeRequest(this.retrofitConfig.productController.getConditions(),{
            this._conditions.value = it
        })
        this.makeRequest(this.retrofitConfig.productController.getVisibilities(),{
            this._visibilities.value = it
        })
        this.makeRequest(this.retrofitConfig.categoryController.getPrimaries(),{
            this._primaryCategories.value = it
        })
    }

    fun updateSecondaries() {
        val primary: String? = _primaryCategoryControl.currentValue.value;
        if(primary != null) {
            this.makeRequest(this.retrofitConfig.categoryController.getSecondaries(primary),{
                this._secondaryCategories.value = it
            })
        }
    }
    fun updateTertiaries() {
        val primary: String? = _primaryCategoryControl.currentValue.value;
        val secondary: String? = _secondaryCategoryControl.currentValue.value;
        if(primary != null && secondary != null) {
            this.makeRequest(this.retrofitConfig.categoryController.getTertiaries(primary,secondary),{
                this._tertiaryCategories.value = it
            })
        }
    }

    fun confirm()
    {
        val createProduct: CreateProduct = CreateProduct(_nameControl.currentValue.value!!,
        _descriptionControl.currentValue.value!!,_priceControl.currentValue.value!!.toBigDecimal(),_minPriceControl.currentValue.value!!.toBigDecimal(),_brandControl.currentValue.value!!,
        _conditionControl.currentValue.value!!,_visibilityControl.currentValue.value!!,_primaryCategoryControl.currentValue.value!!,_secondaryCategoryControl.currentValue.value!!,_tertiaryCategoryControl.currentValue.value!!)
        if(this._formGroup.validate()) {
            this.makeRequest(this.retrofitConfig.productController.createProduct(createProduct),{ product ->
                val result = product
                val files = this._currentSelectedUris.value.stream().map<MultipartBody.Part> {uri ->
                    val fileImage = FileHandler.getFile(application.applicationContext,uri!!)
                    return@map MultipartBody.Part.createFormData("files", fileImage.name, fileImage.asRequestBody("image/jpeg".toMediaTypeOrNull()))
                }.toList()
                this.makeRequest(this.retrofitConfig.productImageController.updateProductImages(product.id,files),{
                  this._createdProduct.value = result
                },{this._createdProduct.value = null})
            })
        }
    }

    fun updateCurrentSelectedUris(value: Uri?) {
        if(value != null) {
            val mutableList: MutableList<Uri?> = mutableListOf()
            mutableList.addAll(this._currentSelectedUris.value)
            mutableList.add(value)
            this._currentSelectedUris.value = mutableList
            this._isValid.value = this._currentSelectedUris.value.isNotEmpty() && this._formGroup.validate();
        }
    }

    fun isValid() {
        _isValid.value = this._currentSelectedUris.value.isNotEmpty() && this._formGroup.validate()
    }

    fun reset()
    {
        this._formGroup.reset()
        this._currentSelectedUris.value = emptyList()
        this._createdProduct.value = null
    }

    val nameControl: FormControl<String?> = _nameControl
    val descriptionControl: FormControl<String?> = _descriptionControl
    val brandControl: FormControl<String?> = _brandControl
    val conditionControl: FormControl<String?> = _conditionControl
    val visibilityControl: FormControl<String?> = _visibilityControl
    val priceControl: FormControl<String?> = _priceControl
    val minPriceControl: FormControl<String?> = _minPriceControl

    val primaryCategoryControl: FormControl<String?> = _primaryCategoryControl
    val secondaryCategoryControl: FormControl<String?> = _secondaryCategoryControl
    val tertiaryCategoryControl: FormControl<String?> = _tertiaryCategoryControl
    val formGroup: FormGroup = _formGroup

    val isValid: StateFlow<Boolean> = _isValid.asStateFlow()
    val createdProduct: StateFlow<Product?> = _createdProduct.asStateFlow()
    val conditions: StateFlow<List<String>> = _conditions.asStateFlow()
    val visibilities: StateFlow<List<String>> = _visibilities.asStateFlow()
    val primaryCategories: StateFlow<List<String>> = _primaryCategories.asStateFlow()
    val secondaryCategories: StateFlow<List<String>> = _secondaryCategories.asStateFlow()
    val tertiaryCategories: StateFlow<List<String>> =_tertiaryCategories.asStateFlow()
    val currentSelectedUris: StateFlow<List<Uri?>> = _currentSelectedUris.asStateFlow()
}