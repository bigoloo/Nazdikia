package com.bigoloo.interview.cafe.nazdikia.domain.location

import com.bigoloo.interview.cafe.nazdikia.models.Tracker
import kotlinx.coroutines.flow.Flow

interface LocationTracker {
    //TODO add the connectivity awareness
    fun getTracker(): Flow<Tracker>
}