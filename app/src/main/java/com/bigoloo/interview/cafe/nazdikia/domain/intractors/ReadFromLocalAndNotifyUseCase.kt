package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.data.repository.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.VenueDataStore
import com.bigoloo.interview.cafe.nazdikia.models.Paginated
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo


class ReadFromLocalAndNotifyUseCase(
    private val venueDataStore: VenueDataStore,
    private val localRepository: LocalPlaceRepository
) {

    suspend fun execute(paginationInfo: PaginationInfo) {
        localRepository.getNearByPlaces(paginationInfo).also {
            venueDataStore.setVenues(Paginated(paginationInfo, it))
        }
    }

}