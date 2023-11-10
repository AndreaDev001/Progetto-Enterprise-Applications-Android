package com.enterpriseapplications.viewmodel.create

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Review
import com.enterpriseapplications.model.create.CreateReview
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigInteger
import java.util.UUID

class CreateReviewViewModel(application: CustomApplication) : BaseViewModel(application)
{
    var userID: UUID? = null
    var update: Boolean = false
    private var _textControl: FormControl<String?> = FormControl("",Validators.required())
    private var _ratingControl: FormControl<String?> = FormControl("",Validators.required(),Validators.min(
        BigInteger.valueOf(0)),Validators.max(BigInteger.valueOf(11)))
    private var _formGroup: FormGroup = FormGroup(_textControl,_ratingControl)
    private var _createdReview: MutableStateFlow<Review?> = MutableStateFlow(null)

    fun reset() {
        this._formGroup.reset()
        this._createdReview.value = null
    }

    fun createReview() {
        val createReview: CreateReview = CreateReview(userID!!,_textControl.currentValue.value!!,_ratingControl.currentValue.value!!.toInt())
        this.makeRequest(this.retrofitConfig.reviewController.createReview(createReview),{
            this._createdReview.value = it
        },{this._createdReview.value = null})
    }

    val textControl: FormControl<String?> = _textControl
    val ratingControl: FormControl<String?> = _ratingControl
    val createdReview: StateFlow<Review?> = _createdReview.asStateFlow()
    val formGroup: FormGroup = _formGroup
}