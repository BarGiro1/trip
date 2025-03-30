package com.example.kot_trip.ui.saved


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.kot_trip.base.App
import com.example.kot_trip.model.Post
import com.example.kot_trip.data.repository.PostRepository

class SavedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PostRepository(application)

    private val _allPosts = MutableLiveData<LiveData<List<Post>>>()
    val allPosts: LiveData<List<Post>> get() = _allPosts.switchMap { it }

    fun loadPostsForUser(userId: String?) {
        val liveData = repository.getCachedPosts(userId)
        _allPosts.value = liveData
        repository.fetchPostsFromFirebase(userId)
    }


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
