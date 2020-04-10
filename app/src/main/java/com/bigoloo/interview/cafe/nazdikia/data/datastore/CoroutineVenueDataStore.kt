package com.bigoloo.interview.cafe.nazdikia.data.datastore

import com.bigoloo.interview.cafe.nazdikia.domain.datastore.VenueDataStore
import com.bigoloo.interview.cafe.nazdikia.models.Paginated
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo
import com.bigoloo.interview.cafe.nazdikia.models.Place
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow


class CoroutineVenueDataStore : VenueDataStore {
    private val requestedPaginationInfoChannel = ConflatedBroadcastChannel<PaginationInfo>()
    private val venueDataChannel = ConflatedBroadcastChannel<Paginated<Place>>()
    override fun setRequestedOffset(offset: Int, limit: Int, total: Int) {
        requestedPaginationInfoChannel.offer(PaginationInfo(offset, limit, total))
    }

    override fun getVenues(): Flow<Paginated<Place>> {
        return venueDataChannel.openSubscription().consumeAsFlow()
    }

    override fun getRequestedVenuePageInfo(): Flow<PaginationInfo> {
        return requestedPaginationInfoChannel.openSubscription().consumeAsFlow()
    }

    override fun setVenues(paginatedVenue: Paginated<Place>) {
        venueDataChannel.offer(paginatedVenue)
    }

}