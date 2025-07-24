package com.zikkeunzikkeun.rocktalk.ui.screens

import RecordScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zikkeunzikkeun.rocktalk.data.UserInfoData
import com.zikkeunzikkeun.rocktalk.ui.components.BottomNavBar
import com.zikkeunzikkeun.rocktalk.ui.theme.RockTalkTheme
import androidx.compose.runtime.*
import com.zikkeunzikkeun.rocktalk.api.getUserInfo
import com.zikkeunzikkeun.rocktalk.util.getUserId


@Composable
fun RockTalkApp() {
    val userId = getUserId()
    var userInfo by remember { mutableStateOf<UserInfoData?>(UserInfoData()) }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    LaunchedEffect(Unit) {
        val userId = getUserId()
        if (!userId.isNullOrEmpty()) {
            userInfo = getUserInfo(userId)
        }
    }

    RockTalkTheme {
        Scaffold(
            bottomBar = {
                if (currentRoute != "login_screen")
                    BottomNavBar(navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = when {
                    userId.isNullOrBlank() -> "login_screen"
                    else -> "main_screen"
                },
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("login_screen") {
                    LoginScreen(navController)
                }

                composable("main_screen") {
                    MainScreen(navController)
                }

                composable("user_profile_screen") {
                    UserProfileScreen(navController = navController)
                }

                composable(
                    route = "board_list_screen?userId={userId}&userName={userName}&boardType={boardType}&centerId={centerId}&centerName={centerName}",
                    arguments = listOf(
                        navArgument("userId") { type = NavType.StringType; },
                        navArgument("userName") { type = NavType.StringType; },
                        navArgument("boardType") { type = NavType.StringType; },
                        navArgument("centerId") { type = NavType.StringType },
                        navArgument("centerName") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    BoardListScreen(
                        navController,
                        userId = backStackEntry.arguments?.getString("userId"),
                        userName = backStackEntry.arguments?.getString("userName"),
                        boardType = backStackEntry.arguments?.getString("boardType"),
                        centerId = backStackEntry.arguments?.getString("centerId"),
                        centerName = backStackEntry.arguments?.getString("centerName"),
                    )
                }

                composable(
                    route = "board_regist_screen?centerId={centerId}&userId={userId}&userName={userName}&boardType={boardType}",
                    arguments = listOf(
                        navArgument("centerId") { type = NavType.StringType; },
                        navArgument("userId") { type = NavType.StringType; },
                        navArgument("userName") { type = NavType.StringType; },
                        navArgument("boardType") { type = NavType.StringType; }
                    )
                ){
                    backStackEntry ->
                    BoardRegistScreen(
                        navController,
                        centerId = backStackEntry.arguments?.getString("centerId"),
                        userId = backStackEntry.arguments?.getString("userId"),
                        boardType = backStackEntry.arguments?.getString("boardType"),
                        userName = backStackEntry.arguments?.getString("userName")
                    )
                }

                composable(
                    route = "board_info_screen?userId={userId}&boardId={boardId}",
                    arguments = listOf(
                        navArgument("userId") { type = NavType.StringType; },
                        navArgument("boardId") { type = NavType.StringType; }
                    )
                ){
                        backStackEntry ->
                    BoardInfoScreen(
                        navController,
                        boardId = backStackEntry.arguments?.getString("boardId"),
                        userId = backStackEntry.arguments?.getString("userId"),
                    )
                }

                composable("record_screen") {
                    RecordScreen(navController = navController)
                }
            }
        }
    }
}
