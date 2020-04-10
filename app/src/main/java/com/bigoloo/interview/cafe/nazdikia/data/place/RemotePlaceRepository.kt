package com.bigoloo.interview.cafe.nazdikia.data.place

import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo
import com.bigoloo.interview.cafe.nazdikia.models.Place

class RemotePlaceRepository :
    PlaceRepository {
    override fun getNearByPlaces(
        paginationInfo: PaginationInfo,
        location: Location
    ): List<Place> {
        TODO("Not yet implemented")
    }

}