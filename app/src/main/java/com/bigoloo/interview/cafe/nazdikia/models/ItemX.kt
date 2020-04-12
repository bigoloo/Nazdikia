package com.bigoloo.interview.cafe.nazdikia.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ItemX(
    @SerializedName("reasonName")
    val reasonName: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("type")
    val type: String
)