package com.example.kot_trip.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kot_trip.base.Status
import com.example.kot_trip.model.Post
import com.example.kot_trip.data.repository.PostRepository

class EditPostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PostRepository(application)
    private val _status = MutableLiveData<Status?>()
    val status: LiveData<Status?> get() = _status


    fun updatePost(post: Post, imageBitmap: Bitmap?) {
        repository.updatePost(
            post,
            imageBitmap,
            onSuccess = {
                Log.d("EditPostViewModel", "Post updated successfully")
                _status.postValue(Status("Post updated successfully", isSuccess = true))
            },
            onFailure = { e ->
                Log.e("EditPostViewModel", "Error updating post: ${e.message}")
                _status.postValue(Status(e.message ?: "Error updating post", isSuccess = false))
            }
        )
    }
}
