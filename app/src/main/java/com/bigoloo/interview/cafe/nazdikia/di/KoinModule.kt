package com.bigoloo.interview.cafe.nazdikia.di

import com.bigoloo.interview.cafe.nazdikia.base.coroutine.applicationDispatcherProvider
import com.bigoloo.interview.cafe.nazdikia.data.connectivity.ConnectivityBroadCastReceiver
import com.bigoloo.interview.cafe.nazdikia.data.datastore.CoroutineInternetConnectivityDataStore
import com.bigoloo.interview.cafe.nazdikia.data.datastore.CoroutineLocationDataStore
import com.bigoloo.interview.cafe.nazdikia.data.datastore.LocationPermissionStatusDataStoreImp
import com.bigoloo.interview.cafe.nazdikia.data.location.FusedLocationTrackerService
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.InternetConnectivityDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.LocationDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.LocationPermissionStatusDataStore
import com.bigoloo.interview.cafe.nazdikia.presentation.ApplicationLifecycleObserver
import com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel.MainActivityViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {

    single<LocationPermissionStatusDataStore> { LocationPermissionStatusDataStoreImp() }
    single<LocationDataStore> { CoroutineLocationDataStore() }
    single<InternetConnectivityDataStore> { CoroutineInternetConnectivityDataStore() }
    single {
        FusedLocationTrackerService(
            get(),
            get(),
            get(),
            get(),
            applicationDispatcherProvider.backgroundDispatcher()
        )
    }
    single {
        ConnectivityBroadCastReceiver(
            get()
        )
    }
    single { ApplicationLifecycleObserver(get(), get(), get(), get()) }

    single { applicationDispatcherProvider }

    viewModel {
        MainActivityViewModel(get(), get())
    }

}