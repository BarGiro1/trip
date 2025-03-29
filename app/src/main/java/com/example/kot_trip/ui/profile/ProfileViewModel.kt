package com.example.kot_trip.ui.profile


import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kot_trip.base.App
import com.example.kot_trip.data.repository.UserRepository
import com.example.kot_trip.model.User
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            val userProfile = userRepository.getUserProfile(
                App.Globals.userId!!,
                onSuccess = { user ->
                    _user.postValue(user)
                },
                onFailure = { e ->
                    Toast.makeText(App.Globals.context, e.message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    fun updateUserProfile(user: User) {
        viewModelScope.launch {
            _user.postValue(user)
        }
    }
}