package com.example.kot_trip.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.kot_trip.data.local.dao.PostDao
import com.example.kot_trip.data.local.database.AppDatabaseInstance
import com.example.kot_trip.model.Post
import com.example.kot_trip.data.remote.FirebaseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostRepository(context: Context) {

    private val postDao: PostDao = AppDatabaseInstance.database.postDao()

    fun getCachedPosts(): LiveData<List<Post>> = postDao.getAllPosts()

    fun fetchPostsFromFirebase() {
        FirebaseModel.getAllPosts(
            onSuccess = { posts ->
                CoroutineScope(Dispatchers.IO).launch {
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
        // add to Firebase and then to local database
        FirebaseModel.addPost(
            post,
            onSuccess = {
                CoroutineScope(Dispatchers.IO).launch {
                    postDao.insert(post)
                }
                onSuccess()
            },
            onFailure = { e ->
                onFailure(e)
            }
        )
    }

    fun updatePost(
        post: Post,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        FirebaseModel.updatePost(
            post,
            onSuccess = {
                CoroutineScope(Dispatchers.IO).launch {
                    postDao.insert(post)
                }
                onSuccess()
            },
            onFailure = { e ->
                onFailure(e)
            }
        )
    }
}
