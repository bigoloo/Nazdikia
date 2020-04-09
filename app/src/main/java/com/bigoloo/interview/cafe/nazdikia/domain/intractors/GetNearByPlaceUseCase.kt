package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.data.place.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.data.place.RemotePlaceRepository
import com.bigoloo.interview.cafe.nazdikia.domain.repository.DataRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.Place

class GetNearByPlaceUseCase(
    private val localPlaceRepository: LocalPlaceRepository,
    private val remotePlaceRepository: RemotePlaceRepository,
    private val dataRepository: DataRepository
) {

    suspend fun getPlaces(newLocation: Location): List<Place> {
        val lastLocation = dataRepository.getLastLocation()
        return if (isLocationChanged(newLocation, lastLocation)) {
            if (System.currentTimeMillis() >= dataRepository.getLastDataReceivedTimestamp()
                + dataRepository.getReceivedDataTimeThreshold()
            )
                callRemotePlaces()
            else
                localPlaceRepository.getNearByPlaces()
        } else {
            localPlaceRepository.getNearByPlaces()
        }
    }

    private fun callRemotePlaces(): List<Place> {
        return remotePlaceRepository.getNearByPlaces().also {
            localPlaceRepository.setPlaces(it)
        }
    }

    private fun isLocationChanged(newLocation: Location, lastLocation: Location): Boolean {
        return newLocation.isInZone(lastLocation, dataRepository.getLocationDistanceThreshold())
    }
}