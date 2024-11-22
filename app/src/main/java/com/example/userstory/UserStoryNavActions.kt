package com.example.userstory

import android.net.Uri
import androidx.navigation.NavHostController
import com.example.userstory.utils.CommonUtils

class UserStoryNavActions(
    navController: NavHostController
) {
    // 일반적인 스크린 이동
    val navigateToScreen: (String, Any?) -> Unit = { screenId, arguments ->

        // 전달할 Arguments 가 있는지 체크
        val encodedArguments = arguments?.let { args -> Uri.encode(CommonUtils.convertObjToJSON(args)) } ?: ""

        val route = if (encodedArguments.isNotEmpty()) {
            "$screenId/$encodedArguments"
        } else {
            screenId
        }

        navController.navigate(route)
    }

    // 메인 (앨범리스트) 스크린 이동
    val navigateToMain: () -> Unit = {
        navController.navigate("AlbumList") {
            popUpTo(0)
            launchSingleTop = true
        }
    }
}