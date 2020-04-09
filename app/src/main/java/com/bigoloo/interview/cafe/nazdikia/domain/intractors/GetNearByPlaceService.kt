package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.domain.location.LocationTracker
import com.bigoloo.interview.cafe.nazdikia.domain.repository.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.models.Tracker
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

class GetNearByPlaceService(
    private val locationTracker: LocationTracker,
    private val localPlaceRepository: LocalPlaceRepository,
    coroutineDispatcher: CoroutineDispatcher
) : CoroutineScope {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(coroutineDispatcher + job)

    override val coroutineContext: CoroutineContext
        get() = scope.coroutineContext


    fun onCreate(){
        launch {
            locationTracker.getTracker().collect {
                updateLocation(it)
            }
        }
    }

    private fun updateLocation(tracker: Tracker) {
        when (tracker) {
            is Tracker.Available -> {

            }
            Tracker.NotAvailable -> {
            }
        }
    }
    fun onStop(){
        scope.coroutineContext.cancelChildren()
    }




}