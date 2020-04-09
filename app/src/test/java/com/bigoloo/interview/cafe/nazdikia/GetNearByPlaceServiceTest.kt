package com.bigoloo.interview.cafe.nazdikia

import com.bigoloo.interview.cafe.nazdikia.base.MainCoroutineRule
import com.bigoloo.interview.cafe.nazdikia.base.createTestDispatcherProvider
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.GetNearByPlaceService
import com.bigoloo.interview.cafe.nazdikia.domain.location.LocationTracker
import com.bigoloo.interview.cafe.nazdikia.domain.repository.LocalPlaceRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class GetNearByPlaceServiceTest {

    @RelaxedMockK
    lateinit var locationTracker: LocationTracker

    @RelaxedMockK
    lateinit var localPlaceRepository: LocalPlaceRepository

    @get:Rule
    private val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun start() {
        MockKAnnotations.init(this)
    }

    private fun createGetNearbyPlaceService(): GetNearByPlaceService {
        return GetNearByPlaceService(locationTracker, localPlaceRepository,
            createTestDispatcherProvider(mainCoroutineRule.getDispatcher()).backgroundDispatcher())
    }

    @Test
    fun `test when user location is empty api should be not be called`() = mainCoroutineRule.runBlockingTest {
        coEvery { locationTracker.getTracker() } returns emptyFlow()
        val getNearByPlaceUseCaseTest = createGetNearbyPlaceService()
        getNearByPlaceUseCaseTest.onCreate()
        verify(exactly = 0) {
            localPlaceRepository.getNearByPlaces()
        }

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
    fun `when connectivity is disconnected location tracker shouldn't be get data`() {

    }

}