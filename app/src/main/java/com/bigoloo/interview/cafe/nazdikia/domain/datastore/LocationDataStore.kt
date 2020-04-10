package com.bigoloo.interview.cafe.nazdikia.domain.datastore

import com.bigoloo.interview.cafe.nazdikia.models.Tracker
import kotlinx.coroutines.flow.Flow

interface LocationDataStore {
    fun setTracker(tracker: Tracker)
    fun getTracker(): Flow<Tracker>
}