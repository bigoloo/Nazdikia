package com.bigoloo.interview.cafe.nazdikia.models


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

sealed class Tracker {
    object NotAvailable : Tracker()
    data class Available(val location: Location) : Tracker()
}

@Parcelize
data class Location(val lat: Double, val lng: Double) : Parcelable {
    fun isInZone(location: Location, thresholdInMeter: Double): Boolean {
        val result = FloatArray(2)
        android.location.Location.distanceBetween(lat, location.lat, lng, location.lng, result)

        return result[0] <= thresholdInMeter
    }
}

@Parcelize
data class Place(val title: String) : Parcelable


fun android.location.Location.toLocation(): Location {
    return Location(latitude, longitude)
}

data class Paginated<T>(val paginationInfo: PaginationInfo, val data: List<T>) : Serializable


data class PaginationInfo(val offset: Int, val limit: Int, val totalSize: Int) : Serializable

