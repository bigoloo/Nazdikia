package com.bigoloo.interview.cafe.nazdikia


import com.bigoloo.interview.cafe.nazdikia.base.MainCoroutineRule
import com.bigoloo.interview.cafe.nazdikia.base.createTestDispatcherProvider
import com.bigoloo.interview.cafe.nazdikia.data.place.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.data.place.RemotePlaceRepository
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.LocationDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.GetNearByPlaceService
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.GetNearByPlaceUseCase
import com.bigoloo.interview.cafe.nazdikia.domain.repository.DataRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule


class GetNearByPlaceServiceTest {

    @RelaxedMockK
    lateinit
    var localPlaceRepository: LocalPlaceRepository

    @RelaxedMockK
    lateinit var remotePlaceRepository: RemotePlaceRepository

    @RelaxedMockK
    lateinit var dataRepository: DataRepository

    @RelaxedMockK
    lateinit var locationDataStore: LocationDataStore

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
            locationDataStore, getNearByPlaceUseCase,
            createTestDispatcherProvider(mainCoroutineRule.getDispatcher()).backgroundDispatcher()
        )
    }


}