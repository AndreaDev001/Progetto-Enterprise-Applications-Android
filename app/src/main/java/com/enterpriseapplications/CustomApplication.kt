package com.enterpriseapplications

import android.app.Application
import android.content.Context
import com.enterpriseapplications.config.authentication.AuthenticationManager

class CustomApplication : Application()
{
    lateinit var authenticationManager: AuthenticationManager;

    fun getContext(): Context? {
        return this.applicationContext;
    }
    override fun onCreate() {
        super.onCreate()
        authenticationManager = AuthenticationManager(this)
    }
}