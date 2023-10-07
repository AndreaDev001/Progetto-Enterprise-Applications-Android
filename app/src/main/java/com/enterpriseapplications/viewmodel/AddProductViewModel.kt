package com.enterpriseapplications.viewmodel

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddProductViewModel(val application: CustomApplication) : BaseViewModel(application)
{
    private var _nameControl: FormControl<String?> = FormControl("",Validators.required())
    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _brandControl: FormControl<String?> = FormControl("",Validators.required())
    private var _conditionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _priceControl: FormControl<String?> = FormControl("",Validators.required())
    private var _minPriceControl: FormControl<String?> = FormControl("",Validators.required())

    private var _primaryCategoryControl: FormControl<String?> = FormControl("",Validators.required())
    private var _secondaryCategoryControl: FormControl<String?> = FormControl("",Validators.required())
    private var _tertiaryCategoryControl: FormControl<String?> = FormControl("",Validators.required())

    private var _formGroup: FormGroup = FormGroup(_nameControl,_descriptionControl,_brandControl,_conditionControl,_priceControl,_minPriceControl,_primaryCategoryControl,_secondaryCategoryControl,_tertiaryCategoryControl)

    private var _conditions: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _primaryCategories: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _secondaryCategories: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private var _tertiaryCategories: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())


    fun confirm()
    {

    }

    fun reset()
    {

    }

    val nameControl: FormControl<String?> = _nameControl
    val descriptionControl: FormControl<String?> = _descriptionControl
    val brandControl: FormControl<String?> = _brandControl
    val conditionControl: FormControl<String?> = _conditionControl
    val priceControl: FormControl<String?> = _priceControl
    val minPriceControl: FormControl<String?> = _minPriceControl

    val primaryCategoryControl: FormControl<String?> = _primaryCategoryControl
    val secondaryCategoryControl: FormControl<String?> = _secondaryCategoryControl
    val tertiaryCategoryControl: FormControl<String?> = _tertiaryCategoryControl
    val formGroup: FormGroup = _formGroup

    val conditions: StateFlow<List<String>> = _conditions.asStateFlow()
    val primaryCategories: StateFlow<List<String>> = _primaryCategories.asStateFlow()
    val secondaryCategories: StateFlow<List<String>> = _secondaryCategories.asStateFlow()
    val tertiaryCategories: StateFlow<List<String>> =_tertiaryCategories.asStateFlow()
}