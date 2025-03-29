package com.example.kot_trip.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "posts")
data class Post(
    @PrimaryKey val id: String,
    val title: String,
    val country: String,
    val city: String,
    val content: String,
    val imageUrl: String,
    val userId: String,
    val createdAt: Long = System.currentTimeMillis()
): Parcelable {
    constructor() : this(
        id = "",
        title = "",
        country = "",
        city = "",
        content = "",
        imageUrl = "",
        userId = ""
    )
}
