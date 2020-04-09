package com.bigoloo.interview.cafe.nazdikia

import com.bigoloo.interview.cafe.nazdikia.base.MainCoroutineRule
import com.bigoloo.interview.cafe.nazdikia.data.location.CacheDataRepository
import com.bigoloo.interview.cafe.nazdikia.data.place.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.data.place.RemotePlaceRepository
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.GetNearByPlaceUseCase
import com.bigoloo.interview.cafe.nazdikia.models.Location
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetNearByPlaceUseCaseTest {


    @RelaxedMockK
    lateinit var localPlaceRepository: LocalPlaceRepository

    @RelaxedMockK
    lateinit var remotePlaceRepository: RemotePlaceRepository

    @RelaxedMockK
    lateinit var dataRepository: CacheDataRepository

    @get:Rule
    private val mainCoroutineRule = MainCoroutineRule()


    @Before
    fun start() {
        MockKAnnotations.init(this)
    }

    private fun createGetNearByPlaceUseCase(): GetNearByPlaceUseCase {
        return GetNearByPlaceUseCase(
            localPlaceRepository,
            remotePlaceRepository,
            dataRepository
        )
    }

    @Test
    fun `test when  location is not changed then local repository should be called`() =
        mainCoroutineRule.runBlockingTest {
            val lastLocation = mockk<Location>()
            val getNearByPlaceUseCase = createGetNearByPlaceUseCase()
            every { dataRepository.getLastLocation() } returns lastLocation
            every { dataRepository.getLastDataReceivedTimestamp() } returns System.currentTimeMillis()
            every { dataRepository.getReceivedDataTimeThreshold() } returns 40000L
            every { lastLocation.isInZone(any(), any()) } returns false


            getNearByPlaceUseCase.getPlaces(lastLocation)

            coVerify(exactly = 1) {
                localPlaceRepository.getNearByPlaces()
            }
            coVerify(exactly = 0) {
                remotePlaceRepository.getNearByPlaces()
            }
        }

    @Test
    fun `test when location is changed then remote repository should be called`() =
        mainCoroutineRule.runBlockingTest {
            val lastLocation = mockk<Location>()
            val getNearByPlaceUseCase = createGetNearByPlaceUseCase()
            every { dataRepository.getLastLocation() } returns lastLocation

            every { lastLocation.isInZone(any(), any()) } returns true


            getNearByPlaceUseCase.getPlaces(lastLocation)

            coVerify(exactly = 0) {
                localPlaceRepository.getNearByPlaces()
            }
            coVerify(exactly = 1) {
                remotePlaceRepository.getNearByPlaces()
            }
            coVerify(exactly = 1) {
                localPlaceRepository.setPlaces(any())
            }
        }

    @Test
    fun `test when location is not changed but data is old then remote repository should be called`() =
        mainCoroutineRule.runBlockingTest {
            val lastLocation = mockk<Location>()
            val getNearByPlaceUseCase = createGetNearByPlaceUseCase()
            every { dataRepository.getLastLocation() } returns lastLocation
            every { dataRepository.getLastDataReceivedTimestamp() } returns 12L
            every { dataRepository.getReceivedDataTimeThreshold() } returns 1L
            every { lastLocation.isInZone(any(), any()) } returns true

            getNearByPlaceUseCase.getPlaces(lastLocation)

            coVerify(exactly = 0) {
                localPlaceRepository.getNearByPlaces()
            }
            coVerify(exactly = 1) {
                remotePlaceRepository.getNearByPlaces()
            }
        }
}