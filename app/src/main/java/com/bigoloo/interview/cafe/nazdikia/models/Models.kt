package com.bigoloo.interview.cafe.nazdikia.models

import android.location.Location

sealed class Tracker {
    object NotAvailable:Tracker()
    data class Available(val location: Location):Tracker()
}


data class Place(val title: String)