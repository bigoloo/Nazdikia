package com.bigoloo.interview.cafe.nazdikia

import com.bigoloo.interview.cafe.nazdikia.base.MainCoroutineRule
import com.bigoloo.interview.cafe.nazdikia.data.repository.LocalPlaceRepository
import com.bigoloo.interview.cafe.nazdikia.domain.intractors.ReadLocalFirstOrCallRemoteUseCase
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.models.PageInfo
import io.mockk.MockKAnnotations
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
    lateinit var readFromLocalAndNotifyUseCase: ReadFromLocalAndNotifyUseCase

    @RelaxedMockK
    lateinit var callRemoteVenueAndNotifyUseCase: CallRemoteVenueAndNotifyUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this)
    }

    private fun createFetchVenueWithPaginationUseCase(): ReadLocalFirstOrCallRemoteUseCase {
        return ReadLocalFirstOrCallRemoteUseCase(
            localPlaceRepository,
            readFromLocalAndNotifyUseCase,
            callRemoteVenueAndNotifyUseCase
        )
    }

    @Test
    fun `when total data is 0 and saved data is 0 then remote database is called`() =
        mainCoroutineRule.runBlockingTest {
            val paginationInfo = PageInfo(0, 0, 0)
            val mockLocation = Location(2.2, 42.3)
            every { localPlaceRepository.getSavedVenueCount() } returns 0

            val fetchVenueWithPaginationUseCase = createFetchVenueWithPaginationUseCase()
            fetchVenueWithPaginationUseCase.execute(paginationInfo, mockLocation)

            coVerify(exactly = 0) {
                readFromLocalAndNotifyUseCase.execute(paginationInfo)
            }
            coVerify(exactly = 1) {
                callRemoteVenueAndNotifyUseCase.execute(paginationInfo, mockLocation)
            }

        }

    @Test
    fun `when total data is the same as saved data  and not zero then local database should be called `() =
        mainCoroutineRule.runBlockingTest {
            val paginationInfo = PageInfo(0, 0, 100)
            val mockLocation = Location(2.2, 42.3)
            every { localPlaceRepository.getSavedVenueCount() } returns 100

            val fetchVenueWithPaginationUseCase = createFetchVenueWithPaginationUseCase()
            fetchVenueWithPaginationUseCase.execute(paginationInfo, mockLocation)

            coVerify(exactly = 1) {
                readFromLocalAndNotifyUseCase.execute(paginationInfo)
            }
            coVerify(exactly = 0) {
                callRemoteVenueAndNotifyUseCase.execute(paginationInfo, mockLocation)
            }

        }


    @Test
    fun `when total data is bigger than saved data and offset +limit is less  than saved data then local database  should be called`() =
        mainCoroutineRule.runBlockingTest {
            val paginationInfo = PageInfo(0, 29, 12)
            val mockLocation = Location(2.2, 42.3)
            every { localPlaceRepository.getSavedVenueCount() } returns 30

            val fetchVenueWithPaginationUseCase = createFetchVenueWithPaginationUseCase()
            fetchVenueWithPaginationUseCase.execute(paginationInfo, mockLocation)

            coVerify(exactly = 1) {
                readFromLocalAndNotifyUseCase.execute(paginationInfo)
            }
            coVerify(exactly = 0) {
                callRemoteVenueAndNotifyUseCase.execute(paginationInfo, mockLocation)
            }

        }

    @Test
    fun `when total data is bigger than saved data and offset +limit is equal than saved data then local database  should be called`() =
        mainCoroutineRule.runBlockingTest {
            val paginationInfo = PageInfo(0, 30, 12)
            val mockLocation = Location(2.2, 42.3)
            every { localPlaceRepository.getSavedVenueCount() } returns 30

            val fetchVenueWithPaginationUseCase = createFetchVenueWithPaginationUseCase()
            fetchVenueWithPaginationUseCase.execute(paginationInfo, mockLocation)

            coVerify(exactly = 1) {
                readFromLocalAndNotifyUseCase.execute(paginationInfo)
            }
            coVerify(exactly = 0) {
                callRemoteVenueAndNotifyUseCase.execute(paginationInfo, mockLocation)
            }

        }


    @Test
    fun `when total data is bigger than saved data and offset+limit is bigger than saved data then remote api should be called`() =
        mainCoroutineRule.runBlockingTest {
            val paginationInfo = PageInfo(31, 30, 12)
            val mockLocation = Location(2.2, 42.3)
            every { localPlaceRepository.getSavedVenueCount() } returns 30

            val fetchVenueWithPaginationUseCase = createFetchVenueWithPaginationUseCase()
            fetchVenueWithPaginationUseCase.execute(paginationInfo, mockLocation)

            coVerify(exactly = 0) {
                readFromLocalAndNotifyUseCase.execute(paginationInfo)
            }
            coVerify(exactly = 1) {
                callRemoteVenueAndNotifyUseCase.execute(paginationInfo, mockLocation)
            }

        }

}