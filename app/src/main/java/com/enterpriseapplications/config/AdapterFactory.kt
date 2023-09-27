package com.enterpriseapplications.config

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class AdapterFactory constructor(): CallAdapter.Factory()
{
    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit
    ): CallAdapter<*, *>? {
        return returnType?.let {
            return try {
                val type = (it as ParameterizedType)
                if(type.rawType != Adapter::class.java)
                    null
                else
                {
                    val result = type.actualTypeArguments[0]
                    CallAdapter<Any>(type)
                }
            }
            catch (exception: Exception) {
                null
            }
        }
    }
}