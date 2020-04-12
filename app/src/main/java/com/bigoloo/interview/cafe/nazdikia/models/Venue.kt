package com.bigoloo.interview.cafe.nazdikia.models


import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
@Entity(tableName = "venue")
data class Venue(

    /*   @Relation(
           parentColumn = "id",
           entityColumn = "categoryId"
       )
       @Embedded
       @SerializedName("categories")
       val categories: List<Category>,*/

    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @Embedded(prefix = "location_")
    @SerializedName("location")
    val location: Location,
    @SerializedName("name")
    val name: String

) : Serializable
