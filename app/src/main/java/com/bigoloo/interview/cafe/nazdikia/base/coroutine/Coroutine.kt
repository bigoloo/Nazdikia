package com.bigoloo.interview.cafe.nazdikia.base.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


interface CoroutineDispatcherProvider {
    fun backgroundDispatcher(): CoroutineDispatcher
    fun mainDispatcher(): CoroutineDispatcher
}


val applicationDispatcherProvider = object : CoroutineDispatcherProvider {
    override fun backgroundDispatcher() = Dispatchers.IO

    override fun mainDispatcher() = Dispatchers.Main


}