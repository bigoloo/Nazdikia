package com.bigoloo.interview.cafe.nazdikia.domain.datastore

import kotlinx.coroutines.flow.Flow

interface DataValidityDataStore {
    fun setIsValidData(isValid: Boolean)
    fun getIsValidData(): Flow<Boolean>
}