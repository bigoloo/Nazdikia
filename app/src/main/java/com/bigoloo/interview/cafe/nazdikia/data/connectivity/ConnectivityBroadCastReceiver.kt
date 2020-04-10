package com.bigoloo.interview.cafe.nazdikia.data.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.InternetConnectivityDataStore

class ConnectivityBroadCastReceiver(private val internetConnectivityDataStore: InternetConnectivityDataStore) :
    BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let { cm ->


            intent.action.takeIf { it == ConnectivityManager.CONNECTIVITY_ACTION }?.let {
                internetConnectivityDataStore.setIsConnected(cm.activeNetworkInfo != null)
            }


        }

    }


}