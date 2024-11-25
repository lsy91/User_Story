package com.example.userstory.ui.feature.photo_picker

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.userstory.ui.common.BaseLazyVerticalGrid
import com.example.userstory.ui.common.BaseText
import com.example.userstory.ui.theme.UserStoryFontColor
import com.example.userstory.ui.theme.UserStoryOverlayButtonBackgroundColor
import com.example.userstory.ui.theme.UserStoryOverlayTextColor
import com.example.userstory.utils.AlbumNameInputDialog
import com.example.userstory.utils.PickContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PhotoPickerScreen(
    photoPickerViewModel: PhotoPickerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val photoPickerState = photoPickerViewModel.state.collectAsState()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = PickContent()
    ) { uri ->
        uri?.let {
            Log.d("PhotoPicker", "Selected URI: $uri")

            photoPickerViewModel.handleIntent(PhotoPickerIntent.SetAccessablePhotos(uri))
        }
    }

    if (photoPickerState.value.showFolderNameInputDialog) {
        AlbumNameInputDialog(
            onDismiss = {
                photoPickerViewModel.handleIntent(PhotoPickerIntent.ShowFolderNameInputDialog(false))
            },
            onConfirm = { albumName ->
                photoPickerViewModel.handleIntent(PhotoPickerIntent.SaveAccessedPhotos(photoPickerState.value.accessedPhotoList, albumName))
                photoPickerViewModel.handleIntent(PhotoPickerIntent.ShowFolderNameInputDialog(false))
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.9f)
        ) {
            BaseLazyVerticalGrid(
                items = photoPickerState.value.accessablePhotoList,
                key = { index -> photoPickerState.value.accessablePhotoList[index] },
                columns = 3
            ) { photoUri ->
                Card(
                    shape = RectangleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(photoUri),
                        contentDescription = "Accessed Photo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
        ) {
            Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BaseText(
                    text = "더 많은 사진 가져오기",
                    fontSize = 13,
                    fontColor = UserStoryFontColor,
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            photoPickerLauncher.launch(Unit)
                        }
                )

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UserStoryOverlayButtonBackgroundColor
                    ),
                    onClick = {
                        // 읽기 / 쓰기 권한 부여
                        val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION

                        // 권한 부여가 완료된 후에 다음 처리
                        CoroutineScope(Dispatchers.IO).launch {
                            photoPickerState.value.accessablePhotoList.forEach { accessablePhotoUri ->
                                context.contentResolver.takePersistableUriPermission(accessablePhotoUri, flag)
                            }
                        }

                        // ViewModel 에 권한 부여 완료된 이미지 URI 리스트 저장
                        photoPickerViewModel.handleIntent(PhotoPickerIntent.SetAccessedPhotos(photoPickerState.value.accessablePhotoList))

                        // 앨범명 입력 다이얼로그 호출
                        photoPickerViewModel.handleIntent(PhotoPickerIntent.ShowFolderNameInputDialog(true))
                    }
                ) {
                    BaseText(
                        text = "앨범에 추가",
                        fontSize = 12,
                        fontColor = UserStoryOverlayTextColor,
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }
            }
        }
    }
}