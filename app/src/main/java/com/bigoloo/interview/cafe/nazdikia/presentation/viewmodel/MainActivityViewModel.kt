package com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel

import com.bigoloo.interview.cafe.nazdikia.base.coroutine.CoroutineDispatcherProvider
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.LocationPermissionStatusDataStore

class MainActivityViewModel(
    private val locationPermissionStatusDataStore: LocationPermissionStatusDataStore,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : BaseViewModel(coroutineDispatcherProvider) {

    fun permissionLocationStatus(isGranted: Boolean) {
        locationPermissionStatusDataStore.setPermissionGrantedStatus(isGranted)
    }
}