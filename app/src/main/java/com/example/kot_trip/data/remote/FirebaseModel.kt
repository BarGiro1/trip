package com.example.kot_trip.data.remote


import android.graphics.Bitmap
import android.util.Log
import com.example.kot_trip.model.Post
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream
import com.example.kot_trip.model.User

object FirebaseModel {
    private val db = FirebaseFirestore.getInstance()
    private val database = Firebase.firestore
    private val storage = Firebase.storage

    init {
        val setting = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {  })
        }

        database.firestoreSettings = setting
    }


    fun googleSignIn(token: String, onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) {
        Log.d("FirebaseModel", "Firebase googleSignIn")
        val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(token, null)
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()

        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                val firebaseUser = authResult.user
                if (firebaseUser != null) {
                    val user = User(
                        id = firebaseUser.uid,
                        name = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: "",
                        profileImageUrl = firebaseUser.photoUrl?.toString()
                    )
                    Log.d("FirebaseModel", "Firebase googleSignIn success: $user")
                    addUser(user, onSuccess = {
                        onSuccess(user)
                    }, onFailure = {
                        onFailure(it)
                    })
                } else {
                    onFailure(Exception("User is null"))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun addPost(post: Post, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("posts")
            .document(post.id)
            .set(post)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun updatePost(post: Post, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("posts")
            .document(post.id)
            .set(post)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun deletePost(postId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("posts")
            .document(postId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getAllPosts(userId: String?, onSuccess: (List<Post>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("posts")
            .get()
            .addOnSuccessListener { documents ->
                val posts = documents.map { it.toObject(Post::class.java) }
                if (userId != null) {
                    val filteredPosts = posts.filter { it.userId == userId }
                    onSuccess(filteredPosts)
                } else {
                    onSuccess(posts)
                }
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun getUserById(userId: String, onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                if (user != null) {
                    onSuccess(user)
                } else {
                    onFailure(Exception("User not found"))
                }
            }
            .addOnFailureListener { onFailure(it) }
    }
    fun addUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .document(user.id)
            .set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
    fun loginUser(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val firebaseUser = authResult.user
                if (firebaseUser != null) {
                    val user = User(
                        id = firebaseUser.uid,
                        name = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: "",
                        profileImageUrl = firebaseUser.photoUrl?.toString()
                    )
                    onSuccess(user)
                } else {
                    onFailure(Exception("User is null"))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
    fun register(
        email: String,
        password: String,
        name: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val firebaseUser = authResult.user
                if (firebaseUser != null) {
                    val user = User(
                        id = firebaseUser.uid,
                        name = name,
                        email = email,
                        profileImageUrl = ""
                    )

                    // שמירה בפיירסטור
                    db.collection("users")
                        .document(user.id)
                        .set(user)
                        .addOnSuccessListener {
                            onSuccess(user)
                        }
                        .addOnFailureListener { onFailure(it) }

                } else {
                    onFailure(Exception("User is null"))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun logoutUser(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        auth.signOut()
        onSuccess()
    }
    fun updateUser(
        user: User,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        db.collection("users")
            .document(user.id)
            .set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
