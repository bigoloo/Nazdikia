package com.bigoloo.interview.cafe.nazdikia

import com.bigoloo.interview.cafe.nazdikia.base.MainCoroutineRule
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.CallRemoteVenueAndNotifyUseCase
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.FetchVenueWithPaginationUseCase
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.GetNearbyVenueUseCase
import com.bigoloo.interview.cafe.nazdikia.domain.repository.SharedRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PaginationInfo
import com.bigoloo.interview.cafe.nazdikia.models.Tracker
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetNearbyVenueUseCaseTest {


    @get:Rule
    private val mainCoroutineRule = MainCoroutineRule()

    @RelaxedMockK
    private lateinit var callRemoteVenueAndNotifyUseCase: CallRemoteVenueAndNotifyUseCase

    @RelaxedMockK
    private lateinit var fetchVenueWithPagination: FetchVenueWithPaginationUseCase

    @RelaxedMockK
    private lateinit var sharedRepository: SharedRepository

    @Before
    fun start() {
        MockKAnnotations.init(this)
    }

    private fun createGetNearbyVenueUseCase(): GetNearbyVenueUseCase {
        return GetNearbyVenueUseCase(
            fetchVenueWithPagination,
            callRemoteVenueAndNotifyUseCase,
            sharedRepository
        )
    }

    @Test
    fun `test when location is  change then callRemoteVenueUseCase should be called`() =
        mainCoroutineRule.runBlockingTest {

            every { sharedRepository.getLastLocation() } returns null

            val getNearbyVenue = createGetNearbyVenueUseCase()

            getNearbyVenue.fetchVenues(Tracker.Available(Location(32.4, 56.5)), null)

            coVerify(exactly = 0) {
                fetchVenueWithPagination.execute(any(), any())
            }
            coVerify(exactly = 1) {
                callRemoteVenueAndNotifyUseCase.execute(any(), any())
            }


        }

    @Test
    fun `test when location is not change and data is expired then callRemoteVenueUseCase should be called `() =
        mainCoroutineRule.runBlockingTest {
            val location = mockk<Location>()
            every { sharedRepository.getLastLocation() } returns location
            every { sharedRepository.getLastDataReceivedTimestamp() } returns 0
            every { location.isInZone(any(), any()) } returns false
            val getNearbyVenue = createGetNearbyVenueUseCase()
            getNearbyVenue.fetchVenues(Tracker.Available(location), null)
            coVerify(exactly = 0) {
                fetchVenueWithPagination.execute(any(), any())
            }
            coVerify(exactly = 1) {
                callRemoteVenueAndNotifyUseCase.execute(any(), any())
            }
        }

    @Test
    fun `test when location is not changed and data is not expired then if call from ui(paginationinfo has value ) fetchVenueWithPagination should be called`() =
        mainCoroutineRule.runBlockingTest {
            val location = mockk<Location>()
            every { sharedRepository.getLastLocation() } returns location
            every { sharedRepository.getLastDataReceivedTimestamp() } returns System.currentTimeMillis()
            every { location.isInZone(any(), any()) } returns false
            val getNearbyVenue = createGetNearbyVenueUseCase()
            getNearbyVenue.fetchVenues(Tracker.Available(location), PaginationInfo(0, 4, 3))

            coVerify(exactly = 1) {
                fetchVenueWithPagination.execute(any(), any())
            }
            coVerify(exactly = 0) {
                callRemoteVenueAndNotifyUseCase.execute(any(), any())
            }

        }

    @Test
    fun `test when location is not change and data is not expired then if call from tracker changes(paginationinfo has not  value ) fetchVenueWithPagination shouldn't be called `() =
        mainCoroutineRule.runBlockingTest {
            val location = mockk<Location>()
            every { sharedRepository.getLastLocation() } returns location
            every { sharedRepository.getLastDataReceivedTimestamp() } returns System.currentTimeMillis()
            every { location.isInZone(any(), any()) } returns false
            val getNearbyVenue = createGetNearbyVenueUseCase()
            getNearbyVenue.fetchVenues(Tracker.Available(location), null)

            coVerify(exactly = 0) {
                fetchVenueWithPagination.execute(any(), any())
            }
            coVerify(exactly = 0) {
                callRemoteVenueAndNotifyUseCase.execute(any(), any())
            }

        }

}