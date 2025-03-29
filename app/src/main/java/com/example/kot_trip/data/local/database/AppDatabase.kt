package com.example.kot_trip.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kot_trip.data.local.dao.PostDao
import com.example.kot_trip.data.local.dao.UserDao
import com.example.kot_trip.model.Post
import com.example.kot_trip.model.User

@Database(entities = [Post::class, User::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao

}
