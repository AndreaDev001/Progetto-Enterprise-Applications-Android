package com.enterpriseapplications.config

import android.util.Log
import com.enterpriseapplications.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class Adapter<T>(private val call: Call<T>)
{
    fun run(responseHandler: (T?, Throwable?) -> Unit) {
        try
        {
            val response = call.execute()
            handleResponse(response,responseHandler)
        }
        catch (t: IOException) {
            responseHandler(null,t)
        }
    }
    fun process(responseHandler: (T?,Throwable?) -> Unit) {
        val callback = object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                handleResponse(response,responseHandler)
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                responseHandler(null,t)
            }

        }
        call.enqueue(callback)
    }
    fun handleResponse(response: Response<T>?,handler: (T?,Throwable?) -> Unit) {
        if(response?.isSuccessful == true) {
            handler(response.body(),null)
        }
        else
        {
            handler(null, response?.let { HttpException(it) })
        }
    }
}