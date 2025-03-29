package com.example.kot_trip.data.local.database

import androidx.room.Room
import com.example.kot_trip.base.App


object AppDatabaseInstance {
    val database: AppDatabase by lazy {
        val context = App.Globals.context ?: throw IllegalStateException("Application context is missing")
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, // ✅ שם המחלקה האמיתי!
            "kot_trip_db"
        ).fallbackToDestructiveMigration().build()
    }
}
