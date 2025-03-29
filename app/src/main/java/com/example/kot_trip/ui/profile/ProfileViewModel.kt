package com.example.kot_trip.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kot_trip.base.App
import com.example.kot_trip.base.Status
import com.example.kot_trip.base.Utils
import com.example.kot_trip.model.User
import com.example.kot_trip.data.repository.UserRepository

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)
    private val _user = MutableLiveData<User>()
    private val _status = MutableLiveData<Status?>()
    val status: LiveData<Status?> get() = _status

    fun getUserProfile(): LiveData<User> {
        repository.getUserProfile(
            App().getUserId()!!,
            onSuccess = { user ->
                _user.postValue(user)
            },
            onFailure = { e ->
                Log.e("ProfileViewModel", "Error getting user: ${e.message}")
            }
        )
        return _user
    }

    fun updateUser(user: User, profileImage: Bitmap?) {

        if (user.name.isBlank() || user.email.isBlank()) {
            Log.d("ProfileViewModel", "updateUser: missing fields")
            _status.value = Status("יש למלא את כל השדות", isSuccess = false)
            return
        }

        repository.updateUser(
            user,
            profileImage,
            onSuccess = {
                Log.d("ProfileViewModel", "User updated successfully")
                _status.value = Status("הפרופיל עודכן בהצלחה", isSuccess = true)
            },
            onFailure = { e ->
                Log.e("ProfileViewModel", "Error updating user: ${e.message}")
                _status.value = Status(e.message ?: "שגיאה", isSuccess = false)
            }
        )
    }
}