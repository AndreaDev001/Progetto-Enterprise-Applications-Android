package com.enterpriseapplications.viewmodel.search

import android.util.Log
import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Product
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchProductsViewModel(val application: CustomApplication) : BaseViewModel(application) {

    private var _nameControl: FormControl<String?> = FormControl("", Validators.required())
    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _brandControl: FormControl<String?> = FormControl("",Validators.required())
    private var _conditionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _minPriceControl: FormControl<String?> = FormControl("",Validators.required())
    private var _maxPriceControl: FormControl<String?> = FormControl("",Validators.required())
    private var _minLikesControl: FormControl<String?> = FormControl("",Validators.required())
    private var _maxLikesControl: FormControl<String?> = FormControl("",Validators.required())

    private var _primaryCategoryControl: FormControl<String?> = FormControl("",Validators.required())
    private var _secondaryCategoryControl: FormControl<String?> = FormControl("",Validators.required())
    private var _tertiaryCategoryControl: FormControl<String?> = FormControl("",Validators.required())


    private var _currentProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private var _currentPage: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentTotalPages: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentTotalElements: MutableStateFlow<Int> = MutableStateFlow(0);

    fun resetSearch() {

    }

    val nameControl: FormControl<String?> = _nameControl
    val descriptionControl: FormControl<String?> = _descriptionControl
    val brandControl: FormControl<String?> = _brandControl
    val minPriceControl: FormControl<String?> = _minPriceControl
    val maxPriceControl: FormControl<String?> = _maxPriceControl
    val minLikesControl: FormControl<String?> = _minLikesControl
    val maxLikesControl: FormControl<String?> = _maxLikesControl
    val conditionControl: FormControl<String?> = _conditionControl

    val currentProducts: StateFlow<List<Product>> = _currentProducts.asStateFlow()
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    val currentTotalPages: StateFlow<Int> = _currentTotalPages.asStateFlow()
    val currentTotalElements: StateFlow<Int> = _currentTotalElements.asStateFlow()

    val primaryCategoryControl: FormControl<String?> = _primaryCategoryControl
    val secondaryCategoryControl: FormControl<String?> = _secondaryCategoryControl
    val tertiaryCategoryControl: FormControl<String?> = _tertiaryCategoryControl
}