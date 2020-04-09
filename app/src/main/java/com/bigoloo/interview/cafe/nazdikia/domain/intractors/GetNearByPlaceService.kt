package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.domain.location.LocationTracker
import com.bigoloo.interview.cafe.nazdikia.models.Place
import com.bigoloo.interview.cafe.nazdikia.models.Tracker
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlin.coroutines.CoroutineContext

class GetNearByPlaceService(
    private val locationTracker: LocationTracker,
    private val getNearByPlaceUseCase: GetNearByPlaceUseCase,
    coroutineDispatcher: CoroutineDispatcher
) : CoroutineScope {

    private val placeChannel = ConflatedBroadcastChannel<List<Place>>()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(coroutineDispatcher + job)

    override val coroutineContext: CoroutineContext
        get() = scope.coroutineContext


    fun onCreate() {
        launch {
            locationTracker.getTracker().collect {
                updateLocation(it)
            }
        }
    }

    private suspend fun updateLocation(tracker: Tracker) {
        when (tracker) {
            is Tracker.Available -> {
                val placeList = getNearByPlaceUseCase.getPlaces(tracker.location)
                placeChannel.offer(placeList)
            }
            Tracker.NotAvailable -> {
            }
        }
    }

    fun getPlaces(): Flow<List<Place>> {
        return placeChannel.openSubscription().consumeAsFlow()
    }

    fun onStop() {
        scope.coroutineContext.cancelChildren()
    }


}