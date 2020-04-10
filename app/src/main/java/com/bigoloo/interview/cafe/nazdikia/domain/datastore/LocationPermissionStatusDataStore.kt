package com.bigoloo.interview.cafe.nazdikia.domain.datastore

import kotlinx.coroutines.flow.Flow

interface LocationPermissionStatusDataStore {

    fun setPermissionGrantedStatus(isGranted: Boolean)
    fun getGrantedStatus(): Flow<Boolean>
}