package com.bigoloo.interview.cafe.nazdikia.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bigoloo.interview.cafe.nazdikia.models.Venue

@Dao
interface VenueDao {


    @Query("SELECT * FROM venue  LIMIT :limit OFFSET :offset")
    fun getVenue(offset: Int, limit: Int): List<Venue>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(venue: List<Venue>): List<Long>

    @Query("DELETE FROM venue")
    suspend fun deleteAll()

    @Query("SELECT COUNT(id) FROM venue")
    suspend fun getVenueCount(): Int
}