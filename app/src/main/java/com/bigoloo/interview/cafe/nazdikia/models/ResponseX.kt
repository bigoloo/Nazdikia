package com.bigoloo.interview.cafe.nazdikia.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResponseX(
    @SerializedName("groups")
    val groups: List<Group>,
    @SerializedName("totalResults")
    val totalResults: Int

)