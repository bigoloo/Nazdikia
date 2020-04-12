package com.bigoloo.interview.cafe.nazdikia.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Ne(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)