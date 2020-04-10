package com.bigoloo.interview.cafe.nazdikia.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bigoloo.interview.cafe.nazdikia.R
import com.bigoloo.interview.cafe.nazdikia.base.coroutine.applicationDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(applicationDispatcherProvider.mainDispatcher() + job)

    private val applicationLifecycleObserver: ApplicationLifecycleObserver by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(applicationLifecycleObserver)
    }

    override val coroutineContext: CoroutineContext
        get() = scope.coroutineContext


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}