package com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bigoloo.interview.cafe.nazdikia.base.coroutine.CoroutineDispatcherProvider
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.DataValidityDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.ReadLocalFirstOrCallRemoteUseCase
import com.bigoloo.interview.cafe.nazdikia.models.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class VenueViewModel(
    private val dataValidityDataStore: DataValidityDataStore,
    private val readLocalFirstOrCallRemote: ReadLocalFirstOrCallRemoteUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) :
    BaseViewModel(coroutineDispatcherProvider) {

    private val limit = 20

    init {
        launch {
            onBackground {
                dataValidityDataStore.getIsValidData().collect { isValid ->
                    if (!isValid) {
                        fetchVenues(true)
                    }

                }
            }
        }
    }

    val state = MutableLiveData<PaginatedLoadableData<Venue>>().apply {
        value = FirstPageNotLoaded
    }
    val selectedItemsLiveData = MutableLiveData<Venue>()

    private fun fetchVenues(isForce: Boolean = false) = launch {
        var nextResultArgument: PageInfo? = null
        if (isForce) {
            onUI {
                state.value = FirstPageLoading
            }
            nextResultArgument = PageInfo(0, limit, 0)
        } else {
            when (state.value) {
                FirstPageLoading, is PageLoading<*> -> return@launch
                FirstPageNotLoaded, is FirstPageFailed -> {
                    onUI {
                        state.value = FirstPageLoading
                    }
                    nextResultArgument = PageInfo(0, limit, 0)
                }
                is PageFailed<*> -> {
                    onUI {
                        (state.value as PageFailed).let {
                            state.value = PageLoading(it.result)
                            nextResultArgument = it.result.pageInfo
                        }
                    }

                }
                is PageLoaded<*> -> {
                    onUI {
                        (state.value!! as PageLoaded<Venue>).let {
                            state.value = PageLoading(it.result)
                            nextResultArgument = it.result.pageInfo
                        }
                    }
                }
            }

        }
        onBackground {
            runCatching {
                readLocalFirstOrCallRemote.execute(nextResultArgument!!)
            }.fold({
                when (state.value!!) {
                    FirstPageLoading -> {

                        onUI {
                            state.value = PageLoaded(
                                PageResult(
                                    it.pageInfo.copy(offset = it.data.size),
                                    it.data
                                )
                            )
                        }
                    }
                    is PageLoading -> {
                        val oldState = (state.value!! as PageLoading)
                        val newList = oldState.result.data.toMutableList() + it.data
                        onUI {
                            state.value = PageLoaded(
                                PageResult(
                                    PageInfo(
                                        offset = oldState.result.pageInfo.offset + it.data.size,
                                        limit = oldState.result.pageInfo.limit,
                                        totalSize = it.pageInfo.totalSize
                                    ),
                                    data = newList
                                )
                            )
                        }
                    }
                    FirstPageNotLoaded, is PageLoaded, is PageFailed, is FirstPageFailed -> {
                        //it is not possible to
                    }
                }

            }, {
                when (state.value!!) {

                    FirstPageLoading -> {
                        onUI {
                            state.value = FirstPageFailed(it)
                        }
                    }
                    is PageLoading -> {
                        val oldState = state.value as PageLoading
                        onUI {
                            state.value = PageFailed(oldState.result, it)
                        }
                    }
                    FirstPageNotLoaded, is FirstPageFailed, is PageLoaded, is PageFailed -> {
                        //notop
                    }
                }
            })
        }

    }

    fun selectedVenueItem(selectedVenue: Venue) {
        selectedItemsLiveData.value = selectedVenue
    }


    fun loadMore() {
        fetchVenues(false)
    }

}