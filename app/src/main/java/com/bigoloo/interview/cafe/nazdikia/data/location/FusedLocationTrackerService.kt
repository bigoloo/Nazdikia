package com.bigoloo.interview.cafe.nazdikia.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.HandlerThread
import android.util.Log
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
import kotlinx.coroutines.flow.zip
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class FusedLocationTrackerService(
    private val context: Context,
    private val locationDataStore: LocationDataStore,
    private val internetConnectivityDataStore: InternetConnectivityDataStore,
    private val locationPermissionStatusDataStore: LocationPermissionStatusDataStore,
    coroutineDispatcher: CoroutineDispatcher
) : CoroutineScope {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(coroutineDispatcher + job)
    override val coroutineContext: CoroutineContext
        get() = scope.coroutineContext
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest().let {
        it.interval = 10_000
        it.smallestDisplacement = 0f// TODO change it  for release
        it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        it.setFastestInterval(10_000)
    }

    fun start() {
        Log.e(
            "fuckyou",
            "FusedLocationTrackerService start"
        )
        launch {
            internetConnectivityDataStore.isConnected()
                .zip(locationPermissionStatusDataStore.getGrantedStatus()) { internetStatus, permissionStatus ->
                    Log.e(
                        "fuckyou",
                        "internet status internetStatus $internetStatus  permissionStatus $permissionStatus"
                    )
                    internetStatus// && permissionStatus
                    true
                }.collect {
                    Log.e("fuckyou", "FusedLocationTrackerService $it")
                    if (it) {
                        startTrackingLocation()
                    } else
                        stopTrackingLocation()
                }
        }
    }

    private var locationTrackerJob: Job? = null
    private fun stopTrackingLocation() {
        locationTrackerJob?.cancel()
    }

    private suspend fun startTrackingLocation() {
        locationTrackerJob?.cancel()
        locationTrackerJob = suspendCancellableCoroutine { continuation ->
            Log.e("fuckyou", "FusedLocationTrackerService start")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                locationDataStore.setTracker(Tracker.NotAvailable)

            }
            val handlerThread = HandlerThread("Location Handler Thread ${Random.nextInt()}").apply {
                start()
            }
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult?) {
                    super.onLocationResult(result)
                    result?.locations?.forEach {
                        Log.e("fuckyou", "FusedLocationTrackerService locations $it")
                        locationDataStore.setTracker(Tracker.Available(it.toLocation()))
                    }

                }
            }
            fusedLocationProviderClient
                .requestLocationUpdates(locationRequest, locationCallback, handlerThread.looper)


            continuation.invokeOnCancellation {
                Log.e("fuckyou", "FusedLocationTrackerService stop")
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    fun stop() {
        Log.e("fuckyou", "FusedLocationTrackerService service stop")
        job.cancel()
    }

}
