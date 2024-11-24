package com.example.userstory.ui.feature.album_list

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.userstory.ui.common.BaseLazyVerticalGrid
import com.example.userstory.ui.common.BaseShimmer
import com.example.userstory.ui.common.BaseText
import com.example.userstory.ui.feature.album_list.bean.Album
import com.example.userstory.ui.theme.UserStoryBackgroundColor
import com.example.userstory.ui.theme.UserStoryCardDescriptionBackgroundColor
import com.example.userstory.ui.theme.UserStoryTabIndicatorColor
import com.facebook.shimmer.Shimmer

@Composable
fun AlbumListScreen(
    albumListViewModel: AlbumListViewModel,
    albumListState: AlbumListState,
    navigateToScreen: (String, Any?) -> Unit
) {
    // shimmer
    val shimmer = albumListViewModel.provideShimmer()

    // 최초에 MyAlbums 리스트부터 가져옴.
    LaunchedEffect(Unit) {
        albumListViewModel.handleIntent(AlbumListIntent.LoadMyAlbums)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // 탭 상태 관리
        val albumListScreenTabs = listOf("My Albums", "All Albums")
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
                    onClick = {
                        selectedTabIndex = tabIndex

                        val intent = if (selectedTabIndex == 0) {
                            AlbumListIntent.LoadMyAlbums
                        } else {
                            AlbumListIntent.LoadAllAlbums
                        }

                        albumListViewModel.handleIntent(intent)
                    },
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

        AlbumList(
            albumListState = albumListState,
            shimmer = shimmer,
            navigateToScreen = navigateToScreen
        )
    }
}

@Composable
fun AlbumList(
    albumListState: AlbumListState,
    shimmer: Shimmer,
    navigateToScreen: (String, Any?) -> Unit
) {
    BaseLazyVerticalGrid(
        items = albumListState.albums, // 앨범 리스트
        key = { index -> albumListState.albums[index].name }, // 앨범명으로 고유키 설정
        columns = 2, // 열 개수
        verticalSpacing = 8, // 수직 간격
        horizontalSpacing = 8, // 수평 간격
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 6.dp,
                vertical = 16.dp
            )
    ) {album ->
        AlbumCard(
            isAlbumListLoading = albumListState.isAlbumListLoading,
            album = album,
            shimmer = shimmer,
            navigateToScreen = navigateToScreen // 아이템 UI 정의
        )
    }
}

@Composable
fun AlbumCard(
    isAlbumListLoading: Boolean,
    album: Album,
    shimmer: Shimmer,
    navigateToScreen: (String, Any?) -> Unit
) {
    Card(
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // 사진 리스트로 이동
                navigateToScreen(
                    "PhotoList",
                    album
                )
            },
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (isAlbumListLoading) {
                BaseShimmer(
                    shimmer = shimmer,
                    contentHeight = 200
                )
            }
            else {
                // 각 폴더의 첫번째 이미지
                Image(
                    painter = rememberAsyncImagePainter(album.photos[0]),
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
                        text = album.name,
                        fontSize = 13,
                        fontWeight = 500
                    )

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp))

                    BaseText(
                        text = "${album.photoCount}",
                        fontSize = 11,
                        fontWeight = 500
                    )
                }
            }
        }
    }
}