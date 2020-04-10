package com.bigoloo.interview.cafe.nazdikia

import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Test

class LocationTrackerServiceTest {


    @Before
    fun start() {
        MockKAnnotations.init(this)
    }


    @Test
    fun `test when internet connectivity is on  then location service should send location`() {

    }

    @Test
    fun `test when internet connectivity is on  then location service shouldn't send location`() {

    }


}