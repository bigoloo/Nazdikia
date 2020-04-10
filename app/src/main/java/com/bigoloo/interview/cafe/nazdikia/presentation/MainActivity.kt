package com.bigoloo.interview.cafe.nazdikia.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bigoloo.interview.cafe.nazdikia.R
import com.bigoloo.interview.cafe.nazdikia.base.coroutine.applicationDispatcherProvider
import com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(applicationDispatcherProvider.mainDispatcher() + job)

    private val applicationLifecycleObserver: ApplicationLifecycleObserver by inject()
    private val mainActivityViewModel: MainActivityViewModel by viewModel()
    private val gpsRequestCode = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(applicationLifecycleObserver)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    gpsRequestCode
                );
            } else {
                mainActivityViewModel.permissionLocationStatus(true)
            }

        }
    }

    override val coroutineContext: CoroutineContext
        get() = scope.coroutineContext


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == gpsRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mainActivityViewModel.permissionLocationStatus(true)
            } else {
                mainActivityViewModel.permissionLocationStatus(false)
            }
        }

    }
}