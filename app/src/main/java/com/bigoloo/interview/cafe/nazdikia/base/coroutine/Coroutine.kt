package com.bigoloo.interview.cafe.nazdikia.base.coroutine

import kotlinx.coroutines.CoroutineDispatcher


interface CoroutineDispatcherProvider {
    fun backgroundDispatcher(): CoroutineDispatcher
    fun uiDispatcher(): CoroutineDispatcher
}