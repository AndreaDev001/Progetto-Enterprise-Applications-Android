package com.enterpriseapplications.viewmodel

import androidx.lifecycle.ViewModel
import com.enterpriseapplications.config.Adapter
import com.enterpriseapplications.config.RetrofitConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseViewModel : ViewModel()
{
    protected var retrofitConfig: RetrofitConfig = RetrofitConfig()

    fun<T> makeRequest(adapter: Adapter<T>,successCallback: (T) -> Unit,errorCallback: () -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            adapter.process { t, throwable ->
                if(throwable == null && t != null) {
                    successCallback(t)
                }
                else
                {
                    errorCallback()
                }
            }
        }
    }
}