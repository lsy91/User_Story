package com.example.userstory.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.userstory.ui.theme.RobotoRegular
import com.example.userstory.ui.theme.UserStoryFontColor

@Composable
fun BaseText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Int,
    fontColor: Color = UserStoryFontColor,
    fontWeight: Int = 400,
    fontFamily: FontFamily = RobotoRegular
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = fontSize.sp,
            color = fontColor,
            fontWeight = FontWeight(fontWeight),
            fontFamily = fontFamily,
            platformStyle = PlatformTextStyle(includeFontPadding = false)
        ),
        modifier = modifier
    )
}

@Composable
fun <T> BaseLazyVerticalGrid(
    items: List<T>, // 데이터 리스트
    key: ((index: Int) -> Any)?, // 각 항목의 고유 키
    columns: Int = 2, // 기본 열 개수
    verticalSpacing: Int = 8, // 아이템 간 수직 간격
    horizontalSpacing: Int = 8, // 아이템 간 수평 간격
    modifier: Modifier = Modifier,
    itemContent: @Composable LazyGridItemScope.(item: T) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing.dp),
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing.dp),
        modifier = modifier,
        state = rememberLazyGridState()
    ) {
        items(
            count = items.size,
            key = key
        ) { index ->
            val item = items[index]
            itemContent(item)
        }
    }
}