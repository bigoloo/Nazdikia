package com.bigoloo.interview.cafe.nazdikia.domain.datastore

import com.bigoloo.interview.cafe.nazdikia.models.Paginated
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo
import com.bigoloo.interview.cafe.nazdikia.models.Place
import kotlinx.coroutines.flow.Flow


interface VenueDataStore {
    fun setRequestedOffset(offset: Int, limit: Int, total: Int)
    fun getVenues(): Flow<Paginated<Place>>
    fun getRequestedVenuePageInfo(): Flow<PaginationInfo>
    fun setVenues(paginatedVenue: Paginated<Place>)
}