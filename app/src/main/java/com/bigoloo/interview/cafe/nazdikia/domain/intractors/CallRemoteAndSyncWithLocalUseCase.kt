package com.bigoloo.interview.cafe.nazdikia.domain.intractors


import com.bigoloo.interview.cafe.nazdikia.data.repository.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.data.repository.RemotePlaceRepository
import com.bigoloo.interview.cafe.nazdikia.domain.repository.SharedRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PageInfo
import com.bigoloo.interview.cafe.nazdikia.models.PageResult
import com.bigoloo.interview.cafe.nazdikia.models.Venue

class CallRemoteAndSyncWithLocalUseCase(
    private val remotePlaceRepository: RemotePlaceRepository,
    private val localPlaceRepository: LocalPlaceRepository,
    private val sharedRepository: SharedRepository
) {
    suspend fun execute(
        pageInfo: PageInfo,
        location: Location,
        eraseLocalData: Boolean
    ): PageResult<Venue> {
        return remotePlaceRepository.getNearbyVenues(pageInfo, location).also {
            sharedRepository.setLastDataReceivedTimestamp(System.currentTimeMillis())
            if (eraseLocalData) {
                localPlaceRepository.clearVenues()
            }
            localPlaceRepository.saveVenues(it.data)
            sharedRepository.setTotalPage(it.pageInfo.totalSize)
        }

    }

}

