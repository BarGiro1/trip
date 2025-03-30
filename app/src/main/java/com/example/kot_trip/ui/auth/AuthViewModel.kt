package com.example.kot_trip.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kot_trip.base.App
import com.example.kot_trip.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _signUpSuccess = MutableLiveData<Boolean>()
    val signUpSuccess: LiveData<Boolean> = _signUpSuccess

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun loginUser(email: String, password: String, onError: (String) -> Unit) = viewModelScope.launch {
        _loading.value = true
        repository.loginUser(email, password,
            onSuccess = {
                App().setUserId(it.id)
                _loginSuccess.postValue(true)
                _loading.postValue(false)
            },
            onFailure = { error ->
                onError(error.message ?: "שגיאה כללית")
                _loading.postValue(false)
            }
        )
    }

    fun register(name: String, email: String, password: String, onError: (String) -> Unit) = viewModelScope.launch {
        _loading.value = true
        repository.registerUser(
            name = name,
            email = email,
            password = password,
            onSuccess = {
                App().setUserId(it.id)
                _signUpSuccess.postValue(true)
                _loading.postValue(false)
            },
            onFailure = { error ->
                onError(error.message ?: "שגיאה כללית")
                _loading.postValue(false)
            }
        )
    }

    class Factory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
