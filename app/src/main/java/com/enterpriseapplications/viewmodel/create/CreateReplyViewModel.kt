package com.enterpriseapplications.viewmodel.create

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.create.CreateReply
import com.enterpriseapplications.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class CreateReplyViewModel(val application: CustomApplication) : BaseViewModel(application)
{
    var reviewID: UUID? = null;
    var update: Boolean = false;

    private var _textControl: FormControl<String?> = FormControl("",Validators.required())
    private var _success: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _formGroup: FormGroup = FormGroup(_textControl)
    fun createReply() {
        val createReply: CreateReply = CreateReply(reviewID!!,_textControl.currentValue.value!!)
        this.makeRequest(this.retrofitConfig.replyController.createReply(createReply),{
            this._success.value = true;
        })
    }

    val textControl: FormControl<String?> = _textControl
    val formGroup: FormGroup = _formGroup
    val success: StateFlow<Boolean> = _success.asStateFlow()
}