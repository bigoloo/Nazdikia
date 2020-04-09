package com.bigoloo.interview.cafe.nazdikia.data.location

import android.content.Context
import com.bigoloo.interview.cafe.nazdikia.domain.location.LocationTracker
import com.bigoloo.interview.cafe.nazdikia.models.Tracker
import kotlinx.coroutines.flow.Flow

class FusedLocationTracker(private val context: Context) :LocationTracker{
    override fun getTracker(): Flow<Tracker> {
        TODO("Not yet implemented")
    }
}