package com.example.userstory

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.userstory.utils.PermissionHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var permissionHelper: PermissionHelper

    private val appSettingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (handleAppSettingsResult()) {
            initApp()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            // 권한 요청이 완료될 때까지 UI 를 그리지 않고 대기
            val hasPermissions = handlePermissions(appSettingsLauncher)

            if (hasPermissions) {
                initApp()
            } else {
                // 권한 요청 Dialog 띄우기
                permissionHelper.showSettingsDialog(this@MainActivity, "제한된 액세스를 허용하려면 설정 화면에서 권한을 변경하세요.", appSettingsLauncher)
            }
        }
    }

    private fun initApp() {
        // 권한 승인이 완료되면 앱 초기화
        setContent {
            UserStoryApp(
                permissionHelper = permissionHelper,
                appSettingsLauncher = appSettingsLauncher,
            )
        }
    }

    private fun handleAppSettingsResult(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val isAlwaysAllow = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED

            val isLimitedAccessAllow = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            ) == PackageManager.PERMISSION_GRANTED

            when {
                isAlwaysAllow && isLimitedAccessAllow -> {
                    Log.e("PermissionCheck", "Always Allow: Media access granted.")
                    return true
                }
                !isAlwaysAllow && isLimitedAccessAllow -> {
                    Log.e("PermissionCheck", "Limited Access Allow: Restricted media access granted.")
                    return true
                }
                else -> {
                    Log.e("PermissionCheck", "Limited Access or Denied.")
                    permissionHelper.showSettingsDialog(
                        this,
                        "미디어 액세스 권한을 항상 허용으로 설정해주세요.",
                        appSettingsLauncher
                    )
                    return false
                }
            }
        } else {
            // API Level 34 미만의 권한 확인 로직
            val permissionsToCheck = when {
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

            val deniedPermissions = permissionsToCheck.filter {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }

            if (deniedPermissions.isEmpty()) {
                Log.e("PermissionCheck", "All permissions granted.")
                return true
            } else {
                Log.e("PermissionCheck", "Permissions denied: $deniedPermissions")
                permissionHelper.showSettingsDialog(
                    this,
                    "미디어 액세스 권한을 항상 허용으로 설정해주세요.",
                    appSettingsLauncher
                )

                return false
            }
        }
    }

    private suspend fun handlePermissions(appSettingsLauncher: ActivityResultLauncher<Intent>): Boolean {
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