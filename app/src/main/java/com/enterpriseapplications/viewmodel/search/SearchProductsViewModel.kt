package com.enterpriseapplications.viewmodel.search

import android.util.Log
import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.viewmodel.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchProductsViewModel(val application: CustomApplication) : BaseViewModel(application) {


    init
    {
        test()
    }

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
    
    
    fun test()
    {
        this.retrofitConfig.productController.getConditions().enqueue(object: Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                Log.d("CALLED", response.body().toString())
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                TODO("Not yet implemented")
            }

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

    val primaryCategoryControl: FormControl<String?> = _primaryCategoryControl
    val secondaryCategoryControl: FormControl<String?> = _secondaryCategoryControl
    val tertiaryCategoryControl: FormControl<String?> = _tertiaryCategoryControl
}