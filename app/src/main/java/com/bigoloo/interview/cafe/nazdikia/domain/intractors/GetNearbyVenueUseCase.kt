package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.data.location.DataRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo
import com.bigoloo.interview.cafe.nazdikia.models.Tracker

class GetNearbyVenueUseCase(
    private val fetchVenueWithPagination: FetchVenueWithPagination,
    private val callRemoteVenueUseCase: CallRemoteVenueUseCase,
    private val dataRepository: DataRepository
) {
    suspend fun getPlaceByPaginationInfo(
        tracker: Tracker.Available,
        paginationInfo: PaginationInfo?
    ) {
        val lastLocation = dataRepository.lastLocation
        when {
            isLocationChanged(tracker.location, lastLocation) || isDataExpired() -> {
                val newPaginationInfo = PaginationInfo(
                    0,
                    dataRepository.pageLimit
                    , 0
                )
                callRemoteVenueUseCase.execute(newPaginationInfo, tracker.location)
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
        return newLocation.isInZone(lastLocation, dataRepository.locationDistanceThreshold)
    }

    private fun isDataExpired() =
        System.currentTimeMillis() >= (dataRepository.lastDataReceivedTimestamp
                + dataRepository.receivedDataTimeThreshold)
}
