package com.bigoloo.interview.cafe.nazdikia.data.datastore

import com.bigoloo.interview.cafe.nazdikia.domain.datastore.InternetConnectivityDataStore
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

class CoroutineInternetConnectivityDataStore : InternetConnectivityDataStore {
    private val channel = ConflatedBroadcastChannel<Boolean>()
    override fun setIsConnected(isConnected: Boolean) {
        channel.offer(isConnected)
    }

    override fun isConnected(): Flow<Boolean> {
        return channel.openSubscription().consumeAsFlow()
    }
}