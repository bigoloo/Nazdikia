package com.bigoloo.interview.cafe.nazdikia

import com.bigoloo.interview.cafe.nazdikia.base.MainCoroutineRule
import com.bigoloo.interview.cafe.nazdikia.data.place.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.data.place.RemotePlaceRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

class GetNearByPlaceUseCaseTest {


    @RelaxedMockK
    lateinit var localPlaceRepository: LocalPlaceRepository

    @RelaxedMockK
    lateinit var remotePlaceRepository: RemotePlaceRepository



    @get:Rule
    private val mainCoroutineRule = MainCoroutineRule()


    @Before
    fun start() {
        MockKAnnotations.init(this)
    }


/*
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
                localPlaceRepository.getNearByPlaces(paginationInfo, location)
            }
            coVerify(exactly = 0) {
                remotePlaceRepository.getNearByPlaces(paginationInfo, location)
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
                localPlaceRepository.getNearByPlaces(paginationInfo, location)
            }
            coVerify(exactly = 1) {
                remotePlaceRepository.getNearByPlaces(paginationInfo, location)
            }
            coVerify(exactly = 1) {
                localPlaceRepository.savePlaces(any())
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
                localPlaceRepository.getNearByPlaces(paginationInfo, location)
            }
            coVerify(exactly = 1) {
                remotePlaceRepository.getNearByPlaces(paginationInfo, location)
            }
        }*/
}