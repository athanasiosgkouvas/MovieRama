package com.example.movierama.ui.data.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedUrl = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()
        val modifiedRequest = originalRequest.newBuilder()
            .url(modifiedUrl)
            .build()
        return chain.proceed(modifiedRequest)
    }
}
