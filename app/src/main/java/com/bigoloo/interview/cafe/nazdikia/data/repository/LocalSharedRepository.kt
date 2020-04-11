package com.bigoloo.interview.cafe.nazdikia.data.repository

import com.bigoloo.interview.cafe.nazdikia.domain.repository.SharedRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location

class LocalSharedRepository : SharedRepository {
    override fun getLastLocation(): Location? {
        TODO("Not yet implemented")
    }

    override fun setLastLocation(location: Location) {
        TODO("Not yet implemented")
    }

    override fun setLastDataReceivedTimestamp(timestamp: Long) {
        TODO("Not yet implemented")
    }

    override fun getLastDataReceivedTimestamp(): Long {
        TODO("Not yet implemented")
    }

}