package com.bigoloo.interview.cafe.nazdikia.data.datastore

import com.bigoloo.interview.cafe.nazdikia.domain.datastore.LocationDataStore
import com.bigoloo.interview.cafe.nazdikia.models.Tracker
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow


class CoroutineLocationDataStore : LocationDataStore {
    private val channel = ConflatedBroadcastChannel<Tracker>()
    override fun setTracker(tracker: Tracker) {
        channel.offer(tracker)
    }

    override fun getTracker(): Flow<Tracker> {
        return channel.openSubscription().consumeAsFlow()
    }
}