package com.bigoloo.interview.cafe.nazdikia.presentation

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bigoloo.interview.cafe.nazdikia.base.isNetworkConnected
import com.bigoloo.interview.cafe.nazdikia.data.connectivity.ConnectivityBroadCastReceiver
import com.bigoloo.interview.cafe.nazdikia.data.location.FusedLocationTrackerService
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.InternetConnectivityDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.SyncNearybyVenue

class ApplicationLifecycleObserver(
    private val context: Context,
    private val connectivityBroadCastReceiver: ConnectivityBroadCastReceiver,
    private val internetConnectivityDataStore: InternetConnectivityDataStore,
    private val locationTrackerService: FusedLocationTrackerService,
    private val syncNearybyVenue: SyncNearybyVenue
) : LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        syncNearybyVenue.start()
        locationTrackerService.start()
        checkConnectivityStatus()
        registerConnectivityBroadcast()


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        locationTrackerService.stop()
        syncNearybyVenue.stop()
        unregisterConnectivityBroadCast()

    }

    private fun checkConnectivityStatus() {
        internetConnectivityDataStore.setIsConnected(context.isNetworkConnected())
    }

    private fun registerConnectivityBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let {
                it.registerDefaultNetworkCallback(connectivityCallback)
            }
        } else {
            val intentFilter = IntentFilter()
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            context.registerReceiver(connectivityBroadCastReceiver, intentFilter)
        }

    }

    private fun unregisterConnectivityBroadCast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let {
                it.unregisterNetworkCallback(connectivityCallback)
            }
        } else
            context.unregisterReceiver(connectivityBroadCastReceiver)
    }

    private val connectivityCallback = object :
        ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            internetConnectivityDataStore.setIsConnected(true)
        }

        override fun onLost(network: Network?) {
            internetConnectivityDataStore.setIsConnected(false)
        }
    }

}

