package com.bigoloo.interview.cafe.nazdikia.data.datastore

import com.bigoloo.interview.cafe.nazdikia.domain.datastore.LocationPermissionStatusDataStore
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

class LocationPermissionStatusDataStoreImp : LocationPermissionStatusDataStore {
    private val channel = ConflatedBroadcastChannel<Boolean>(false)
    override fun setPermissionGrantedStatus(isGranted: Boolean) {
        channel.offer(isGranted)
    }

    override fun getGrantedStatus(): Flow<Boolean> {
        return channel.openSubscription().consumeAsFlow()
    }
}