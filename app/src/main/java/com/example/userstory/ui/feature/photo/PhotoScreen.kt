package com.example.userstory.ui.feature.photo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.userstory.R
import com.example.userstory.utils.CoilWithImageState

@Composable
fun PhotoScreen(
    navigateToMain: () -> Unit,
) {
    val decoItems = List(10) { "Deco $it" } // 10개의 아이템 리스트

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // 이미지 리소스
                contentDescription = "Selected Photo",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
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
                    DecoItem(decoItem)
                }
            }
        }
    }
}

@Composable
fun DecoItem(
    decoItem: String
) {
    CoilWithImageState()
}
