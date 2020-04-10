package com.bigoloo.interview.cafe.nazdikia.data.location

import com.bigoloo.interview.cafe.nazdikia.models.Location

class DataRepository {
    var lastLocation: Location? = null

    val locationDistanceThreshold: Double = 100.0

    var lastDataReceivedTimestamp: Long = 212L
    val receivedDataTimeThreshold: Long = 3 * 60 * 60 * 1000 // 3 hours
    val pageLimit = 40
}