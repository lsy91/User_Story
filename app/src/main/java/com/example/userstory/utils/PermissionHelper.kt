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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class PermissionHelper(activity: ComponentActivity) {

    // 설정 화면에서 권한 처리를 하고 돌아와서 허용 여부를 판단하는 런쳐
    private val appSettingsPermissionResultLauncher = activity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // READ_MEDIA_IMAGES 권한 상태 확인
            val isAlwaysAllow = ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED

            // READ_MEDIA_VISUAL_USER_SELECTED 권한 상태 확인
            val isLimitedAccessAllow = ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            ) == PackageManager.PERMISSION_GRANTED

            when {
                isAlwaysAllow && isLimitedAccessAllow -> {
                    Log.e("PermissionCheck", "Always Allow: Media access granted.")
                }
                !isAlwaysAllow && isLimitedAccessAllow -> {
                    Log.e("PermissionCheck", "Limited Access Allow: Restricted media access granted.")
                }
                else -> {
                    Log.e("PermissionCheck", "Limited Access or Denied.")

                    // 또 거부하면 다시 설정화면으로 보낸다.
                    showSettingsDialog(activity)
                }
            }
        }

        else {
            // API Level 34 미만은 버전별로 체크해야할 권한 분기처리
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

            // 권한 상태 확인
            val deniedPermissions = permissionsToCheck.filter { permission ->
                ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
            }

            if (deniedPermissions.isEmpty()) {
                Log.e("PermissionCheck", "All permissions granted.")
            } else {
                Log.e("PermissionCheck", "Permissions denied: $deniedPermissions")
                // 또 거부했으면 다시 안내 팝업 띄운다. 권한 재요청
                showSettingsDialog(activity)
            }
        }
    }

    // 시스템 대화상자 허용 여부를 판단하는 런쳐
    private val systemBoxPermissionsLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // 하나라도 허용되지 않은 권한이 있는지 확인
        val hasDeniedPermission = permissions.any { (_, isGranted) -> !isGranted }

        if (hasDeniedPermission) {
            // 하나라도 거부된 권한이 있는 경우 권한이 필요하다고 안내 띄워준다.
            showSettingsDialog(activity)
        } else {
            // 모든 권한이 허용된 경우 처리
            Log.d("Permissions", "All permissions granted.")
        }
    }

    /**
     * 권한 요청 전에 상태를 확인한 후 부족한 권한만 요청합니다.
     */
    fun requestPermissions(context: Context, permissions: List<String>) {

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                val apiLevel34PermissionList = listOf(
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                )

                // 둘 다 허용이 안 됐는지 확인
                val allDenied = apiLevel34PermissionList.all {
                    ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
                }

                // 둘 다 허용이 안된 경우만 팝업 안내 문구 띄운다.
                if (allDenied) {
                    showSettingsDialog(context)
                }
            }
            else -> {
                // 부족한 권한 필터링
                val requiredPermissions = permissions.filter { permission ->
                    ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
                }

                if (requiredPermissions.isNotEmpty()) {
                    systemBoxPermissionsLauncher.launch(requiredPermissions.toTypedArray())
                }
            }
        }
    }

    // 권한 거부시 띄워지는 안내 팝업 (뒤로가기로 닫기 불가)
    private fun showSettingsDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("권한 필요")
            .setMessage("제한된 액세스를 허용하려면 설정 화면에서 권한을 변경하세요.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                openAppSettings(context)
            }
            .setCancelable(false)
            .show()
    }

    // 설정 화면으로 보내서 허용 여부 확인 런쳐 실행한다.
    private fun openAppSettings(context: Context) {
        val packageName = context.packageName // Activity Context 기반
        Log.d("SettingsIntent", "Opening settings for package: $packageName")
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
        }

        // 허용 여부 확인 런쳐 실행
        appSettingsPermissionResultLauncher.launch(intent)
    }
}