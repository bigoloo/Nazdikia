package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.data.location.DataRepository
import com.bigoloo.interview.cafe.nazdikia.data.place.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.data.place.RemotePlaceRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo

class CallRemoteAndSyncWithLocalUseCase(
    private val remotePlaceRepository: RemotePlaceRepository,
    private val localPlaceRepository: LocalPlaceRepository,
    private val dataRepository: DataRepository
) {
    suspend fun execute(
        paginationInfo: PaginationInfo,
        location: Location,
        eraseLocalData: Boolean
    ) {
        remotePlaceRepository.getNearByPlaces(paginationInfo, location).also {
            dataRepository.lastDataReceivedTimestamp = System.currentTimeMillis()
            if (eraseLocalData) {
                localPlaceRepository.clearPlaces()
            }
            localPlaceRepository.savePlaces(it)
        }
    }

}