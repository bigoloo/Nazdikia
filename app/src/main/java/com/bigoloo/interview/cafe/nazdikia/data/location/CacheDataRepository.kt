package com.bigoloo.interview.cafe.nazdikia.data.location

import com.bigoloo.interview.cafe.nazdikia.domain.repository.DataRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location

class CacheDataRepository : DataRepository {
    override fun getLastLocation(): Location {
        TODO("Not yet implemented")
    }

    override fun saveLocation(location: Location) {
        TODO("Not yet implemented")
    }

    override fun getLocationDistanceThreshold(): Double {
        return 100.0
    }

    override fun getLastDataReceivedTimestamp(): Long {
        return 212L
    }

    override fun setLastDataReceivedTimestamp(timestamp: Long) {

    }

    override fun getReceivedDataTimeThreshold(): Long {
        return 3 * 60 * 60 * 1000 // 3 hours
    }

}