package com.bigoloo.interview.cafe.nazdikia.models


import java.io.Serializable

sealed class Tracker {
    object NotAvailable : Tracker()
    data class Available(val location: Location) : Tracker()
}


fun android.location.Location.toLocation(): Location {
    return Location(lat = latitude, lng = longitude)
}


data class PageInfo(val offset: Int, val limit: Int, val totalSize: Int) : Serializable {

}


sealed class ListViewItem {
    object Loading : ListViewItem()
    data class Data<T>(val data: T) : ListViewItem()
}

data class PageResult<T>(val pageInfo: PageInfo, val data: List<T>) : Serializable {
    val isLoadMoreNeeded = pageInfo.offset < pageInfo.totalSize
}


object UnAvailableLocationException : Exception("Location is not Available")

sealed class PaginatedLoadableData<out T>
object FirstPageNotLoaded : PaginatedLoadableData<Nothing>()
object FirstPageLoading : PaginatedLoadableData<Nothing>()
data class FirstPageFailed(val throwable: Throwable) : PaginatedLoadableData<Nothing>()
data class PageLoaded<T>(val result: PageResult<T>) : PaginatedLoadableData<T>()
data class PageFailed<T>(val result: PageResult<T>, val throwable: Throwable) :
    PaginatedLoadableData<T>()

data class PageLoading<T>(val result: PageResult<T>) : PaginatedLoadableData<T>()
