package com.enterpriseapplications.viewmodel.create

import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.form.FormControl
import com.enterpriseapplications.form.FormGroup
import com.enterpriseapplications.form.Validators
import com.enterpriseapplications.model.Reply
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
    private var _newReply: MutableStateFlow<Reply?> = MutableStateFlow(null)
    private var _formGroup: FormGroup = FormGroup(_textControl)

    fun reset() {
        this._textControl.updateValue(null);
        this._newReply.value = null
    }

    fun createReply() {
        val createReply: CreateReply = CreateReply(reviewID!!,_textControl.currentValue.value!!)
        this.makeRequest(this.retrofitConfig.replyController.createReply(createReply),{
            this._newReply.value = it
        },{this._newReply.value = null})
    }

    val textControl: FormControl<String?> = _textControl
    val formGroup: FormGroup = _formGroup
    val newReply: StateFlow<Reply?> = _newReply.asStateFlow()
}