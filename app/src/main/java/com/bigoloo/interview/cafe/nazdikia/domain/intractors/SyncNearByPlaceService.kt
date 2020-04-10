package com.bigoloo.interview.cafe.nazdikia.domain.intractors

import com.bigoloo.interview.cafe.nazdikia.domain.datastore.LocationDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.VenueDataStore
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo
import com.bigoloo.interview.cafe.nazdikia.models.Tracker
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

class SyncNearByPlaceService(
    private val locationDataStore: LocationDataStore,
    private val venueDataStore: VenueDataStore,
    private val getNearbyVenueUseCase: GetNearbyVenueUseCase,
    private val loadVenueWithPagination: LocalVenueWithPagination,
    coroutineDispatcher: CoroutineDispatcher
) : CoroutineScope {

    private var handleUIRequest: Job? = null
    private var handleTrackerJob: Job? = null
    private var currentTracker: Tracker? = null
    private val job = SupervisorJob()
    private val scope = CoroutineScope(coroutineDispatcher + job)

    override val coroutineContext: CoroutineContext
        get() = scope.coroutineContext


    fun onCreate() {
        launch {
            locationDataStore.getTracker().collect {
                handleTrackerJob?.cancel()
                handleTrackerJob = launch {
                    handleUpdateLocation(it)
                }
            }
        }
        launch {
            venueDataStore.getRequestedVenuePageInfo().collect {
                handleUIRequest?.cancel()
                handleUIRequest = launch {
                    handleRequestedInfo(it)
                }
            }
        }
    }

    private suspend fun handleRequestedInfo(paginationInfo: PaginationInfo) {
        when (currentTracker) {
            is Tracker.Available -> {
                getNearbyVenueUseCase.getPlaceByPaginationInfo(
                    tracker = currentTracker as Tracker.Available,
                    paginationInfo = paginationInfo

                )

            }
            null, Tracker.NotAvailable -> {
                loadVenueWithPagination.execute(paginationInfo)
            }

        }
    }


    private suspend fun handleUpdateLocation(tracker: Tracker) {
        currentTracker = tracker
        when (tracker) {
            is Tracker.Available -> {
                getNearbyVenueUseCase.getPlaceByPaginationInfo(
                    tracker = currentTracker as Tracker.Available,
                    paginationInfo = null

                )
            }
            Tracker.NotAvailable -> {
                //no-op
            }
        }
    }

    fun onStop() {
        job.cancel()
    }


}