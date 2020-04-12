package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import android.util.Log
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.LocationDataStore
import com.bigoloo.interview.cafe.nazdikia.models.Tracker
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class SyncNearybyVenue(
    private val locationDataStore: LocationDataStore,
    private val fetchVenuesIfNeededUseCase: FetchVenuesIfNeededUseCase,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    private var handleTrackerJob: Job? = null
    private var currentTracker: Tracker? = null
    private lateinit var scope: CoroutineScope


    fun start() {
        scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
        scope.launch {
            locationDataStore.getTracker().collect {
                handleTrackerJob?.cancel()
                handleTrackerJob = launch {
                    handleUpdateLocation(it)
                }
            }
        }
    }


    private suspend fun handleUpdateLocation(tracker: Tracker) {
        Log.e("handleUpdateLocation", "called with $tracker")
        currentTracker = tracker
        when (tracker) {
            is Tracker.Available -> {
                fetchVenuesIfNeededUseCase.fetchVenues(
                    tracker = currentTracker as Tracker.Available
                )
            }
            Tracker.NotAvailable -> {
                //no-op
            }
        }
    }

    fun stop() {
        scope.cancel()
    }


}