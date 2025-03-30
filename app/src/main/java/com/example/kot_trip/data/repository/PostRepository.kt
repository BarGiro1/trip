package com.example.kot_trip.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import android.graphics.Bitmap
import com.example.kot_trip.base.App
import com.example.kot_trip.data.CloudinaryModel
import com.example.kot_trip.data.local.dao.PostDao
import com.example.kot_trip.data.local.database.AppDatabaseInstance
import com.example.kot_trip.model.Post
import com.example.kot_trip.data.remote.FirebaseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostRepository(context: Context) {

    private val postDao: PostDao = AppDatabaseInstance.database.postDao()
    private val cloudinaryModel = CloudinaryModel()

    fun getCachedPosts(userId: String? = null): LiveData<List<Post>> = postDao.getAllPosts(userId)
    fun getAllPostsFromCache(): LiveData<List<Post>> = postDao.getAllPostsAllUsers()


    fun fetchPostsFromFirebase(userId: String? = null) {
        FirebaseModel.getAllPosts(
            userId,
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
        imageBitmap: Bitmap?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // add to Firebase and then to local database
        FirebaseModel.addPost(
            post,
            onSuccess = {
                imageBitmap?.let { bitmap ->
                    cloudinaryModel.uploadBitmap(
                        bitmap,
                        onSuccess = { imageUrl ->
                            post.imageUrl = imageUrl
                            updatePost(post, onSuccess, onFailure)
                        },
                        onError = { e ->
                            onFailure(Exception(e))
                        }
                    )
                } ?: run {
                    onSuccess()
                }
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
    fun logoutCleanUp() {
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            postDao.deleteAll()
        }
    }


    fun deletePost(
        post: Post,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        FirebaseModel.deletePost(
            post.id,
            onSuccess = {
                CoroutineScope(Dispatchers.IO).launch {
                    postDao.delete(post)
                }
                onSuccess()
            },
            onFailure = { e ->
                onFailure(e)
            }
        )
    }

}
