package com.bigoloo.interview.cafe.nazdikia.data.place

import com.bigoloo.interview.cafe.nazdikia.domain.repository.PlaceRepository
import com.bigoloo.interview.cafe.nazdikia.models.Place

class LocalPlaceRepository :
    PlaceRepository {
    override fun getNearByPlaces(): List<Place> {
        TODO("Not yet implemented")
    }

    fun setPlaces(placeList: List<Place>) {
        TODO("Not yet implemented")
    }

}