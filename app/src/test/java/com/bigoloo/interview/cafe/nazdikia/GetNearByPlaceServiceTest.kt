package com.bigoloo.interview.cafe.nazdikia


import com.bigoloo.interview.cafe.nazdikia.base.MainCoroutineRule
import com.bigoloo.interview.cafe.nazdikia.base.createTestDispatcherProvider
import com.bigoloo.interview.cafe.nazdikia.data.place.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.data.place.RemotePlaceRepository
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.GetNearByPlaceService
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.GetNearByPlaceUseCase
import com.bigoloo.interview.cafe.nazdikia.domain.location.LocationTracker
import com.bigoloo.interview.cafe.nazdikia.domain.repository.DataRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class GetNearByPlaceServiceTest {

    @RelaxedMockK
    lateinit var locationTracker: LocationTracker

    @RelaxedMockK
    lateinit var localPlaceRepository: LocalPlaceRepository

    @RelaxedMockK
    lateinit var remotePlaceRepository: RemotePlaceRepository

    @RelaxedMockK
    lateinit var dataRepository: DataRepository


    @get:Rule
    private val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun start() {
        MockKAnnotations.init(this)
    }

    private fun createGetNearbyPlaceService(): GetNearByPlaceService {

        val getNearByPlaceUseCase =
            GetNearByPlaceUseCase(localPlaceRepository, remotePlaceRepository, dataRepository)
        return GetNearByPlaceService(
            locationTracker, getNearByPlaceUseCase,
            createTestDispatcherProvider(mainCoroutineRule.getDispatcher()).backgroundDispatcher()
        )
    }


    @Test
    fun `test when user has location and is offline data should be read from local repository`() {

    }



    @Test
    fun `test when user has location and it was offline and then online data should be read from api`() {

    }



    @Test
    fun `when connectivity is disconnected location tracker shouldn't be get data`() {

    }

}