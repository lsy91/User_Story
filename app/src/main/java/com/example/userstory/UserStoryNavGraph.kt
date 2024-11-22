package com.example.userstory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.userstory.ui.common.BaseText
import com.example.userstory.ui.feature.album_list.AlbumListScreen
import com.example.userstory.ui.theme.RobotoRegular
import com.example.userstory.ui.theme.UserStoryBackgroundColor
import com.example.userstory.ui.theme.UserStoryFontColor
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserStoryNavGraph(
    isExpandedScreen: Boolean,
    navController: NavHostController,
    navigateToScreen: (String) -> Unit,
    navigateToMain: () -> Unit,
    coroutineScope: CoroutineScope
) {
    Scaffold(
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            TopAppBar(
                title = {
                    BaseText(
                        text = when (currentRoute) {
                            "AlbumList" -> "ALBUM LIST"
                            "PictureList" -> "앨범명"
                            "Picture" -> "앨범명"
                            else -> ""
                        },
                        fontSize = 18,
                        fontColor = UserStoryFontColor,
                        fontWeight = 700,
                        fontFamily = RobotoRegular
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = UserStoryBackgroundColor
                )
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
                    AlbumListScreen()
                }

                composable(
                    route = ScreenRoute.PictureListRoute.route
                ) {

                }

                composable(
                    route = ScreenRoute.PictureRoute.route
                ) {

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

    data object PictureListRoute : ScreenRoute("PictureList")

    data object PictureRoute : ScreenRoute("Picture")
}