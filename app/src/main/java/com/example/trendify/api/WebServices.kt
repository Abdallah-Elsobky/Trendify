package com.example.trendify.api

import com.example.trendify.api.model.newsResponse.NewsResponse
import com.example.trendify.api.model.sourcesResponse.SourcesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {

    @GET("v2/top-headlines/sources")
    fun getSources(
        @Query("category")
        categoryId: String?
    ): Call<SourcesResponse>

    @GET("v2/everything")
    fun getNews(
        @Query("sources") source: String = "abc-news"
    ): Call<NewsResponse>
}