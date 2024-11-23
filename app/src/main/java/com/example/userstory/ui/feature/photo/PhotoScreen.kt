package com.example.userstory.ui.feature.photo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.userstory.R
import com.example.userstory.utils.CoilWithImageState
import com.example.userstory.utils.SaveComposableAsImage
import com.example.userstory.utils.saveBitmapToGallery

@Composable
fun PhotoScreen(
    photoViewModel: PhotoViewModel,
    navigateToMain: () -> Unit
) {
    val decoItems = List(10) { "Deco $it" } // 10개의 아이템 리스트

    // TODO Test 선택된 Image Url 상태 관리
    var selectedUrl by remember { mutableStateOf("") }
    val context = LocalContext.current

    val photoState by photoViewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f)
        ) {
            SaveComposableAsImage(
                isSaving = photoState.isSaving,
                content = {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background), // 이미지 리소스
                            contentDescription = "Selected Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        if (selectedUrl.isNotBlank()) {

                            // 간단히 SVG 이미지 로드
                            val imageLoader = ImageLoader.Builder(context)
                                .components {
                                    add(SvgDecoder.Factory()) // SVG 디코더 추가
                                }
                                .build()

                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data("https://firebasestorage.googleapis.com/v0/b/userstory-e9437.firebasestorage.app/o/deco_svg_images%2Fdeco_1.svg?alt=media&token=d729269f-72c0-499c-8da0-dc2621703ec1")
                                    .size(1024, 1024)
                                    .build(),
                                contentDescription = "Loaded Image",
                                imageLoader = imageLoader,
                                // error = painterResource(R.drawable.error),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .align(Alignment.Center)
                            )

                            // TODO 이미지 로드가 완료된 다음 전달하도록 코루틴으로 제어 필요
                            // 툴바에 버튼을 보이라고 인텐트로 전달
                            LaunchedEffect(selectedUrl) {
                                photoViewModel.handleIntent(PhotoIntent.UpdateButtonVisibility(selectedUrl.isNotBlank()))
                            }
                        }
                    }
                },
                onSave = { bitmap ->
                    saveBitmapToGallery(context, bitmap)
                    photoViewModel.handleIntent(PhotoIntent.UpdateSavingState(false))
                    // state 를 false  로 변경한 다음 메인으로 이동
                    navigateToMain()
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)
                .drawBehind {
                    drawRect(
                        color = Color.White
                    )
                }
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(), // 가로 전체 너비 사용
                horizontalArrangement = Arrangement.spacedBy(16.dp), // 아이템 간 간격
                contentPadding = PaddingValues(
                    start = 40.dp,
                    end = 40.dp,
                    top = 40.dp,
                    bottom = 30.dp
                )
            ) {
                itemsIndexed(
                    items = decoItems,
                    key = { _, demoItem -> demoItem }
                ) {_, decoItem ->
                    DecoItem(
                        decoItem
                    ) { url ->
                        selectedUrl = url
                    }
                }
            }
        }
    }
}

@Composable
fun DecoItem(
    decoItem: String,
    onClick: (String) -> Unit // 클릭 시 url 전달
) {
    CoilWithImageState { url ->
        onClick(url)
    }
}
