package com.bigoloo.interview.cafe.nazdikia.models


import java.io.Serializable

sealed class Tracker {
    object NotAvailable : Tracker()
    data class Available(val location: Location) : Tracker()
}


fun android.location.Location.toLocation(): Location {
    return Location(lat = latitude, lng = longitude)
}

sealed class Paginated<T>(open val pageInfo: PageInfo) : Serializable {

    data class Loaded<T>(override val pageInfo: PageInfo, val data: List<T>) :
        Paginated<T>(pageInfo)

    data class Failed<T>(override val pageInfo: PageInfo, val throwable: Throwable?) :
        Paginated<T>(pageInfo)
}


data class PageInfo(val offset: Int, val limit: Int, val totalSize: Int) : Serializable


sealed class ListViewItem {
    object Loading : ListViewItem()
    data class Data<T>(val data: T) : ListViewItem()
}

data class PageResult<T>(val pageInfo: PageInfo, val data: List<T>)

sealed class LoadableData<T>

object NotLoaded : LoadableData<Nothing>()
object Loading : LoadableData<Nothing>()
data class Failed(val throwable: Throwable)
data class Loaded<T>(val data: T)


object UnAvailableLocationException : Exception("Location is not Available")
