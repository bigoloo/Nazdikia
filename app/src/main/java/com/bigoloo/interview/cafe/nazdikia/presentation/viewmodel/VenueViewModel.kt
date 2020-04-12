package com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel

import com.bigoloo.interview.cafe.nazdikia.base.coroutine.CoroutineDispatcherProvider
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.DataValidityDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.ReadLocalFirstOrCallRemoteUseCase
import com.bigoloo.interview.cafe.nazdikia.models.PageInfo
import com.bigoloo.interview.cafe.nazdikia.models.Venue
import com.bigoloo.interview.cafe.nazdikia.presentation.base.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class VenueViewModel(
    private val dataValidityDataStore: DataValidityDataStore,
    private val readLocalFirstOrCallRemote: ReadLocalFirstOrCallRemoteUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) :
    BaseViewModel(coroutineDispatcherProvider) {

    data class ViewState(
        val pageInfo: PageInfo,
        val venueList: List<Venue>,
        val error: Throwable?,
        val isLoadMoreNeeded: Boolean
    )

    init {
        launch {
            onBackground {
                dataValidityDataStore.getIsValidData().collect {
                    loadMore()
                }
            }
        }
    }

    val currentState = SingleLiveEvent<ViewState>().apply {
        value = ViewState(
            PageInfo(0, 20, 0), emptyList(), null
            , isLoadMoreNeeded = false
        )
    }


    private fun fetchVenues() = launch {
        //TODO go to loading state
        onBackground {
            runCatching {
                readLocalFirstOrCallRemote.execute(currentState.value!!.pageInfo)
            }.fold({
                currentState.value?.let { lastState ->
                    val newVenueList: List<Venue> =
                        lastState.venueList.toMutableList() + it.data

                    val newState = ViewState(
                        pageInfo = PageInfo(
                            lastState.pageInfo.offset
                                    + it.data.size,
                            lastState.pageInfo.limit,
                            it.pageInfo.totalSize
                        ),
                        venueList = newVenueList,
                        error = null,
                        isLoadMoreNeeded = newVenueList.size !=
                                it.pageInfo.totalSize

                    )
                    onUI {
                        currentState.value = newState
                    }
                }
            }, {
                currentState.value?.let { lastState ->
                    val newState = ViewState(
                        pageInfo = lastState.pageInfo,
                        venueList = lastState.venueList,
                        error = it,
                        isLoadMoreNeeded = lastState.isLoadMoreNeeded


                    )
                    onUI {
                        currentState.value = newState
                    }
                }
            })
        }

    }

    //TODO Refactor
    private var fetchVenueJob: Job? = null
    fun loadMore() {
        fetchVenueJob?.let {
            if (it.isCompleted || it.isCancelled) {
                fetchVenueJob = fetchVenues()
            }
        } ?: run {
            fetchVenueJob = fetchVenues()
        }

    }

}