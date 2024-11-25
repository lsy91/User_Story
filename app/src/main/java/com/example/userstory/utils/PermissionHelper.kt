package com.example.userstory.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PermissionHelper @Inject constructor() {
    // 앱 최초 실행 후 권한을 받아올 때 까지 UI 를 그리지 않게 suspend 시키는 함수
    suspend fun requestPermissionsSuspend(activity: ComponentActivity, permissions: List<String>): Boolean {
        val requiredPermissions = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
        }

        if (requiredPermissions.isEmpty()) return true // 모든 권한이 이미 허용된 경우

        return suspendCoroutine { continuation ->
            val launcher = activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { results ->
                // 권한 요청 결과 반환
                val allGranted = results.all { it.value }

                if (allGranted) {
                    continuation.resume(true)
                } else {
                    continuation.resume(false)
                }
            }
            launcher.launch(requiredPermissions.toTypedArray())
        }
    }

    fun handleAllAlbumPermission(
        activity: ComponentActivity,
        appSettingsLauncher: ActivityResultLauncher<Intent>,
        onSuccess: () -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // API Level 34 이상
            val isAlwaysAllow = ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED

            val isLimitedAccessAllow = ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            ) == PackageManager.PERMISSION_GRANTED

            if (isAlwaysAllow && isLimitedAccessAllow) {
                // 권한 충족 시 성공 콜백 호출
                onSuccess()
            } else {
                // 권한 부족 시 설정 화면으로 유도
                showSettingsDialog(activity, "미디어 액세스 권한을 항상 허용으로 설정해주세요.", appSettingsLauncher)
            }
        } else {
            // API Level 34 미만
            val permissionsToRequest = when {
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

            val deniedPermissions = permissionsToRequest.filter {
                ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
            }

            if (deniedPermissions.isEmpty()) {
                // 권한 충족 시 성공 콜백 호출
                onSuccess()
            } else {
                // 권한 부족 시 설정 화면으로 유도
                showSettingsDialog(activity, "미디어 액세스 권한을 항상 허용으로 설정해주세요.", appSettingsLauncher)
            }
        }
    }

    // 권한 거부시 띄워지는 안내 팝업 (뒤로가기로 닫기 불가)
    fun showSettingsDialog(context: Context, message: String, launcher: ActivityResultLauncher<Intent>) {
        AlertDialog.Builder(context)
            .setTitle("권한 필요")
            .setMessage(message)
            .setPositiveButton("설정으로 이동") { _, _ ->
                openAppSettings(context, launcher)
            }
            .setCancelable(false)
            .show()
    }

    // 설정 화면으로 보내서 허용 여부 확인 런쳐 실행한다.
    private fun openAppSettings(context: Context, launcher: ActivityResultLauncher<Intent>) {
        val packageName = context.packageName // Activity Context 기반
        Log.d("SettingsIntent", "Opening settings for package: $packageName")
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
        }

        // 허용 여부 확인 런쳐 실행
        launcher.launch(intent)
    }
}