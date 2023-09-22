package com.enterpriseapplications.viewmodel.search

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.Validators

class SearchProductsViewModel : ViewModel() {

    private var _nameControl: FormControl<String?> = FormControl("", Validators.required())
    private var _descriptionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _brandControl: FormControl<String?> = FormControl("",Validators.required())
    private var _conditionControl: FormControl<String?> = FormControl("",Validators.required())
    private var _minPriceControl: FormControl<String?> = FormControl("",Validators.required())
    private var _maxPriceControl: FormControl<String?> = FormControl("",Validators.required())
    private var _minLikesControl: FormControl<String?> = FormControl("",Validators.required())
    private var _maxLikesControl: FormControl<String?> = FormControl("",Validators.required())


    val nameControl: FormControl<String?> = _nameControl
    val descriptionControl: FormControl<String?> = _descriptionControl
    val brandControl: FormControl<String?> = _brandControl
    val minPriceControl: FormControl<String?> = _minPriceControl
    val maxPriceControl: FormControl<String?> = _maxPriceControl
    val minLikesControl: FormControl<String?> = _minLikesControl
    val maxLikesControl: FormControl<String?> = _maxLikesControl
    val conditionControl: FormControl<String?> = _conditionControl
}