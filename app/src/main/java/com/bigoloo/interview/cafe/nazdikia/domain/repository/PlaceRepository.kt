package com.bigoloo.interview.cafe.nazdikia.domain.repository

import com.bigoloo.interview.cafe.nazdikia.models.Place

interface PlaceRepository {
    fun getNearByPlaces(): List<Place>
}