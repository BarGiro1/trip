package com.example.kot_trip.base

import android.app.Application
import android.content.Context

class App: Application() {

    object Globals {
        var context: Context? = null
        var userId: String? = null
    }

    override fun onCreate() {
        super.onCreate()
        Globals.context = applicationContext
    }

    fun setUserId(userId: String?) {
        val sharedPref = Globals.context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putString("userId", userId)
        editor?.apply()
        Globals.userId = userId
    }

    fun getUserId(): String? {
        if (Globals.userId == null) {
            val sharedPref = Globals.context?.getSharedPreferences("user", Context.MODE_PRIVATE)
            Globals.userId = sharedPref?.getString("userId", null)
        }
        return Globals.userId
    }

    fun logout() {
        setUserId(null)
    }
}