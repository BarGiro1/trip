package com.example.kot_trip.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.kot_trip.data.local.database.AppLocalDb
import com.example.kot_trip.data.local.dao.PostDao
import com.example.kot_trip.model.Post
import com.example.kot_trip.data.remote.FirebaseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostRepository(context: Context) {

    private val postDao: PostDao = AppLocalDb.database.postDao()

    fun getCachedPosts(): LiveData<List<Post>> = postDao.getAllPosts()

    fun fetchPostsFromFirebase() {
        FirebaseModel.getAllPosts(
            onSuccess = { posts ->
                CoroutineScope(Dispatchers.IO).launch {
                    postDao.clearAll()
                    postDao.insertAll(posts)
                }
            },
            onFailure = {
            }
        )
    }

    fun addPost(
        post: Post,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        FirebaseModel.addPost(post, onSuccess, onFailure)
    }
}
