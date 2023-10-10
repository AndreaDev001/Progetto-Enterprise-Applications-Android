package com.enterpriseapplications.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.config.authentication.AuthenticationManager

val viewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>,extras: CreationExtras): T {
        val application = checkNotNull(extras[APPLICATION_KEY]) as CustomApplication
        if(BaseViewModel::class.java.isAssignableFrom(modelClass))
            return modelClass.getConstructor(CustomApplication::class.java).newInstance(application)
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}