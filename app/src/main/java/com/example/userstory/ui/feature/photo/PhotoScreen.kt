package com.example.userstory.ui.feature.photo

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.userstory.ui.common.BaseShimmer
import com.example.userstory.utils.CoilWithImageState
import com.example.userstory.utils.SaveComposableAsImage
import com.example.userstory.utils.saveBitmapToGallery

@Composable
fun PhotoScreen(
    selectedPhoto: String?,
    photoViewModel: PhotoViewModel,
    photoState: PhotoState,
    navigateToMain: () -> Unit
) {
    val shimmer = photoViewModel.provideShimmer()

    // Firebase 에서 이미지 파일 가져오기
    LaunchedEffect(Unit) {
        photoViewModel.handleIntent(PhotoIntent.GetDecoItem)
    }

    // 선택된 Image Url 상태 관리
    var selectedDecoItemUrl by remember { mutableStateOf("") }
    val context = LocalContext.current

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
                    PhotoEditorContent(
                        context = context,
                        selectedPhoto = selectedPhoto,
                        selectedDecoItemUrl = selectedDecoItemUrl,
                        photoViewModel = photoViewModel
                    )
                },
                onSave = { bitmap ->
                    // 비트맵 변환 및 저장 로직
                    val softwareBitmap = if (bitmap.config == Bitmap.Config.HARDWARE) {
                        bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    } else {
                        bitmap
                    }

                    saveBitmapToGallery(context, softwareBitmap, "UserStory_mod")

                    photoViewModel.handleIntent(PhotoIntent.UpdateSavingState(false))
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
            if (photoState.isDecoItemLoading) {
                // 로딩 중 Shimmer를 표시
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 40.dp, top = 40.dp, bottom = 30.dp), // 여백 설정
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // 아이템 간 간격 설정
                ) {
                    repeat(5) { // Shimmer를 3개 반복
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .wrapContentHeight()
                        ) {
                            BaseShimmer(
                                shimmer = shimmer,
                                contentHeight = 100
                            )
                        }

                    }
                }
            } else {
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
                    if (photoState.decoItems.isEmpty()) {
                        // 데이터가 없을 때 고정된 개수의 Shimmer 표시
                        items(5) {
                            BaseShimmer(
                                shimmer = shimmer,
                                contentHeight = 200 // Shimmer 높이 설정
                            )
                        }
                    } else {
                        // 데이터가 있을 때 실제 아이템 표시
                        itemsIndexed(photoState.decoItems) { _, decoItem ->
                            DecoItem(
                                svgImageUrl = decoItem.svgImageUrl
                            ) { url ->
                                selectedDecoItemUrl = url.replace("_border", "")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DecoItem(
    svgImageUrl: String,
    onClick: (String) -> Unit // 클릭 시 url 전달
) {
    CoilWithImageState(svgImageUrl) { url ->
        onClick(url)
    }
}

@Composable
fun PhotoEditorContent(
    context: Context,
    selectedPhoto: String?,
    selectedDecoItemUrl: String,
    photoViewModel: PhotoViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(selectedPhoto)
                    .allowHardware(false)
                    .build()
            ),
            contentDescription = "Selected Photo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )

        if (selectedDecoItemUrl.isNotBlank()) {

            // SVG 이미지 로드 설정
            val imageLoader = ImageLoader.Builder(context)
                .components {
                    add(SvgDecoder.Factory()) // SVG 디코더 추가
                }
                .allowHardware(false)
                .build()

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(selectedDecoItemUrl)
                    .size(1024, 1024)
                    .allowHardware(false)
                    .build(),
                contentDescription = "Loaded Image",
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
            )

            // 툴바 버튼 보이기
            LaunchedEffect(selectedDecoItemUrl) {
                photoViewModel.handleIntent(PhotoIntent.UpdateButtonVisibility(selectedDecoItemUrl.isNotBlank()))
            }
        }
    }
}
