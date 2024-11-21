package com.example.userstory

import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.userstory.ui.theme.UserstoryTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private val appSettingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // READ_MEDIA_IMAGES 권한 상태 확인
        val isAlwaysAllow = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED

        // READ_MEDIA_VISUAL_USER_SELECTED 권한 상태 확인
        val isLimitedAccessAllow = ContextCompat.checkSelfPermission(
            this,
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
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 권한 요청 처리
        val permissionsToRequest = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> listOf()

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> listOf(
                android.Manifest.permission.READ_MEDIA_IMAGES
            )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> listOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

            else -> listOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        val requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.forEach { (permission, isGranted) ->
                Log.e("Permissions", "Permission: $permission, Granted: $isGranted")
            }
        }

        setContent {
            UserstoryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PermissionHandler(
                        permissions = permissionsToRequest.toTypedArray(),
                        requestPermissionsLauncher = { permissions ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                showSettingsDialog() // 34 이상 팝업 호출
                            } else {
                                requestPermissionsLauncher.launch(permissions)
                            }
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한 필요")
            .setMessage("제한된 액세스를 허용하려면 설정 화면에서 권한을 변경하세요.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun openAppSettings() {
        val packageName = this.packageName // Activity Context 기반
        Log.d("SettingsIntent", "Opening settings for package: $packageName")
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
        }
        appSettingsLauncher.launch(intent)
    }
}

@Composable
fun PermissionHandler(
    permissions: Array<String>,
    requestPermissionsLauncher: (Array<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var permissionRequested by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // 최초 권한 요청 트리거
    LaunchedEffect(Unit) {
        if (!permissionRequested) {
            requestPermissionsLauncher(permissions)
            permissionRequested = true
        }
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Greeting(name = "Android")

        Button(onClick = {
            accessAllMediaFilesGroupedByFolder(context)
        }) {
            Text(text = "Access Media Files")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

// 미디어 폴더/파일 접근 함수 (이미지 파일만)
fun accessAllMediaFilesGroupedByFolder(context: Context) {
    // 필요한 컬럼 지정
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 폴더 이름
        MediaStore.Images.Media.DATE_TAKEN          // 날짜
    )
    val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

    // 쿼리 실행
    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )

    val folderMap = mutableMapOf<String, MutableList<String>>()

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val folderColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        while (it.moveToNext()) {
            // 이미지 ID와 폴더 이름 추출
            val id = it.getLong(idColumn)
            val folderName = it.getString(folderColumn) ?: "Unknown"

            // 이미지 URI 생성
            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            // 폴더별로 URI 추가
            folderMap.computeIfAbsent(folderName) { mutableListOf() }.add(uri.toString())
        }
    }

    if (folderMap.isNotEmpty()) {
        folderMap.forEach { (folder, uris) ->
            Log.e("MediaAccess", "Folder: $folder, Image Count: ${uris.size}")
            uris.forEach { uri ->
                Log.e("MediaAccess", "Image URI: $uri")
            }
        }
    } else {
        Log.e("MediaAccess", "No media files found.")
    }
}