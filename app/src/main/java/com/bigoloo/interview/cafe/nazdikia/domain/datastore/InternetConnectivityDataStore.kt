package com.bigoloo.interview.cafe.nazdikia.domain.datastore

import kotlinx.coroutines.flow.Flow

interface InternetConnectivityDataStore {
    fun setIsConnected(isConnected: Boolean)
    fun isConnected(): Flow<Boolean>
}