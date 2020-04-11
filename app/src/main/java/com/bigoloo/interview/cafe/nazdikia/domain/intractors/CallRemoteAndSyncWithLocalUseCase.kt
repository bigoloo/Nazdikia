package com.bigoloo.interview.cafe.nazdikia.domain.intractors


import com.bigoloo.interview.cafe.nazdikia.data.repository.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.data.repository.RemotePlaceRepository
import com.bigoloo.interview.cafe.nazdikia.domain.repository.SharedRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo

class CallRemoteAndSyncWithLocalUseCase(
    private val remotePlaceRepository: RemotePlaceRepository,
    private val localPlaceRepository: LocalPlaceRepository,
    private val sharedRepository: SharedRepository
) {
    suspend fun execute(
        paginationInfo: PaginationInfo,
        location: Location,
        eraseLocalData: Boolean
    ) {
        remotePlaceRepository.getNearByPlaces(paginationInfo, location).also {
            sharedRepository.setLastDataReceivedTimestamp(System.currentTimeMillis())
            if (eraseLocalData) {
                localPlaceRepository.clearPlaces()
            }
            localPlaceRepository.savePlaces(it)
        }
    }

}