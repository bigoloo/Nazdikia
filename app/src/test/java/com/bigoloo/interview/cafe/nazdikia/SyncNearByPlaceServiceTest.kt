package com.bigoloo.interview.cafe.nazdikia


import com.bigoloo.interview.cafe.nazdikia.base.MainCoroutineRule
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule


class SyncNearByPlaceServiceTest {



    @get:Rule
    private val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun start() {
        MockKAnnotations.init(this)
    }



}