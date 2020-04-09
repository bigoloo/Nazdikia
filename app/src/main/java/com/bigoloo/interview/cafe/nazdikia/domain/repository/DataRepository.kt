package com.bigoloo.interview.cafe.nazdikia.domain.repository

import com.bigoloo.interview.cafe.nazdikia.models.Location

interface DataRepository {
    fun getLastLocation(): Location
    fun saveLocation(location: Location)
    fun getLocationDistanceThreshold(): Double
    fun getLastDataReceivedTimestamp(): Long
    fun setLastDataReceivedTimestamp(timestamp: Long)
    fun getReceivedDataTimeThreshold(): Long
}