package com.example.kot_trip.ui.home


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.kot_trip.model.Post
import com.example.kot_trip.data.repository.PostRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PostRepository(application)

    val allPosts: LiveData<List<Post>> = repository.getAllPostsFromCache()
    fun refreshPosts() {
        repository.fetchPostsFromFirebase()
    }


}
