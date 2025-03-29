package com.example.kot_trip.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kot_trip.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: String): LiveData<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserByIdOnce(id: String): User?
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserLive(id: String): LiveData<User?>
    @Delete
    suspend fun delete(user: User)
    @Query("DELETE FROM users")
    suspend fun deleteAll()


}
