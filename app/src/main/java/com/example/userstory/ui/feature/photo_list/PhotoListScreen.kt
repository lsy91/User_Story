package com.example.userstory.ui.feature.photo_list

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.userstory.R
import com.example.userstory.ui.common.BaseLazyVerticalGrid

@Composable
fun PhotoListScreen(
    navigateToScreen: (String, Any?) -> Unit
) {

    // Test
    val photoList = listOf("1","2","3","4","5","6","7","8","9","10")

    BaseLazyVerticalGrid(
        items = photoList,
        key = { index -> photoList[index] },
        columns = 3, // 열 개수
        verticalSpacing = 2, // 수직 간격
        horizontalSpacing = 2, // 수평 간격
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 16.dp
            )
    ) {photoKey ->
        PhotoCard(
            photoKey = photoKey,
            navigateToScreen = navigateToScreen
        )
    }
}

@Composable
fun PhotoCard(
    photoKey: String,
    navigateToScreen: (String, Any?) -> Unit
) {
    // Test
    val context = LocalContext.current

    Card(
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                // Test
                Toast.makeText(context, photoKey, Toast.LENGTH_SHORT).show()

                // TODO 사진으로 이동
                navigateToScreen(
                    "Photo",
                    "{Arguments : Photo}"
                )
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background), // 이미지 리소스
            contentDescription = "Selectable Photo",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
    }
}