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

    private var _conditions: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _primaryCategories: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _secondaryCategories: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _tertiaryCategories: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    private var _currentProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    private var _currentPage: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentTotalPages: MutableStateFlow<Int> = MutableStateFlow(0);
    private var _currentTotalElements: MutableStateFlow<Int> = MutableStateFlow(0);

    init
    {
        this.makeRequest(this.retrofitConfig.productController.getConditions(),{
            this._conditions.value = it
        })
        this.makeRequest(this.retrofitConfig.categoryController.getPrimaries(),{
            this._primaryCategories.value = it
        })
        this.resetSearch();
    }

    fun updateCurrentProducts(page: Boolean) {
        this.makeRequest(this.retrofitConfig.productController.getProducts(_primaryCategoryControl.currentValue.value,
            _secondaryCategoryControl.currentValue.value,_tertiaryCategoryControl.currentValue.value,
            _nameControl.currentValue.value,_descriptionControl.currentValue.value,_conditionControl.currentValue.value,
            0,100,this._currentPage.value,20),{
            if(it._embedded != null) {
                if(!page)
                    this._currentProducts.value = it._embedded.content;
                else
                {
                    val mutableList: MutableList<Product> = mutableListOf()
                    mutableList.addAll(this._currentProducts.value)
                    mutableList.addAll(it._embedded.content)
                    this._currentProducts.value = mutableList
                }
            }
            else
                this._currentProducts.value = emptyList();
            this._currentPage.value = it.page.number
            this._currentTotalPages.value = it.page.totalPages
            this._currentTotalElements.value = it.page.totalElements
        })
    }

    fun updateCurrentPage() {
        if(this._currentPage.value + 1 >= this._currentTotalPages.value)
            return;
        this.updateCurrentProducts(true)
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
            if(_primaryCategoryControl.currentValue.value != null) {
                this.makeRequest(this.retrofitConfig.categoryController.getTertiaries(
                    _primaryCategoryControl.currentValue.value!!,secondary),{
                    this._tertiaryCategories.value = it
                })
            }
        }
    }
    fun resetSearch() {
        this.makeRequest(this.retrofitConfig.productController.getProducts(null,null,null, null,null,null,
            null,null,0,20),{
            this._currentProducts.value = it._embedded!!.content
            this._currentPage.value = it.page.number
            this._currentTotalPages.value = it.page.totalPages
            this._currentTotalElements.value = it.page.totalElements
        })
    }

    val nameControl: FormControl<String?> = _nameControl
    val descriptionControl: FormControl<String?> = _descriptionControl
    val brandControl: FormControl<String?> = _brandControl
    val minPriceControl: FormControl<String?> = _minPriceControl
    val maxPriceControl: FormControl<String?> = _maxPriceControl
    val minLikesControl: FormControl<String?> = _minLikesControl
    val maxLikesControl: FormControl<String?> = _maxLikesControl
    val conditionControl: FormControl<String?> = _conditionControl

    val conditions: StateFlow<List<String>> = _conditions.asStateFlow()
    val primaryCategories: StateFlow<List<String>> = _primaryCategories.asStateFlow()
    val secondaryCategories: StateFlow<List<String>> = _secondaryCategories.asStateFlow()
    val tertiaryCategories: StateFlow<List<String>> = _tertiaryCategories.asStateFlow()

    val currentProducts: StateFlow<List<Product>> = _currentProducts.asStateFlow()
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    val currentTotalPages: StateFlow<Int> = _currentTotalPages.asStateFlow()
    val currentTotalElements: StateFlow<Int> = _currentTotalElements.asStateFlow()

    val primaryCategoryControl: FormControl<String?> = _primaryCategoryControl
    val secondaryCategoryControl: FormControl<String?> = _secondaryCategoryControl
    val tertiaryCategoryControl: FormControl<String?> = _tertiaryCategoryControl
}