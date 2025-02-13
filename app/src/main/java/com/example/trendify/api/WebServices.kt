package com.example.trendify.api

import com.example.trendify.api.model.newsResponse.NewsResponse
import com.example.trendify.api.model.sourcesResponse.SourcesResponse
import org.intellij.lang.annotations.Language
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

interface WebServices {

    @GET("v2/top-headlines/sources")
    fun getSources(
        @Query("category")
        categoryId: String?,
//        @Query("language")
//        language: String = "en"
    ): Call<SourcesResponse>

    @GET("v2/everything")
    fun getNews(
        @Query("sources")
        source: String = "abc-news",
        @Query("q")
        query: String?,
    ): Call<NewsResponse>
}