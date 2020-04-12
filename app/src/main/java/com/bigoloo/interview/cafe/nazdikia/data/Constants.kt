package com.bigoloo.interview.cafe.nazdikia.data

object Constants {

    const val locationDistanceThreshold: Double = 100.0
    const val receivedDataTimeThreshold: Long = 3 * 60 * 60 * 1000 // 3 hours
    const val pageLimit = 20

    object SharedPrefKey {
        const val totalPageKey = "total_page_key"
        const val locationKey = "location_key"
        const val lastRemoteCallKey = "last_remote_call"
    }
}