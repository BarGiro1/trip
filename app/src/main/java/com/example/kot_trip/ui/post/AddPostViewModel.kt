package com.example.kot_trip.ui.post

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kot_trip.data.remote.FirebaseModel
import com.example.kot_trip.data.repository.PostRepository
import com.example.kot_trip.model.Post
import java.util.*


class AddPostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PostRepository(application)

    private val _status = MutableLiveData<String?>()
    val status: LiveData<String?> get() = _status

    fun addPost(
        title: String,
        description: String,
        city: String,
        country: String,
        userId: String = "demo_user", // בשלב מאוחר - מ-FirebaseAuth
        imageUrl: String = ""
    ) {
        if (title.isBlank() || description.isBlank() || city.isBlank() || country.isBlank()) {
            _status.value = "יש למלא את כל השדות"
            return
        }

        val post = Post(
            id = UUID.randomUUID().toString(),
            userId = userId,
            title = title,
            content = description,
            city = city,
            country = country,
            imageUrl = imageUrl
        )

        repository.addPost(
            post,
            onSuccess = { _status.postValue("הפוסט נוסף בהצלחה 🎉") },
            onFailure = { e -> _status.postValue("שגיאה: ${e.message}") }
        )
    }

    fun clearStatus() {
        _status.value = null
    }
}