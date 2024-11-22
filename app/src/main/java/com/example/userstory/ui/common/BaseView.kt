package com.example.userstory.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
            .fillMaxWidth()
            .wrapContentHeight()
    )
}