package com.enterpriseapplications.viewmodel.search

import android.util.Log
import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Page
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
    private var _currentProductsPage: MutableStateFlow<Page> = MutableStateFlow(Page(20,0,0,0));
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
            0,100,this._currentProductsPage.value.number,20),{
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
            this._currentProductsPage.value = this._currentProductsPage.value.copy(size = it.page.size, totalElements = it.page.totalElements, totalPages = it.page.totalPages, number = it.page.number)
        })
    }

    fun updateCurrentPage() {
        if(this._currentProductsPage.value.number + 1 >= this._currentProductsPage.value.totalPages)
            return;
        this._currentProductsPage.value = this._currentProductsPage.value.copy(size = this._currentProductsPage.value.size,totalElements = this._currentProductsPage.value.totalElements,totalPages = this._currentProductsPage.value.totalPages, number = 0)
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
        this._currentProductsPage.value = this._currentProductsPage.value.copy(size = 20,0,0,0);
        this.updateCurrentProducts(false)
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
    val currentProductsPage: StateFlow<Page> = _currentProductsPage.asStateFlow()

    val primaryCategoryControl: FormControl<String?> = _primaryCategoryControl
    val secondaryCategoryControl: FormControl<String?> = _secondaryCategoryControl
    val tertiaryCategoryControl: FormControl<String?> = _tertiaryCategoryControl
}