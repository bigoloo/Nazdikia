package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.data.repository.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.domain.repository.SharedRepository
import com.bigoloo.interview.cafe.nazdikia.models.PageInfo
import com.bigoloo.interview.cafe.nazdikia.models.PageResult
import com.bigoloo.interview.cafe.nazdikia.models.UnAvailableLocationException
import com.bigoloo.interview.cafe.nazdikia.models.Venue

class ReadLocalFirstOrCallRemoteUseCase(
    private val localPlaceRepository: LocalPlaceRepository,
    private val localRepository: LocalPlaceRepository,
    private val callRemote: CallRemoteAndSyncWithLocalUseCase,
    private val sharedRepository: SharedRepository
) {

    suspend fun execute(pageInfo: PageInfo): PageResult<Venue> {
        val savedCount = localPlaceRepository.getSavedVenueCount()
        val totalPage = sharedRepository.getTotalPage()
        val lastLocation = sharedRepository.getLastLocation()


        return if (isRemoteCallNeeded(
                pageInfo.offset,
                pageInfo.limit,
                totalPage,
                savedCount
            )
        ) {
            lastLocation?.let {
                callRemote.execute(pageInfo, it, false)
            } ?: throw UnAvailableLocationException
        } else {
            localRepository.getNearbyVenues(
                pageInfo
            )
        }

    }

    private fun isRemoteCallNeeded(
        offset: Int,
        limit: Int,
        totalPage: Int,
        savedCount: Int
    ): Boolean {

        return when {
            totalPage == -1 -> true
            totalPage > savedCount -> offset + limit > savedCount
            totalPage == savedCount -> false
            else -> false
        }

    }
}