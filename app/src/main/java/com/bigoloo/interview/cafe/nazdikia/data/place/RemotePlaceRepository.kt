package com.bigoloo.interview.cafe.nazdikia.data.place

import com.bigoloo.interview.cafe.nazdikia.domain.repository.PlaceRepository
import com.bigoloo.interview.cafe.nazdikia.models.Place

class RemotePlaceRepository :
    PlaceRepository {
    override fun getNearByPlaces(): List<Place> {
        TODO("Not yet implemented")
    }

}