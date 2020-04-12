package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.data.Constants
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.DataValidityDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.repository.SharedRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PageInfo
import com.bigoloo.interview.cafe.nazdikia.models.Tracker

class FetchVenuesIfNeededUseCase(
    private val callRemote: CallRemoteAndSyncWithLocalUseCase,
    private val dataValidityDataStore: DataValidityDataStore,
    private val sharedRepository: SharedRepository
) {
    suspend fun fetchVenues(
        tracker: Tracker.Available
    ) {
        val lastLocation = sharedRepository.getLastLocation()
        sharedRepository.setLastLocation(tracker.location)
        when {
            isLocationChanged(tracker.location, lastLocation) || isDataExpired() -> {
                val newPaginationInfo = PageInfo(
                    0,
                    Constants.pageLimit
                    , 0
                )
                callRemote.execute(newPaginationInfo, tracker.location, true)
                dataValidityDataStore.setIsValidData(isValid = false)
            }
        }
    }


    private fun isLocationChanged(newLocation: Location, lastLocation: Location?): Boolean {
        if (lastLocation == null) return true
        return newLocation.isInZone(lastLocation, Constants.locationDistanceThreshold)
    }

    private fun isDataExpired() =
        System.currentTimeMillis() >= (sharedRepository.getLastDataReceivedTimestamp()
                + Constants.receivedDataTimeThreshold)
}
