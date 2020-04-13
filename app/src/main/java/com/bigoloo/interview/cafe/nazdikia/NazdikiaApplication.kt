package com.bigoloo.interview.cafe.nazdikia

import android.app.Application
import com.bigoloo.interview.cafe.nazdikia.di.koinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class NazdikiaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin()
    }

    private fun initializeKoin() {
        startKoin {
            androidContext(this@NazdikiaApplication)
            modules(koinModule)
        }
    }
}