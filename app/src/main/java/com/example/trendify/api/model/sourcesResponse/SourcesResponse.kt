package com.example.trendify.api.model.sourcesResponse

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SourcesResponse(

    @field:SerializedName("sources")
    val sources: List<Source?>? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("message")
    val message: String? = null,
) : Parcelable