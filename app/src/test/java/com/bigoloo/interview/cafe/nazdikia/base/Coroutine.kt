package com.bigoloo.interview.cafe.nazdikia.base

import com.bigoloo.interview.cafe.nazdikia.base.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher


fun createTestDispatcherProvider(coroutineDispatcher: CoroutineDispatcher): CoroutineDispatcherProvider =
    object : CoroutineDispatcherProvider {
        override fun backgroundDispatcher() = coroutineDispatcher
        override fun mainDispatcher() = coroutineDispatcher

    }