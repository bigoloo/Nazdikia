package com.bigoloo.interview.cafe.nazdikia.data.repository

import com.bigoloo.interview.cafe.nazdikia.data.db.VenueDao
import com.bigoloo.interview.cafe.nazdikia.domain.repository.SharedRepository
import com.bigoloo.interview.cafe.nazdikia.models.PageInfo
import com.bigoloo.interview.cafe.nazdikia.models.PageResult
import com.bigoloo.interview.cafe.nazdikia.models.Venue

class LocalPlaceRepository(
    private val dao: VenueDao,
    private val sharedRepository: SharedRepository
) {

    suspend fun getSavedVenueCount(): Int {
        return dao.getVenueCount()
    }

    fun getNearbyVenues(
        pageInfo: PageInfo
    ): PageResult<Venue> {

        val savedVenueList = dao.getVenue(pageInfo.offset, pageInfo.limit)
        return PageResult(
            pageInfo.copy(totalSize = sharedRepository.getTotalPage()),
            savedVenueList
        )
    }

    suspend fun saveVenues(venueList: List<Venue>) {
        dao.insert(venueList)
    }

    suspend fun clearVenues() {
        dao.deleteAll()
    }

}