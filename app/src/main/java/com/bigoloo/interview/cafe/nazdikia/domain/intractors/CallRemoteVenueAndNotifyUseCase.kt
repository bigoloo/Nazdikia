package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo

class CallRemoteVenueAndNotifyUseCase(
    private val readFromLocalAndNotifyUseCase: ReadFromLocalAndNotifyUseCase,
    private val callRemoteAndSyncWithLocalUseCase: CallRemoteAndSyncWithLocalUseCase
) {

    suspend fun execute(paginationInfo: PaginationInfo, location: Location) {
        runCatching {
            callRemoteAndSyncWithLocalUseCase.execute(
                paginationInfo, location, true
            )

            readFromLocalAndNotifyUseCase.execute(
                paginationInfo
            )
        }.onFailure {
            //TODO first Page error
        }
    }
}