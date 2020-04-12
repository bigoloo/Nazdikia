package com.bigoloo.interview.cafe.nazdikia.models


import androidx.annotation.Keep

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
@Entity(tableName = "category")
data class Category(
    @Embedded
    @SerializedName("icon")
    val icon: Icon,

    @PrimaryKey
    @SerializedName("id")
    val categoryId: String,


    @SerializedName("name")
    val name: String,

    @SerializedName("pluralName")
    val pluralName: String,


    @SerializedName("primary")
    val primary: Boolean,


    @SerializedName("shortName")
    val shortName: String
) : Serializable