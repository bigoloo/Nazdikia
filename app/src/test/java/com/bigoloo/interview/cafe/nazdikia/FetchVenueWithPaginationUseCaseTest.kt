package com.bigoloo.interview.cafe.nazdikia

import com.bigoloo.interview.cafe.nazdikia.base.MainCoroutineRule
import com.bigoloo.interview.cafe.nazdikia.data.repository.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.CallRemoteAndSyncWithLocalUseCase
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.ReadLocalFirstOrCallRemoteUseCase
import com.bigoloo.interview.cafe.nazdikia.domain.repository.SharedRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PageInfo
import com.bigoloo.interview.cafe.nazdikia.models.PageResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FetchVenueWithPaginationUseCaseTest {


    @get:Rule
    private val mainCoroutineRule = MainCoroutineRule()

    @RelaxedMockK
    lateinit var localPlaceRepository: LocalPlaceRepository

    @RelaxedMockK
    lateinit var callRemote: CallRemoteAndSyncWithLocalUseCase

    @RelaxedMockK
    lateinit var sharedRepository: SharedRepository

    @Before
    fun before() {
        MockKAnnotations.init(this)
    }

    private val mockLocation = Location(lat = 2.2, lng = 42.3)

    private fun createFetchVenueWithPaginationUseCase(): ReadLocalFirstOrCallRemoteUseCase {
        return ReadLocalFirstOrCallRemoteUseCase(
            localPlaceRepository,
            callRemote,
            sharedRepository
        )
    }

    @Test
    fun `when total data is 0 and saved data is 0 then remote database is called`() =
        mainCoroutineRule.runBlockingTest {
            val paginationInfo = PageInfo(0, 20, -1)

            coEvery { localPlaceRepository.getSavedVenueCount() } returns 0
            every { sharedRepository.getLastLocation() } returns mockLocation
            every { sharedRepository.getTotalPage() } returns -1
            coEvery { callRemote.execute(any(), any(), any()) } returns PageResult(
                paginationInfo,
                emptyList()
            )
            val fetchVenueWithPaginationUseCase = createFetchVenueWithPaginationUseCase()
            fetchVenueWithPaginationUseCase.execute(paginationInfo)

            coVerify(exactly = 0) {
                localPlaceRepository.getNearbyVenues(any())
            }
            coVerify(exactly = 1) {
                callRemote.execute(paginationInfo, mockLocation, false)
            }

        }

    @Test
    fun `when total data is the same as saved data  and not zero then local database should be called `() =
        mainCoroutineRule.runBlockingTest {

            coEvery { localPlaceRepository.getSavedVenueCount() } returns 100

            val paginationInfo = PageInfo(32, 20, 32)

            coEvery { localPlaceRepository.getSavedVenueCount() } returns 32
            every { sharedRepository.getLastLocation() } returns mockLocation
            every { sharedRepository.getTotalPage() } returns 32
            coEvery { callRemote.execute(any(), any(), any()) } returns PageResult(
                paginationInfo,
                emptyList()
            )

            val fetchVenueWithPaginationUseCase = createFetchVenueWithPaginationUseCase()
            fetchVenueWithPaginationUseCase.execute(paginationInfo)

            coVerify(exactly = 1) {
                localPlaceRepository.getNearbyVenues(any())
            }
            coVerify(exactly = 0) {
                callRemote.execute(paginationInfo, mockLocation, false)
            }

        }


    @Test
    fun `when totalPage is bigger than savedCount and offset + limit is less than saved count  then local database  should be called`() =
        mainCoroutineRule.runBlockingTest {
            val paginationInfo = PageInfo(0, 4, 12)

            coEvery { localPlaceRepository.getSavedVenueCount() } returns 12
            every { sharedRepository.getTotalPage() } returns 32
            val fetchVenueWithPaginationUseCase = createFetchVenueWithPaginationUseCase()
            fetchVenueWithPaginationUseCase.execute(paginationInfo)

            coVerify(exactly = 1) {
                localPlaceRepository.getNearbyVenues(any())
            }
            coVerify(exactly = 0) {
                callRemote.execute(paginationInfo, mockLocation, false)
            }

        }

    @Test
    fun `when total data is bigger than saved data and offset +limit is equal than saved data then remote database  should be called`() =
        mainCoroutineRule.runBlockingTest {

            val paginationInfo = PageInfo(12, 4, 12)

            coEvery { localPlaceRepository.getSavedVenueCount() } returns 12
            every { sharedRepository.getTotalPage() } returns 32
            coEvery { callRemote.execute(any(), any(), any()) } returns PageResult(
                paginationInfo,
                emptyList()
            )

            val fetchVenueWithPaginationUseCase = createFetchVenueWithPaginationUseCase()
            fetchVenueWithPaginationUseCase.execute(paginationInfo)

            coVerify(exactly = 0) {
                localPlaceRepository.getNearbyVenues(any())
            }
            coVerify(exactly = 1) {
                callRemote.execute(any(), any(), any())
            }
        }

}