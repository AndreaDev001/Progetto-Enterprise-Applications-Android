package com.enterpriseapplications.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.config.authentication.AuthenticationManager

val viewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>,extras: CreationExtras): T {
        val application = checkNotNull(extras[APPLICATION_KEY]) as CustomApplication
        if(BaseViewModel::class.java.isAssignableFrom(CustomApplication::class.java))
            return modelClass.getConstructor(BaseViewModel::class.java).newInstance(application)
        throw Exception("Invalid base class");
    }
}