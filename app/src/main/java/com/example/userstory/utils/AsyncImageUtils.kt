package com.example.userstory.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import java.io.File
import java.io.FileOutputStream

@Composable
fun CoilWithImageState(
    svgImageUrl: String,
    onImageClicked: (String) -> Unit // 클릭 시 url 을 전달하는 콜백
) {

    val context = LocalContext.current

    // SVG 이미지 로드 state 별로 각각 동작을 처리하기 위해 AsyncImagePainter 와 Image Composable 사용
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .crossfade(true)
            .components { add(SvgDecoder.Factory()) }
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .respectCacheHeaders(false)
            .build()
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(svgImageUrl)
            .size(200,200)
            .build(),
        imageLoader = imageLoader
    )

    when (painter.state) {
        is AsyncImagePainter.State.Success -> {
            // 로드 성공
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
                    .clickable {
                        onImageClicked(svgImageUrl)
                    }
            ) {
                Image(
                    painter = painter,
                    contentDescription = "Loaded",
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )
            }

        }
        is AsyncImagePainter.State.Error -> {
            // 로드 실패
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = ColorPainter(Color.Red),
                    contentDescription = "Error",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        else -> {}
    }
}

@Composable
fun SaveComposableAsImage(
    isSaving: Boolean,
    content: @Composable () -> Unit,
    onSave: (Bitmap) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(factory = { context ->
            ComposeView(context).apply {
                setContent {
                    content() // 캡처할 Composable 컨텐츠
                }
            }
        }, update = { view ->
            if (isSaving) {
                view.post {
                    val bitmap = Bitmap.createBitmap(
                        view.width,
                        view.height,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    view.draw(canvas)

                    onSave(bitmap) // 생성된 Bitmap을 콜백으로 전달
                }
            }
        })
    }
}

// 합성 사진 저장 (API Level 별로 분기처리)
fun saveBitmapToGallery(context: Context, bitmap: Bitmap, folderName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // API Level 29 이상 (Scoped Storage)
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "${System.currentTimeMillis()}_image.png")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/${folderName}")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                Toast.makeText(context, "Image saved to gallery!", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    } else {
        // API Level 29 미만 (Legacy Storage)
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val appFolder = File(picturesDir, folderName) // 하위 폴더 생성 (옵션)
        if (!appFolder.exists()) {
            appFolder.mkdirs()
        }

        val file = File(appFolder, "${System.currentTimeMillis()}_image.png")
        try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            Toast.makeText(context, "Image saved to: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }
}

// 사용자 지정 사진 저장 (API Level 별로 분기처리) - MediaStore API 로 조회할 수 있게 새로운 폴더 & 파일명으로 저장
fun saveUriToAppSpecificFolder(context: Context, sourceUris: List<Uri>, folderName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver = context.contentResolver

        sourceUris.forEachIndexed { index, sourceUri ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "${System.currentTimeMillis()}_image_$index.png") // 저장할 파일 이름
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png") // MIME 타입
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/$folderName" // 앱 전용 폴더 이름
                )
            }

            val destUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            if (destUri != null) {
                try {
                    resolver.openInputStream(sourceUri)?.use { inputStream ->
                        resolver.openOutputStream(destUri)?.use { outputStream ->
                            inputStream.copyTo(outputStream) // 데이터 복사
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // 복사 실패 시 저장된 URI 삭제
                    resolver.delete(destUri, null, null)
                }
            } else {
                Toast.makeText(context, "Failed to save image from $sourceUri", Toast.LENGTH_SHORT).show()
            }
        }

        Toast.makeText(context, "Images saved to $folderName folder!", Toast.LENGTH_SHORT).show()
    } else {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "${System.currentTimeMillis()}_image.png") // 저장할 파일 이름
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png") // MIME 타입
            put(
                MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/MyAppPrivateFolder" // 앱 전용 폴더 이름
            )
        }

        val resolver = context.contentResolver
        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        Toast.makeText(context, "Images saved to $folderName folder!", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun AlbumNameInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "앨범명 입력") },
        text = {
            Column {
                Text(text = "저장할 앨범명을 입력해주세요:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("앨범명") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(text)
                },
                enabled = text.isNotBlank()
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}