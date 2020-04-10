package com.bigoloo.interview.cafe.nazdikia.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bigoloo.interview.cafe.nazdikia.R
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val applicationLifecycleObserver: ApplicationLifecycleObserver by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(applicationLifecycleObserver)
    }


}