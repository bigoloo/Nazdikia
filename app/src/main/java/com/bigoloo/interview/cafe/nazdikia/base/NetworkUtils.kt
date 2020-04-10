package com.bigoloo.interview.cafe.nazdikia.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build


fun Context.isNetworkConnected(): Boolean {

    return (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let { cm ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm.activeNetwork != null
        } else {
            cm.activeNetworkInfo != null
        }
    } ?: false
}