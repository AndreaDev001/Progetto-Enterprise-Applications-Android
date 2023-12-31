package com.enterpriseapplications.config.authentication

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor(private val authenticationManager: AuthenticationManager): Interceptor
{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val value: Pair<TokenData?,AuthenticatedUser?> = Pair(AuthenticationManager.currentToken.value,AuthenticationManager.currentUser.value);
        val result: Pair<TokenData?,AuthenticatedUser?>? = authenticationManager.refreshToken(value)
        if(result != null) {
            val requiredAuthentication: String = "Bearer ${result.first!!.accessToken}"
            return chain.proceed(request.newBuilder().addHeader("Authorization",requiredAuthentication).build())
        }
        return chain.proceed(request);
    }
}