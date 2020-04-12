package com.bigoloo.interview.cafe.nazdikia.di

import androidx.room.Room
import com.bigoloo.interview.cafe.nazdikia.base.coroutine.CoroutineDispatcherProvider
import com.bigoloo.interview.cafe.nazdikia.base.coroutine.applicationDispatcherProvider
import com.bigoloo.interview.cafe.nazdikia.data.connectivity.ConnectivityBroadCastReceiver
import com.bigoloo.interview.cafe.nazdikia.data.datastore.CoroutineDataValidityDataStore
import com.bigoloo.interview.cafe.nazdikia.data.datastore.CoroutineInternetConnectivityDataStore
import com.bigoloo.interview.cafe.nazdikia.data.datastore.CoroutineLocationDataStore
import com.bigoloo.interview.cafe.nazdikia.data.datastore.LocationPermissionStatusDataStoreImp
import com.bigoloo.interview.cafe.nazdikia.data.db.AppDatabase
import com.bigoloo.interview.cafe.nazdikia.data.location.FusedLocationTrackerService
import com.bigoloo.interview.cafe.nazdikia.data.network.AuthInterceptor
import com.bigoloo.interview.cafe.nazdikia.data.network.VenueApi
import com.bigoloo.interview.cafe.nazdikia.data.repository.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.data.repository.LocalSharedRepository
import com.bigoloo.interview.cafe.nazdikia.data.repository.RemotePlaceRepository
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.DataValidityDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.InternetConnectivityDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.LocationDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.LocationPermissionStatusDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.CallRemoteAndSyncWithLocalUseCase
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.FetchVenuesIfNeededUseCase
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.ReadLocalFirstOrCallRemoteUseCase
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.SyncNearybyVenue
import com.bigoloo.interview.cafe.nazdikia.domain.repository.SharedRepository
import com.bigoloo.interview.cafe.nazdikia.presentation.ApplicationLifecycleObserver
import com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel.MainActivityViewModel
import com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel.VenueViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
            get<CoroutineDispatcherProvider>
                (named("dispatcherProvider")).backgroundDispatcher()

        )
    }
    single {
        ConnectivityBroadCastReceiver(
            get()
        )
    }
    single { ApplicationLifecycleObserver(get(), get(), get(), get(), get()) }
    single<SharedRepository> { LocalSharedRepository(get()) }
    single(named("dispatcherProvider")) { applicationDispatcherProvider }
    single<DataValidityDataStore> { CoroutineDataValidityDataStore() }
    single { RemotePlaceRepository(get()) }
    single { LocalPlaceRepository(get<AppDatabase>(named("database")).venueDao(), get()) }
    single<AppDatabase>(named("database")) {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "nazdikia-name"
        ).build()
    }
    single {
        SyncNearybyVenue(
            get(), get(), get<CoroutineDispatcherProvider>
                (named("dispatcherProvider")).backgroundDispatcher()
        )
    }


    factory { FetchVenuesIfNeededUseCase(get(), get(), get()) }
    factory { CallRemoteAndSyncWithLocalUseCase(get(), get(), get()) }
    factory { ReadLocalFirstOrCallRemoteUseCase(get(), get(), get(), get()) }


    single<Retrofit>(named("retrofit")) {
        val client = OkHttpClient.Builder().addInterceptor(AuthInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
        Retrofit.Builder()
            .baseUrl("https://api.foursquare.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    }

    single<VenueApi> {
        get<Retrofit>(named("retrofit")).create(VenueApi::class.java)
    }
    viewModel {
        MainActivityViewModel(
            get(), get<CoroutineDispatcherProvider>
                (named("dispatcherProvider"))
        )
    }

    viewModel {
        VenueViewModel(
            get(), get(), get<CoroutineDispatcherProvider>
                (named("dispatcherProvider"))
        )
    }
}