package com.example.userstory.ui.feature.album_list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.userstory.ui.common.BaseText
import com.example.userstory.ui.theme.UserStoryBackgroundColor
import com.example.userstory.ui.theme.UserStoryTabIndicatorColor

@Composable
fun AlbumListScreen(

) {
    // 탭 상태 관리
    val albumListScreenTabs = listOf("Favorite Albums", "All Albums")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = UserStoryBackgroundColor,
        indicator = { tabPositions ->
            SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]), // 선택된 탭 위치
                color = UserStoryTabIndicatorColor
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(
                elevation = 5.dp
            )
    ) {
        albumListScreenTabs.forEachIndexed { tabIndex, tabTitle ->
            Tab(
                selected = selectedTabIndex == tabIndex,
                onClick = { selectedTabIndex = tabIndex },
                text = {
                    BaseText(
                        text = tabTitle,
                        fontSize = 13,
                        fontWeight = 700
                    )
                }
            )
        }
    }
}