package com.example.kot_trip.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val email: String = "",
    val profileImageUrl: String?
) : Parcelable {
    constructor() : this(
        id = "",
        name = "",
        email = "",
        profileImageUrl = null
    )
}
