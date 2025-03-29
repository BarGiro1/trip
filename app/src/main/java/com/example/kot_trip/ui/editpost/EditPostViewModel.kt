package com.example.kot_trip.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.kot_trip.model.Post
import com.example.kot_trip.data.repository.PostRepository

class EditPostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PostRepository(application)

    fun updatePost(post: Post) {
        repository.updatePost(
            post,
            onSuccess = {
              Log.d("EditPostViewModel", "Post updated successfully")
            },
            onFailure = { e ->
                Log.e("EditPostViewModel", "Error updating post: ${e.message}")
            }
        )
    }
}
