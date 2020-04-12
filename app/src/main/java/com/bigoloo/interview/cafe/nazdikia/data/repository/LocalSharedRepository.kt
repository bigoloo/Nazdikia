package com.bigoloo.interview.cafe.nazdikia.data.repository

import android.content.Context
import com.bigoloo.interview.cafe.nazdikia.data.Constants
import com.bigoloo.interview.cafe.nazdikia.domain.repository.SharedRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location

class LocalSharedRepository(context: Context) : SharedRepository {

    private val locationSharedPref = context.getSharedPreferences(
        Constants.SharedPrefKey.locationKey,
        Context.MODE_PRIVATE
    )

    private val lastRemoteCallTimestampSharedPref = context.getSharedPreferences(
        Constants.SharedPrefKey.lastRemoteCallKey,
        Context.MODE_PRIVATE
    )

    private val totalPageSharedPref = context.getSharedPreferences(
        Constants.SharedPrefKey.totalPageKey,
        Context.MODE_PRIVATE
    )

    override fun getLastLocation(): Location? {
        if (!locationSharedPref.getBoolean(isValidLocationKey, false)) return null
        return Location(
            lat = locationSharedPref.getString(latKey, "0")!!.toDouble(),
            lng = locationSharedPref.getString(lngKey, "0")!!.toDouble()

        )
    }

    override fun setLastLocation(location: Location) {
        with(locationSharedPref.edit()) {
            putString(latKey, location.lat.toString())
            putString(lngKey, location.lng.toString())
            putBoolean(isValidLocationKey, true)
            commit()
        }
    }

    override fun setLastDataReceivedTimestamp(timestamp: Long) {


        with(lastRemoteCallTimestampSharedPref.edit()) {
            putLong(lastRemoteCallTimestampKey, timestamp)
            commit()
        }
    }

    override fun getLastDataReceivedTimestamp(): Long {
        return lastRemoteCallTimestampSharedPref.getLong(lastRemoteCallTimestampKey, 0)
    }

    override fun setTotalPage(totalSize: Int) {
        with(totalPageSharedPref.edit()) {
            putInt(totalSizeKey, totalSize)
            commit()

        }
    }

    override fun getTotalPage(): Int {
        return totalPageSharedPref.getInt(totalSizeKey, -1)
    }

    private companion object {
        const val lastRemoteCallTimestampKey = "lastRemoteCallTimestamp"
        const val latKey = "lat"
        const val lngKey = "lng"
        const val totalSizeKey = "totalSize"
        const val isValidLocationKey = "isValidLocation"

    }
}