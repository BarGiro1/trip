package com.example.kot_trip.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kot_trip.dao.FirebaseModel
import com.example.kot_trip.model.Post
import java.util.*

class AddPostViewModel : ViewModel() {

    private val _status = MutableLiveData<String?>()
    val status: LiveData<String?> get() = _status

    fun submitPost(title: String, description: String, city: String, country: String) {

        if (title.isBlank() || description.isBlank() || city.isBlank() || country.isBlank()) {
            _status.value = "יש למלא את כל השדות"
            return
        }

        val post = Post(
            id = UUID.randomUUID().toString(),
            userId = "demo_user",
            title = title,
            description = description,
            city = city,
            country = country
        )

        FirebaseModel.addPost(post,
            onSuccess = { _status.value = "הפוסט נוסף בהצלחה" },
            onFailure = { _status.value = "שגיאה: ${it.message}" }
        )
    }

    fun clearStatus() {
        _status.value = null
    }
}
