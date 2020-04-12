package com.bigoloo.interview.cafe.nazdikia.data.network

import com.bigoloo.interview.cafe.nazdikia.models.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface VenueApi {

    @GET("v2/venues/explore")
    suspend fun getVenue(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("ll") ll: String
    ): Response
}