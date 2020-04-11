package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.data.applicationConstants
import com.bigoloo.interview.cafe.nazdikia.domain.repository.SharedRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo
import com.bigoloo.interview.cafe.nazdikia.models.Tracker

class GetNearbyVenueUseCase(
    private val fetchVenueWithPagination: FetchVenueWithPaginationUseCase,
    private val callRemoteVenueAndNotifyUseCase: CallRemoteVenueAndNotifyUseCase,
    private val sharedRepository: SharedRepository
) {
    suspend fun fetchVenues(
        tracker: Tracker.Available,
        paginationInfo: PaginationInfo?
    ) {
        val lastLocation = sharedRepository.getLastLocation()
        when {
            isLocationChanged(tracker.location, lastLocation) || isDataExpired() -> {
                val newPaginationInfo = PaginationInfo(
                    0,
                    applicationConstants.pageLimit
                    , 0
                )
                callRemoteVenueAndNotifyUseCase.execute(newPaginationInfo, tracker.location)
            }
            else -> {
                paginationInfo?.let {
                    fetchVenueWithPagination.execute(it, tracker.location)
                }

            }
        }
    }


    private fun isLocationChanged(newLocation: Location, lastLocation: Location?): Boolean {
        if (lastLocation == null) return true
        return newLocation.isInZone(lastLocation, applicationConstants.locationDistanceThreshold)
    }

    private fun isDataExpired() =
        System.currentTimeMillis() >= (sharedRepository.getLastDataReceivedTimestamp()
                + applicationConstants.receivedDataTimeThreshold)
}
