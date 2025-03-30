package com.example.kot_trip.ui.saved


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.kot_trip.base.App
import com.example.kot_trip.model.Post
import com.example.kot_trip.data.repository.PostRepository

class SavedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PostRepository(application)

    val allPosts: LiveData<List<Post>> = repository.getCachedPosts(App().getUserId())

    fun refreshPosts() {
        Log.d("SavedViewModel", "refreshPosts of user ${App().getUserId()}")
        repository.fetchPostsFromFirebase(App().getUserId())
    }

    fun deletePost(post: Post) {
        repository.deletePost(
            post,
            onSuccess = { refreshPosts() },
            onFailure = { /* Handle error */ }
        )
    }
}
