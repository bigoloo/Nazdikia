package com.bigoloo.interview.cafe.nazdikia

import com.bigoloo.interview.cafe.nazdikia.base.MainCoroutineRule
import com.bigoloo.interview.cafe.nazdikia.domain.datastore.DataValidityDataStore
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.CallRemoteAndSyncWithLocalUseCase
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.FetchVenuesIfNeededUseCase
import com.bigoloo.interview.cafe.nazdikia.domain.repository.SharedRepository
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.Tracker
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FetchVenuesIfNeededUseCaseTest {


    @get:Rule
    private val mainCoroutineRule = MainCoroutineRule()


    @RelaxedMockK
    private lateinit var callRemote: CallRemoteAndSyncWithLocalUseCase

    @RelaxedMockK
    private lateinit var dataValidityDataStore: DataValidityDataStore

    @RelaxedMockK
    private lateinit var sharedRepository: SharedRepository


    @Before
    fun start() {
        MockKAnnotations.init(this)
    }

    private fun createGetNearbyVenueUseCase(): FetchVenuesIfNeededUseCase {
        return FetchVenuesIfNeededUseCase(
            callRemote,
            dataValidityDataStore,
            sharedRepository
        )
    }

    @Test
    fun `test when location is  change then callRemoteVenueUseCase should be called`() =
        mainCoroutineRule.runBlockingTest {

            every { sharedRepository.getLastLocation() } returns null

            val getNearbyVenue = createGetNearbyVenueUseCase()

            getNearbyVenue.fetchVenues(Tracker.Available(Location(lat = 32.4, lng = 56.5)))

            coVerify(exactly = 1) {
                callRemote.execute(any(), any(), true)
                dataValidityDataStore.setIsValidData(false)
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
            getNearbyVenue.fetchVenues(Tracker.Available(location))

            coVerify(exactly = 1) {
                callRemote.execute(any(), any(), true)
                dataValidityDataStore.setIsValidData(false)
            }
        }


}