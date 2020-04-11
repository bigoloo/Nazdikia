package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.data.repository.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo

class FetchVenueWithPaginationUseCase(
    private val localPlaceRepository: LocalPlaceRepository,
    private val readFromLocalAndNotifyUseCase: ReadFromLocalAndNotifyUseCase,
    private val callRemoteVenueAndNotifyUseCase: CallRemoteVenueAndNotifyUseCase
) {

    suspend fun execute(paginationInfo: PaginationInfo, location: Location) {
        val savedCount = localPlaceRepository.getSavedVenueCount()
        if (paginationInfo.totalSize < savedCount) {
            if (paginationInfo.offset + paginationInfo.limit <= savedCount) {
                readFromLocalAndNotifyUseCase.execute(
                    paginationInfo
                )
            } else {
                callRemoteVenueAndNotifyUseCase.execute(paginationInfo, location)
            }
        } else {
            if (paginationInfo.totalSize == 0 && savedCount == 0) {
                callRemoteVenueAndNotifyUseCase.execute(
                    paginationInfo, location
                )
            } else
                readFromLocalAndNotifyUseCase.execute(
                    paginationInfo
                )
        }
    }
}