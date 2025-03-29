package com.example.kot_trip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey val id: String,
    val title: String,
    val country: String,
    val city: String,
    val content: String,
    val imageUrl: String,
    val userId: String,
    val timestamp: Long
)
