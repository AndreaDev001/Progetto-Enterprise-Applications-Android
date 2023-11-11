package com.enterpriseapplications.form

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FormGroup(private vararg val controls: FormControl<*>)
{
    private var _valid: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _controlsList: MutableStateFlow<MutableList<FormControl<*>>> = MutableStateFlow(mutableListOf())

    init
    {
        _controlsList.value.addAll(controls)
        for(control in _controlsList.value)
            control.formGroup = this
    }

    fun reset() {
        _valid.value = false
        for(control in _controlsList.value)
            control.reset()
    }

    fun addControl(formControl: FormControl<*>) {
        val mutableList = _controlsList.value
        mutableList.addAll(this._controlsList.value)
        mutableList.add(formControl);
        formControl.formGroup = this
        this._controlsList.value = mutableList
    }

    fun clearControls() {
        for(control in _controlsList.value)
            control.formGroup = null
       _controlsList.value = mutableListOf()
    }
    fun validate(): Boolean {
        for(control in _controlsList.value) {
            if(!control.isValid()) {
                _valid.value = false
                return false
            }
        }
        _valid.value = true
        return true;
    }

    val valid: StateFlow<Boolean> = _valid.asStateFlow()
    val controlsList: StateFlow<MutableList<FormControl<*>>> = _controlsList.asStateFlow()

}