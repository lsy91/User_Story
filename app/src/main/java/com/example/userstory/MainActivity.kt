package com.example.userstory

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.userstory.utils.PermissionHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var permissionHelper: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionHelper = PermissionHelper(this)

        lifecycleScope.launch {
            // 권한 요청이 완료될 때까지 UI 를 그리지 않고 대기
            val hasPermissions = handlePermissions()

            if (hasPermissions) {
                setContent {
                    UserStoryApp()
                }
            } else {
                // 권한 요청 Dialog 띄우기
                permissionHelper.showSettingsDialog(this@MainActivity)
            }
        }
    }

    private suspend fun handlePermissions(): Boolean {
        val permissionsToRequest = when {
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

        return permissionHelper.requestPermissionsSuspend(this@MainActivity, permissionsToRequest)
    }
}