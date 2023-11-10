package com.enterpriseapplications.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.enterpriseapplications.CustomApplication
import com.enterpriseapplications.config.RetrofitConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseViewModel(application: CustomApplication) : ViewModel() {
    protected var retrofitConfig: RetrofitConfig =
        RetrofitConfig(application, application.authenticationManager)

    fun <T> makeRequest(
        call: Call<T>,
        successCallback: (T) -> Unit,
        errorCallback: () -> Unit = {}
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful)
                    successCallback(response.body()!!)
                else
                    errorCallback()
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                errorCallback()
            }
        })
    }
    fun makeDeleteRequest(call: Call<Void>,successCallback: () -> Unit,errorCallback: () -> Unit = {}) {
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                successCallback()
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                errorCallback()
            }
        })
    }
}