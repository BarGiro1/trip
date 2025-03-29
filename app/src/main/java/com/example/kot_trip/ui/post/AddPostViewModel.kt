package com.example.kot_trip.ui.post

import android.app.Application

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kot_trip.base.Status
import com.example.kot_trip.data.repository.PostRepository
import com.example.kot_trip.model.Post
import java.util.*


class AddPostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PostRepository(application)
    private val _status = MutableLiveData<Status?>()
    val status: LiveData<Status?> get() = _status

    fun addPost(
        title: String,
        description: String,
        city: String,
        country: String,
        userId: String,
        imageBitmap: Bitmap?
    ) {
        if (title.isBlank() || description.isBlank() || city.isBlank() || country.isBlank()) {
            Log.d("AddPostViewModel", "addPost: missing fields")
            _status.value = Status("יש למלא את כל השדות", isSuccess = false)
            return
        }

        val post = Post(
            id = UUID.randomUUID().toString(),
            userId = userId,
            title = title,
            content = description,
            city = city,
            country = country,
            imageUrl = ""
        )

        repository.addPost(
            post,
            imageBitmap,
            onSuccess = {
                _status.postValue(Status("הפוסט נוסף בהצלחה", isSuccess = true))
            },
            onFailure = { e -> _status.postValue(Status(e.message ?: "שגיאה", isSuccess = false)) }
        )
    }

    fun updatePost(
        post: Post,
        imageBitmap: Bitmap?
    ) {
        repository.updatePost(
            post,
            onSuccess = {
                _status.postValue(Status("הפוסט עודכן בהצלחה", isSuccess = true))
            },
            onFailure = { e -> _status.postValue(Status(e.message ?: "שגיאה", isSuccess = false)) }
        )
    }

    fun clearStatus() {
        _status.value = null
    }
}