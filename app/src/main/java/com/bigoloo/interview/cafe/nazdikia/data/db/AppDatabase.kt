package com.bigoloo.interview.cafe.nazdikia.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bigoloo.interview.cafe.nazdikia.models.Category
import com.bigoloo.interview.cafe.nazdikia.models.Venue


@Database(entities = [Venue::class, Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun venueDao(): VenueDao
}