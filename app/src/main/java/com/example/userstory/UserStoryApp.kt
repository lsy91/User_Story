package com.example.userstory

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.userstory.ui.theme.UserStoryTheme
import com.example.userstory.utils.PermissionHelper

@Composable
fun UserStoryApp(
    permissionHelper: PermissionHelper,
    appSettingsLauncher: ActivityResultLauncher<Intent>
) {
    UserStoryTheme {

        // navigation controller
        val navController = rememberNavController()
        val navActions = remember(navController) {
            UserStoryNavActions(navController)
        }

        UserStoryNavGraph(
            navController = navController,
            permissionHelper = permissionHelper,
            appSettingsLauncher = appSettingsLauncher,
            navigateToScreen = navActions.navigateToScreen,
            navigateToMain = navActions.navigateToMain,
        )
    }
}