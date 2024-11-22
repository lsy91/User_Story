package com.example.userstory

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.userstory.ui.theme.UserStoryTheme

@Composable
fun UserStoryApp(
    widthSizeClass: WindowWidthSizeClass
) {
    UserStoryTheme {

        // navigation controller
        val navController = rememberNavController()
        val navActions = remember(navController) {
            UserStoryNavActions(navController)
        }

        // coroutine scope
        val coroutineScope = rememberCoroutineScope()

        // 현재 스크린
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.arguments?.getString("")

        // 태블릿 (또는 Z 폴드) / 일반 휴대폰 스크린 사이즈 판단
        val isExpandedScreen = (widthSizeClass == WindowWidthSizeClass.Expanded) || (widthSizeClass == WindowWidthSizeClass.Medium)

        UserStoryNavGraph(
            isExpandedScreen = isExpandedScreen,
            navController = navController,
            navigateToScreen = navActions.navigateToScreen,
            navigateToMain = navActions.navigateToMain,
            coroutineScope = coroutineScope
        )
    }
}