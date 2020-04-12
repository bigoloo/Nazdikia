package com.bigoloo.interview.cafe.nazdikia.data.repository

import com.bigoloo.interview.cafe.nazdikia.data.network.VenueApi
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PageInfo
import com.bigoloo.interview.cafe.nazdikia.models.PageResult
import com.bigoloo.interview.cafe.nazdikia.models.Venue

class RemotePlaceRepository(private val api: VenueApi) {
    suspend fun getNearbyVenues(
        pageInfo: PageInfo,
        location: Location
    ): PageResult<Venue> {
        val result = api.getVenue(
            pageInfo.offset,
            pageInfo.limit,
            location.latlng
        ).response

        return PageResult(
            PageInfo(
                offset = pageInfo.offset,
                limit = pageInfo.limit,
                totalSize = result.totalResults
            ),
            result.groups.flatMap { it ->
                it.items.map { it.venue }
            }
        )


    }
}