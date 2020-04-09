package com.bigoloo.interview.cafe.nazdikia

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test


class GetNearByPlaceUseCaseTest {



    @Before
    fun start(){
        MockKAnnotations.init(this)
    }

    @Test
    fun `test when user location is empty api should be not be called`() {


    }

    @Test
    fun `test when user has location and it is not changed api should not be called`() {

    }

    @Test
    fun `test when user has location and it is not changed data should read from local repository`() {

    }

    @Test
    fun `test when user has location and is offline data should be read from local repository`() {

    }

    @Test
    fun `test when user location is not change and data is old api should be called`() {

    }

    @Test
    fun `test when user has location and it was offline and then online data should be read from api`() {

    }

    @Test
    fun `test when user has location and it api is called local repository should be synced`() {

    }
    @Test
    fun `when connectivity is disconnected location tracker shouldn't be get data`(){

    }

}