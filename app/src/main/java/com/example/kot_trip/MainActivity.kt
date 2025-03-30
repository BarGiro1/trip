package com.example.kot_trip

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kot_trip.base.App
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)
        guardUserId()
    }

    fun guardUserId() {
        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNav.setupWithNavController(navController)

        // logout on item click, the other keep the same
        App().getUserId().let {
            if (it != null) {
                Log.d("MainActivity", "User is logged in: $it")
                navController.navigate(R.id.homeFragment)
            }
            else {
                Log.d("MainActivity", "User is not logged in")
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    bottomNav.visibility = when (destination.id) {
                        R.id.loginFragment, R.id.signUpFragment -> android.view.View.GONE
                        else -> android.view.View.VISIBLE
                    }
                }
                bottomNav.setupWithNavController(navController)
            }
        }
    }
}
