package com.example.kot_trip.model

data class Post(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val country: String = "",
    val city: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
