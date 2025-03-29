package com.example.kot_trip.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kot_trip.base.App
import com.example.kot_trip.data.local.dao.PostDao
import com.example.kot_trip.model.Post


@Database(entities = [Post::class], version = 2)
abstract class AppLocalDbRepository: RoomDatabase() {
    abstract fun postDao(): PostDao
}

object AppLocalDb {
    val database: AppLocalDbRepository by lazy {
        val context = App.Globals.context ?: throw IllegalStateException("Application context is missing")
        Room.databaseBuilder(
            context,
            AppLocalDbRepository::class.java,
            "kot_trip_db"
        ).fallbackToDestructiveMigration().build()
    }
}