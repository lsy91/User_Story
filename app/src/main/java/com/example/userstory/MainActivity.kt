package com.example.userstory

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.userstory.utils.PermissionHelper

class MainActivity : ComponentActivity() {

    private lateinit var permissionHelper: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 권한 처리
        permissionHelper = PermissionHelper(this)
        handlePermissions()

        setContent {
            UserStoryApp()
        }
    }

    private fun handlePermissions() {
        val permissionsToRequest =
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> emptyList()

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> listOf(
                    android.Manifest.permission.READ_MEDIA_IMAGES
                )

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> listOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )

                else -> listOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
           }

        permissionHelper.requestPermissions(this@MainActivity, permissionsToRequest)
    }
}