package com.bigoloo.interview.cafe.nazdikia.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Icon(
    @SerializedName("prefix")
    val prefix: String,
    @SerializedName("suffix")
    val suffix: String
)