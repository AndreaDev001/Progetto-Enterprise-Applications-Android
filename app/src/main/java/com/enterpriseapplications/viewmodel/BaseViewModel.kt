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

abstract class BaseViewModel(application: CustomApplication) : ViewModel()
{
    protected var retrofitConfig: RetrofitConfig = RetrofitConfig(application,application.authenticationManager)

    fun<T> makeRequest(call: Call<T>, successCallback: (T) -> Unit, errorCallback: () -> Unit = {}) {

        CoroutineScope(Dispatchers.Default).launch {
            call.enqueue(object: Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if(response.isSuccessful)
                        successCallback(response.body()!!);
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    errorCallback()
                }
            })
        }
    }
}