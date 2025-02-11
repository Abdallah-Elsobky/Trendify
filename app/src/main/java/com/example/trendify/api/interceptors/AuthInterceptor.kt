package com.example.trendify.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    // 9fecbc97ffa84a7ab98daf02f9005798
    // c6c60e0a2e1646438105c1142fe437c7
    override fun intercept(chain: Interceptor.Chain): Response {
        val apiKey = "9fecbc97ffa84a7ab98daf02f9005798"
        val newRequest = chain.request().newBuilder()
        newRequest.addHeader("X-Api-Key", apiKey)
        return chain.proceed(newRequest.build())
    }
}