package com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.bigoloo.interview.cafe.nazdikia.base.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(coroutineDispatcherProvider: CoroutineDispatcherProvider) :
    ViewModel(),
    CoroutineScope {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(coroutineDispatcherProvider.mainDispatcher() + job)
    override val coroutineContext: CoroutineContext
        get() = scope.coroutineContext


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}