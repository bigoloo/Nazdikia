package com.bigoloo.interview.cafe.nazdikia.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Item(
    @SerializedName("venue")
    val venue: Venue
)