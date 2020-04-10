package com.bigoloo.interview.cafe.nazdikia.models


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
