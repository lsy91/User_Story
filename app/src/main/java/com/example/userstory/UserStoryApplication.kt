package com.example.userstory

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

/**
 * Application
 */
@HiltAndroidApp
class UserStoryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this@UserStoryApplication)
    }
}