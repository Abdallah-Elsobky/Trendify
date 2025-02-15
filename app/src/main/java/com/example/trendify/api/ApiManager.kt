package com.example.trendify.api

import android.util.Log
import com.example.trendify.api.interceptors.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

    companion object {
        private var retrofit: Retrofit? = null

        private fun initRetrofit(): Retrofit {
            if (retrofit == null) {

                val loggingInterceptor = HttpLoggingInterceptor { message ->
                    Log.e("API", message)
                }
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(AuthInterceptor())
                    .build()
                retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://newsapi.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

        fun webService(): WebServices {
            return initRetrofit().create(WebServices::class.java)
        }
    }
}