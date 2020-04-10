package com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel

import com.bigoloo.interview.cafe.nazdikia.base.coroutine.CoroutineDispatcherProvider
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.VenueDataStore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class VenueViewModel(
    private val venueDataStore: VenueDataStore,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) :
    BaseViewModel(coroutineDispatcherProvider) {


    //TODO move them to state
    private val pageLimit = 50
    private var currentOffset = 0
    private var total = 0

    fun requestedOffset(offset: Int) {
        venueDataStore.setRequestedOffset(offset, pageLimit, total)
    }


    init {

        launch {
            venueDataStore.getVenues().collect {

            }
        }
    }
}