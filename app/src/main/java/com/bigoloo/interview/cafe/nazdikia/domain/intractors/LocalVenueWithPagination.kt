package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.data.place.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo

class LocalVenueWithPagination(
    private val localPlaceRepository: LocalPlaceRepository,
    private val readFromLocalAndNotifyUseCase: ReadFromLocalAndNotifyUseCase
) {

    suspend fun execute(paginationInfo: PaginationInfo) {
        val savedCount = localPlaceRepository.getSavedVenueCount()
        if (paginationInfo.totalSize < savedCount) {
            if (paginationInfo.offset + paginationInfo.limit <= savedCount) {
                readFromLocalAndNotifyUseCase.execute(
                    paginationInfo
                )
            }
        } else {
            readFromLocalAndNotifyUseCase.execute(
                paginationInfo
            )
        }
    }
}