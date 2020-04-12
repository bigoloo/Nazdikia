package com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.bigoloo.interview.cafe.nazdikia.base.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(private val coroutineDispatcherProvider: CoroutineDispatcherProvider) :
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

    fun onBackground(block: suspend () -> Unit) {
        launch {
            withContext(coroutineDispatcherProvider.backgroundDispatcher()) {
                block()
            }
        }
    }

    fun onUI(block: suspend () -> Unit) {
        launch {
            withContext(coroutineDispatcherProvider.mainDispatcher()) {
                block()
            }
        }
    }

}