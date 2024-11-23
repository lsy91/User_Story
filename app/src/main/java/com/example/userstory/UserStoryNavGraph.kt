package com.example.userstory

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.userstory.ui.common.BaseText
import com.example.userstory.ui.feature.album_list.AlbumListScreen
import com.example.userstory.ui.feature.photo.PhotoIntent
import com.example.userstory.ui.feature.photo.PhotoScreen
import com.example.userstory.ui.feature.photo.PhotoViewModel
import com.example.userstory.ui.feature.photo_list.PhotoListScreen
import com.example.userstory.ui.theme.RobotoRegular
import com.example.userstory.ui.theme.UserStoryBackgroundColor
import com.example.userstory.ui.theme.UserStoryFontColor
import com.example.userstory.ui.theme.UserStoryOverlayButtonBackgroundColor
import com.example.userstory.ui.theme.UserStoryOverlayTextColor
import com.example.userstory.utils.CommonUtils
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserStoryNavGraph(
    navController: NavHostController,
    navigateToScreen: (String, Any?) -> Unit,
    navigateToMain: () -> Unit,
    coroutineScope: CoroutineScope,
    photoViewModel: PhotoViewModel = hiltViewModel() // Inject ViewModel
) {

    val photoState by photoViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: ""

            TopAppBar(
                title = {
                    BaseText(
                        text = when {
                            currentRoute.contains("AlbumList") -> "ALBUM LIST"
                            currentRoute.contains("PhotoList") -> "앨범명"
                            currentRoute.contains("Photo") -> "앨범명"
                            else -> ""
                        },
                        fontSize = 18,
                        fontColor = UserStoryFontColor,
                        fontWeight = 700,
                        fontFamily = RobotoRegular
                    )
                },
                actions = {
                    // 현재 화면이 PhotoScreen 이면서 버튼을 보여야 하는 경우인지 체크
                    if (currentRoute.contains("Photo") && photoState.isButtonVisible) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = UserStoryOverlayButtonBackgroundColor
                            ),
                            onClick = {
                                // 사진 저장
                                photoViewModel.handleIntent(PhotoIntent.UpdateSavingState(true))
                            },
                            contentPadding = PaddingValues(horizontal = 30.dp, vertical = 8.dp),
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(end = 10.dp)
                        ) {
                            BaseText(
                                text = "Overlay",
                                fontSize = 12,
                                fontColor = UserStoryOverlayTextColor,
                                modifier = Modifier
                                    .wrapContentSize()
                            )
                        }
                    }
                },
                navigationIcon = {
                    // 사진 리스트 화면, 사진 화면은 뒤로가기 아이콘을 넣어준다.
                    if (!currentRoute.contains("AlbumList")) {
                        // 뒤로가기 아이콘 추가
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "backward",
                                tint = UserStoryFontColor
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = UserStoryBackgroundColor
                ),
                modifier =
                    // 앨범 리스트를 제외한 나머지 화면들은 툴바에 그림자효과를 준다.
                    if (!currentRoute.contains("AlbumList")) {
                        Modifier.shadow(elevation = 3.dp)
                    } else {
                        Modifier
                    }

            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawRect(
                        color = UserStoryBackgroundColor
                    )
                }
        ) {
            NavHost(
                navController = navController,
                startDestination = "AlbumList",
                modifier = Modifier
                    .drawBehind {
                        drawRect(
                            color = UserStoryBackgroundColor
                        )
                    }
                    .padding(innerPadding)
            ) {
                composable(
                    route = ScreenRoute.AlbumListRoute.route
                ) {
                    AlbumListScreen(
                        navigateToScreen = navigateToScreen
                    )
                }

                composable(
                    route = ScreenRoute.PhotoListRoute.route + "/{PhotoList}",
                    arguments = listOf(
                        navArgument("PhotoList") {
                            type = NavType.StringType
                            defaultValue = ""
                        }
                    )
                ) { backStackEntry ->

                    // TODO Test
                    // 선택한 앨범에서 전달받은 PhotoList 가져오기
                    val encodedPhotoListData = backStackEntry.arguments?.getString("PhotoList") ?: ""
                    val decodedPhotoList = Uri.decode(encodedPhotoListData)

                    // JSON을 객체로 변환
                    val photoListArguments = CommonUtils.convertJSONToObj<String>(decodedPhotoList, String::class.java)

                    Log.e("sy.lee", photoListArguments.toString())

                    PhotoListScreen(
                        navigateToScreen = navigateToScreen
                    )
                }

                composable(
                    route = ScreenRoute.PhotoRoute.route + "/{Photo}",
                    arguments = listOf(
                        navArgument("Photo") {
                            type = NavType.StringType
                            defaultValue = ""
                        }
                    )
                ) {backStackEntry ->
                    // TODO Test
                    // 선택한 Photo 가져오기
                    val encodedPhotoData = backStackEntry.arguments?.getString("Photo") ?: ""
                    val decodedPhoto = Uri.decode(encodedPhotoData)

                    // JSON을 객체로 변환
                    val photoArguments = CommonUtils.convertJSONToObj<String>(decodedPhoto, String::class.java)

                    Log.e("sy.lee", photoArguments.toString())

                    PhotoScreen(
                        photoViewModel = photoViewModel,
                        navigateToMain = navigateToMain,
                    )
                }
            }
        }
    }
}

/**
 * 스크린 이동 Route
 */
sealed class ScreenRoute(val route: String) {
    data object AlbumListRoute : ScreenRoute("AlbumList")

    data object PhotoListRoute : ScreenRoute("PhotoList")

    data object PhotoRoute : ScreenRoute("Photo")
}