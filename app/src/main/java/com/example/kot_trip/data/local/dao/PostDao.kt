package com.example.kot_trip.data.local.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kot_trip.model.Post


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PostDao {

    @Query("SELECT * FROM posts WHERE (userId = :userId OR :userId IS NULL) ORDER BY createdAt DESC")
    fun getAllPosts(userId: String?): LiveData<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>)

    @Delete
    suspend fun delete(post: Post)


    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAllPostsAllUsers(): LiveData<List<Post>>

    @Query("DELETE FROM posts")
    fun deleteAll()

}
