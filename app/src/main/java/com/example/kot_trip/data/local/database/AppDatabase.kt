package com.example.kot_trip.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kot_trip.data.local.dao.PostDao
import com.example.kot_trip.model.Post

@Database(entities = [Post::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
