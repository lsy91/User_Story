package com.example.userstory.ui.feature.photo_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.userstory.ui.common.BaseLazyVerticalGrid
import com.example.userstory.ui.common.BaseShimmer
import com.example.userstory.ui.feature.album_list.bean.Album
import com.facebook.shimmer.Shimmer

@Composable
fun PhotoListScreen(
    album: Album?,
    photoListState: PhotoListState,
    photoListViewModel: PhotoListViewModel,
    navigateToScreen: (String, Any?) -> Unit
) {
    // shimmer
    val shimmer = photoListViewModel.provideShimmer()

    LaunchedEffect(Unit) {
        photoListViewModel.handleIntent(PhotoListIntent.LoadPhotoList, album)
        photoListViewModel.handleIntent(PhotoListIntent.ChangeToolbarTitle(album?.name))
    }

    BaseLazyVerticalGrid(
        items = photoListState.photoList,
        key = { index -> photoListState.photoList[index] },
        columns = 3, // 열 개수
        verticalSpacing = 2, // 수직 간격
        horizontalSpacing = 2, // 수평 간격
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 16.dp
            )
    ) {photoUri ->
        PhotoCard(
            isPhotoListLoading = photoListState.isPhotoListLoading,
            photoUri = photoUri,
            shimmer = shimmer,
            navigateToScreen = navigateToScreen
        )
    }
}

@Composable
fun PhotoCard(
    isPhotoListLoading: Boolean,
    photoUri: String,
    shimmer: Shimmer,
    navigateToScreen: (String, Any?) -> Unit
) {
    Card(
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // 사진으로 이동
                navigateToScreen(
                    "Photo",
                    photoUri
                )
            }
    ) {
        if (isPhotoListLoading) {
            BaseShimmer(
                shimmer = shimmer,
                contentHeight = 100
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(photoUri),
                contentDescription = "Selectable Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        }
    }
}