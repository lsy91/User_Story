package com.example.userstory.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest

@Composable
fun CoilWithImageState() {

    val context = LocalContext.current

    // SVG 이미지 로드 state 별로 각각 동작을 처리하기 위해 AsyncImagePainter 와 Image Composable 사용
    val decoImageUrl = "https://firebasestorage.googleapis.com/v0/b/userstory-e9437.firebasestorage.app/o/deco_svg_images%2Fdeco_1.svg?alt=media&token=45808843-f216-406b-b2e8-d5efae088265"

    val imageLoader = ImageLoader.Builder(context)
        .components { add(SvgDecoder.Factory()) } // SVG 디코더 추가
        .memoryCachePolicy(CachePolicy.ENABLED) // 메모리 캐싱 활성화
        .diskCachePolicy(CachePolicy.ENABLED) // 디스크 캐싱 활성화
        .build()

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(decoImageUrl)
            .size(coil.size.Size.ORIGINAL)
            .networkCachePolicy(CachePolicy.DISABLED) // 네트워크 요청 비활성화
            .build(),
        imageLoader = imageLoader
    )

    val state = painter.state

    Box(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
    ) {
        when (state) {
            is AsyncImagePainter.State.Success -> {
                // 로드 성공
                Image(
                    painter = painter,
                    contentDescription = "Loaded",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            is AsyncImagePainter.State.Error -> {
                // 로드 실패
                Image(
                    painter = ColorPainter(Color.Red),
                    contentDescription = "Error",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
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
}