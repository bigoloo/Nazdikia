package com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.bigoloo.interview.cafe.nazdikia.base.coroutine.CoroutineDispatcherProvider
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.LocationPermissionStatusDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class MainActivityViewModel(
    private val locationPermissionStatusDataStore: LocationPermissionStatusDataStore,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel(),
    CoroutineScope {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(coroutineDispatcherProvider.mainDispatcher() + job)
    override val coroutineContext: CoroutineContext
        get() = scope.coroutineContext


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun permissionLocationStatus(isGranted: Boolean) {
        locationPermissionStatusDataStore.setPermissionGrantedStatus(isGranted)
    }
}