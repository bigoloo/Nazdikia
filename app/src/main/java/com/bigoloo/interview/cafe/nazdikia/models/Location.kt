package com.bigoloo.interview.cafe.nazdikia.models


import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

@Keep
@Entity
data class Location(
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("cc")
    val cc: String? = null,
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("crossStreet")
    val crossStreet: String? = null,
    @SerializedName("distance")
    val distance: Int? = null,

    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("postalCode")
    val postalCode: String? = null,
    @SerializedName("state")
    val state: String? = null
) {
    fun isInZone(location: Location, thresholdInMeter: Double): Boolean {
        val result = FloatArray(2)
        android.location.Location.distanceBetween(lat, location.lat, lng, location.lng, result)

        return result[0] <= thresholdInMeter
    }

    @Ignore
    val isValidLocation = (lat == lng) && lat != -1.0

    @Ignore
    val latlng: String = "${lat},${lng}"
}