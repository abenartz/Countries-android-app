package com.example.countries.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.countries.models.Country

@Database(entities = [Country::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getCountryDao(): CountriesDao

    companion object{
        val DATABASE_NAME: String = "app_db"
    }
}