package com.example.userstory

import androidx.navigation.NavHostController

class UserStoryNavActions(
    navController: NavHostController
) {
    // 일반적인 스크린 이동
    val navigateToScreen: (String) -> Unit = { screenId ->
        navController.navigate(screenId)
    }

    // 메인 (앨범리스트) 스크린 이동
    val navigateToMain: () -> Unit = {
        navController.navigate("AlbumList") {
            popUpTo(0)
            launchSingleTop = true
        }
    }
}