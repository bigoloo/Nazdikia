package com.bigoloo.interview.cafe.nazdikia.data.datastore

import com.bigoloo.interview.cafe.nazdikia.domain.datastore.DataValidityDataStore
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

class CoroutineDataValidityDataStore : DataValidityDataStore {
    private val channel = ConflatedBroadcastChannel<Boolean>(false)
    override fun setIsValidData(isValid: Boolean) {
        channel.offer(isValid)
    }

    override fun getIsValidData(): Flow<Boolean> {
        return channel.openSubscription().consumeAsFlow()
    }


}