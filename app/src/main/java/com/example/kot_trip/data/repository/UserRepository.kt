package com.example.kot_trip.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.kot_trip.base.App.Globals.context
import com.example.kot_trip.data.CloudinaryModel
import com.example.kot_trip.data.local.database.AppDatabaseInstance
import com.example.kot_trip.data.remote.FirebaseModel
import com.example.kot_trip.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository(private val context: Context) {

    private val userDao = AppDatabaseInstance.database.userDao()
    private val cloudinaryModel = CloudinaryModel()

    fun getCachedUser(userId: String): LiveData<User?> = userDao.getUserLive(userId)

    fun getUserProfile(userId: String, onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) {
        FirebaseModel.getUserById(
            userId,
            onSuccess = { user ->
                CoroutineScope(Dispatchers.IO).launch {
                    userDao.insert(user)
                }
                onSuccess(user)
            },
            onFailure = { onFailure(it) }
        )
    }

    fun saveUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        FirebaseModel.addUser(user,
            onSuccess = {
                CoroutineScope(Dispatchers.IO).launch {
                    userDao.insert(user)
                }
                onSuccess()
            },
            onFailure = { onFailure(it) }
        )
    }

    fun registerUser(
        name: String,
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        FirebaseModel.register(
            email = email,
            password = password,
            name = name,
            onSuccess = { user ->
                CoroutineScope(Dispatchers.IO).launch {
                    userDao.insert(user)
                }
                onSuccess(user)
            },
            onFailure = { onFailure(it) }
        )
    }

    fun loginUser(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        FirebaseModel.loginUser(
            email,
            password,
            onSuccess = { user ->
                CoroutineScope(Dispatchers.IO).launch {
                    userDao.insert(user)
                }
                onSuccess(user)
            },
            onFailure = { onFailure(it) }
        )
    }

    fun updateUser(
        user: User,
        profileImageBitmap: Bitmap?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        Log.d("UserRepository", "updateUser: updating user")
        FirebaseModel.updateUser(
            user,
            onSuccess = {
                profileImageBitmap?.let {
                    cloudinaryModel.uploadBitmap(
                        profileImageBitmap,
                        onSuccess = { imageUrl ->
                            val userCopy = user.copy(profileImageUrl = imageUrl)
                            FirebaseModel.updateUser(
                                userCopy,
                                onSuccess = {
                                    Log.d("UserRepository", "updateUser: user updated successfully")
                                    CoroutineScope(Dispatchers.IO).launch {
                                        userDao.insert(user)
                                    }
                                    onSuccess()
                                },
                                onFailure = { onFailure(it) })
                        }, onError = { onFailure(Exception(it)) })
                    CoroutineScope(Dispatchers.IO).launch {
                        userDao.insert(user)
                    }
                    onSuccess()
                }
                onSuccess()
            },
            onFailure = { onFailure(it) }
        )
    }
}
