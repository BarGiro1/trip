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
}