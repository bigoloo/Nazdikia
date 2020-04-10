package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.data.place.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo

class FetchVenueWithPagination(
    private val localPlaceRepository: LocalPlaceRepository,
    private val readFromLocalAndNotifyUseCase: ReadFromLocalAndNotifyUseCase,
    private val callRemoteVenueUseCase: CallRemoteVenueUseCase
) {

    suspend fun execute(paginationInfo: PaginationInfo, location: Location) {
        val savedCount = localPlaceRepository.getSavedVenueCount()
        if (paginationInfo.totalSize < savedCount) {
            if (paginationInfo.offset + paginationInfo.limit <= savedCount) {
                readFromLocalAndNotifyUseCase.execute(
                    paginationInfo
                )
            } else {
                callRemoteVenueUseCase.execute(paginationInfo, location)
            }
        } else {
            readFromLocalAndNotifyUseCase.execute(
                paginationInfo
            )
        }
    }
}