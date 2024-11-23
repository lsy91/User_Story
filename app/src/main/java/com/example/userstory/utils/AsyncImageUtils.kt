package com.example.userstory.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest

@Composable
fun CoilWithImageState(
    onImageClicked: (String) -> Unit // 클릭 시 url 을 전달하는 콜백
) {

    val context = LocalContext.current

    // SVG 이미지 로드 state 별로 각각 동작을 처리하기 위해 AsyncImagePainter 와 Image Composable 사용
    val decoImageUrl = "https://firebasestorage.googleapis.com/v0/b/userstory-e9437.firebasestorage.app/o/deco_svg_images%2Fdeco_border_1.svg?alt=media&token=097aa6dc-15b2-4608-a8fb-b4fd7e14c5a5"

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
            .data(decoImageUrl)
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
                        // TODO
                        onImageClicked(decoImageUrl)
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
                    .clickable {
                        // TODO
                        onImageClicked(decoImageUrl)
                    }
            ) {
                Image(
                    painter = ColorPainter(Color.Red),
                    contentDescription = "Error",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        else -> {
            // 로딩 중
            Image(
                painter = ColorPainter(Color.Gray),
                contentDescription = "Loading",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
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

fun saveBitmapToGallery(context: Context, bitmap: Bitmap, filename: String = "image.png") {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
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
}