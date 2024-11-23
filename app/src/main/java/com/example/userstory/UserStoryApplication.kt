package com.example.userstory

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application
 */
@HiltAndroidApp
class UserStoryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}