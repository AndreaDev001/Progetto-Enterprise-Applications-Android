package com.enterpriseapplications.form

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Thread.State

class FormControl<T>(private var _initialValue: T, vararg val validators: Validator<T>)
{
    private var _currentValue: MutableStateFlow<T?> = MutableStateFlow(_initialValue)
    private var _valid: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _errors: MutableStateFlow<MutableSet<String>> = MutableStateFlow(mutableSetOf())
    private var _errorsText: MutableStateFlow<MutableSet<String>> = MutableStateFlow(mutableSetOf())
    private var _touched: MutableStateFlow<Boolean> = MutableStateFlow(false)


    init {
        isValid()
    }

    fun hasError(error: String): Boolean {
        return _errors.value.contains(error)
    }

    fun updateValue(value: T) {
        this._touched.value = true
        this._currentValue.value = value
        this._errors.value = mutableSetOf()
        this._errorsText.value = mutableSetOf()
        this.isValid()
    }
    fun reset() {
        this._touched.value = false
        this._currentValue.value = null
        this._errors.value = mutableSetOf()
        this._errorsText.value = mutableSetOf()
    }
    fun isValid(): Boolean {
        for(validator in validators) {
            if(!validator.validate(_currentValue.value)) {
                _errors.value.add(validator.errorName)
                _errorsText.value.add(validator.errorText)
                _valid.value = false
                return false
            }
        }
        _valid.value = true
        return true;
    }

    val currentValue: StateFlow<T?> = _currentValue.asStateFlow()
    val valid: StateFlow<Boolean> = _valid.asStateFlow()
    val touched: StateFlow<Boolean> = _touched.asStateFlow()
    val errors: StateFlow<MutableSet<String>> = _errors.asStateFlow()
    val errorsText: StateFlow<MutableSet<String>> = _errorsText.asStateFlow()
}