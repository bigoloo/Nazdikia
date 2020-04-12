package com.bigoloo.interview.cafe.nazdikia.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Response(

    @SerializedName("response")
    val response: ResponseX
)