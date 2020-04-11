package com.bigoloo.interview.cafe.nazdikia

import com.bigoloo.interview.cafe.nazdikia.base.MainCoroutineRule
import com.bigoloo.interview.cafe.nazdikia.data.repository.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.data.repository.RemotePlaceRepository
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.CallRemoteAndSyncWithLocalUseCase
import com.bigoloo.interview.cafe.nazdikia.domain.repository.SharedRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

class CallRemoteAndSyncWithLocalUseCaseTest {


    @get:Rule
    private val mainCoroutineRule = MainCoroutineRule()

    @RelaxedMockK
    lateinit var remotePlaceRepository: RemotePlaceRepository

    @RelaxedMockK
    lateinit var localPlaceRepository: LocalPlaceRepository

    @RelaxedMockK
    lateinit var sharedRepository: SharedRepository


    fun createCallRemoteAndSyncWithLocalUseCase(): CallRemoteAndSyncWithLocalUseCase {
        return CallRemoteAndSyncWithLocalUseCase(
            remotePlaceRepository,
            localPlaceRepository,
            sharedRepository
        )
    }

    @Before
    fun before() {
        MockKAnnotations.init(this)
    }

    private val mockPagination = PaginationInfo(0, 0, 0)
    private val mockLocation = Location(3.1, 3.4)

    @Test
    fun `test when api is called then timestamp should be changed`() =
        mainCoroutineRule.runBlockingTest {
            coEvery { remotePlaceRepository.getNearByPlaces(any(), any()) } returns emptyList()

            val callRemoteAndSyncWithLocalUseCase = createCallRemoteAndSyncWithLocalUseCase()
            callRemoteAndSyncWithLocalUseCase.execute(mockPagination, mockLocation, false)
            verify(exactly = 1) {
                sharedRepository.setLastDataReceivedTimestamp(any())
            }
        }

    @Test
    fun `test when api is called and erase local database is needed then local repository clear should be called`() =
        mainCoroutineRule.runBlockingTest {
            coEvery { remotePlaceRepository.getNearByPlaces(any(), any()) } returns emptyList()

            val callRemoteAndSyncWithLocalUseCase = createCallRemoteAndSyncWithLocalUseCase()
            callRemoteAndSyncWithLocalUseCase.execute(mockPagination, mockLocation, true)
            verify(exactly = 1) {
                localPlaceRepository.clearPlaces()
            }
        }

    @Test
    fun `test when api is called and  erase local database doesn't  needed  then local clear shouldn't be called`() =
        mainCoroutineRule.runBlockingTest {
            coEvery { remotePlaceRepository.getNearByPlaces(any(), any()) } returns emptyList()

            val callRemoteAndSyncWithLocalUseCase = createCallRemoteAndSyncWithLocalUseCase()
            callRemoteAndSyncWithLocalUseCase.execute(mockPagination, mockLocation, false)
            verify(exactly = 0) {
                localPlaceRepository.clearPlaces()
            }
        }

    @Test
    fun `test when api is called then data should be saved in database`() =
        mainCoroutineRule.runBlockingTest {
            coEvery { remotePlaceRepository.getNearByPlaces(any(), any()) } returns emptyList()

            val callRemoteAndSyncWithLocalUseCase = createCallRemoteAndSyncWithLocalUseCase()

            callRemoteAndSyncWithLocalUseCase.execute(
                mockPagination,
                mockLocation,
                Random.nextBoolean()
            )
            verify(exactly = 1) {
                localPlaceRepository.savePlaces(any())
            }
        }


    @Test
    fun `test when api is called and received data is empty then local data base should be erased or not `() {
        //TODO product decide about that
    }


}