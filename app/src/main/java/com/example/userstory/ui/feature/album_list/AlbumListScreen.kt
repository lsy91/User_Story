package com.example.userstory.ui.feature.album_list

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.userstory.R
import com.example.userstory.ui.common.BaseText
import com.example.userstory.ui.theme.UserStoryBackgroundColor
import com.example.userstory.ui.theme.UserStoryCardDescriptionBackgroundColor
import com.example.userstory.ui.theme.UserStoryTabIndicatorColor

@Composable
fun AlbumListScreen(

) {
    // 탭 상태 관리
    val albumListScreenTabs = listOf("My Albums", "All Albums")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
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

        // TODO 클릭한 탭에 따라 보여줄 앨범 리스트 데이터만 분기처리. 앨범 리스트 뷰는 재활용한다.
        // 갤러리 이미지 가져오기
        // accessAllMediaFilesGroupedByFolder(context)

        val albumData = when (selectedTabIndex) {
            1 -> {
                "All Albums"
            }
            else -> {
                "My Albums"
            }
        }

        AlbumList(albumData)
    }
}

@Composable
fun AlbumList(
    albumData: String
) {
    val lazyListState = rememberLazyGridState()
    val context = LocalContext.current

    // Test
    Toast.makeText(context, albumData, Toast.LENGTH_SHORT).show()
    val albumList = listOf("1","2","3","4","5","6","7")

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 6.dp,
                vertical = 16.dp
            )
    ) {
        // TODO 사진을 완전히 불러오는 동안 Shimmer 적용 - 코루틴으로 제어
        items(
            count = albumList.size,
            key = { index -> albumList[index] } // 각 항목의 고유 키
        ) { index ->
            val album = albumList[index]
            AlbumCard()
        }
    }
}

@Composable
fun AlbumCard() {
    Card(
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // TODO 사진 리스트로 이동
            },
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // TODO 폴더의 첫번째 이미지
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // 이미지 리소스
                contentDescription = "Minimal Leaf",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawRect(
                            color = UserStoryCardDescriptionBackgroundColor
                        )
                    }
                    .padding(
                        horizontal = 8.dp,
                        vertical = 9.dp
                    )
            ) {
                BaseText(
                    text = "Album Name",
                    fontSize = 13,
                    fontWeight = 500
                )

                Spacer(modifier = Modifier.fillMaxWidth().height(3.dp))

                BaseText(
                    text = "0 Images",
                    fontSize = 11,
                    fontWeight = 500
                )
            }
        }
    }
}