package com.example.userstory.ui.feature.album_list

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.userstory.R
import com.example.userstory.ui.common.BaseLazyVerticalGrid
import com.example.userstory.ui.common.BaseShimmer
import com.example.userstory.ui.common.BaseText
import com.example.userstory.ui.theme.UserStoryBackgroundColor
import com.example.userstory.ui.theme.UserStoryCardDescriptionBackgroundColor
import com.example.userstory.ui.theme.UserStoryTabIndicatorColor

@Composable
fun AlbumListScreen(
    navigateToScreen: (String, Any?) -> Unit
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
        // CommonUtils.accessAllMediaFilesGroupedByFolder(context)

        val albumData = when (selectedTabIndex) {
            1 -> {
                "All Albums"
            }
            else -> {
                "My Albums"
            }
        }

        AlbumList(
            albumData = albumData,
            navigateToScreen = navigateToScreen
        )
    }
}

@Composable
fun AlbumList(
    albumData: String,
    albumListViewModel: AlbumListViewModel = hiltViewModel(),
    navigateToScreen: (String, Any?) -> Unit
) {
    val context = LocalContext.current
    val shimmer = albumListViewModel.provideShimmer()

    // Test
    val albumList = listOf("1","2","3","4","5","6","7")

    BaseLazyVerticalGrid(
        items = albumList, // 데이터 리스트
        key = { index -> albumList[index] }, // 고유 키
        columns = 2, // 열 개수
        verticalSpacing = 8, // 수직 간격
        horizontalSpacing = 8, // 수평 간격
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 6.dp,
                vertical = 16.dp
            )
    ) { albumKey ->
//        AlbumCard(
//            albumKey = albumKey,
//            navigateToScreen = navigateToScreen // 아이템 UI 정의
//        )
        BaseShimmer(
            shimmer = shimmer
        )
    }
}

@Composable
fun AlbumCard(
    albumKey: String,
    navigateToScreen: (String, Any?) -> Unit
) {

    // Test
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                // Test
                Toast
                    .makeText(context, albumKey, Toast.LENGTH_SHORT)
                    .show()

                // TODO 사진 리스트로 이동
                navigateToScreen(
                    "PhotoList",
                    "{Arguments : PhotoList}"
                )
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
                contentDescription = "First Photo",
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

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp))

                BaseText(
                    text = "0 Images",
                    fontSize = 11,
                    fontWeight = 500
                )
            }
        }
    }
}