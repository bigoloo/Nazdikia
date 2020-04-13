package com.bigoloo.interview.cafe.nazdikia.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.HandlerThread
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.InternetConnectivityDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.LocationDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.LocationPermissionStatusDataStore
import com.bigoloo.interview.cafe.nazdikia.models.Tracker
import com.bigoloo.interview.cafe.nazdikia.models.toLocation
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlin.random.Random

class FusedLocationTrackerService(
    private val context: Context,
    private val locationDataStore: LocationDataStore,
    private val internetConnectivityDataStore: InternetConnectivityDataStore,
    private val locationPermissionStatusDataStore: LocationPermissionStatusDataStore,
    private val coroutineDispatcher: CoroutineDispatcher
) {


    private lateinit var scope: CoroutineScope

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest().let {
        it.interval = 10_000
        it.smallestDisplacement = 100f// TODO change it  for release
        it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        it.setFastestInterval(10_000)
    }

    fun start() {

        scope = CoroutineScope(coroutineDispatcher + SupervisorJob())

        scope.launch {
            internetConnectivityDataStore.isConnected()
                .combine(locationPermissionStatusDataStore.getGrantedStatus()) { internetStatus, permissionStatus ->
                    internetStatus && permissionStatus
                }.collect {
                    if (it) {
                        locationTrackerJob?.cancel()
                        locationTrackerJob = startTrackingLocation()
                    } else
                        stopTrackingLocation()
                }
        }
    }

    private var locationTrackerJob: Job? = null
    private fun stopTrackingLocation() {
        locationTrackerJob?.cancel()
    }

    private suspend fun startTrackingLocation() = scope.launch {

        suspendCancellableCoroutine { continuation ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                locationDataStore.setTracker(Tracker.NotAvailable)

            }
            val handlerThread =
                HandlerThread("Location Handler Thread ${Random.nextInt()}").apply {
                    start()
                }
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult?) {
                    super.onLocationResult(result)
                    result?.locations?.forEach {
                        locationDataStore.setTracker(Tracker.Available(it.toLocation()))
                    }

                }
            }
            fusedLocationProviderClient
                .requestLocationUpdates(locationRequest, locationCallback, handlerThread.looper)


            continuation.invokeOnCancellation {

            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }

    }

    fun stop() {
        scope.cancel()
    }

}
