package com.bigoloo.interview.cafe.nazdikia.domain.repository

import com.bigoloo.interview.cafe.nazdikia.models.Location

interface SharedRepository {
    fun getLastLocation(): Location?
    fun setLastLocation(location: Location)
    fun setLastDataReceivedTimestamp(timestamp: Long)
    fun getLastDataReceivedTimestamp(): Long
    fun setTotalPage(totalSize: Int)
    fun getTotalPage(): Int

}