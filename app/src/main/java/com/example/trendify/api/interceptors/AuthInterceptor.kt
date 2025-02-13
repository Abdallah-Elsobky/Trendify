package com.example.trendify.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    // 9fecbc97ffa84a7ab98daf02f9005798
    // c6c60e0a2e1646438105c1142fe437c7
    // 8ce4cd6d08814fe3bf10283d5d075d42
    // a0ac3d7ad41d4b97928b2eff2f6e48c4
    override fun intercept(chain: Interceptor.Chain): Response {
        val apiKey = "a0ac3d7ad41d4b97928b2eff2f6e48c4"
        val newRequest = chain.request().newBuilder()
        newRequest.addHeader("X-Api-Key", apiKey)
        return chain.proceed(newRequest.build())
    }
}