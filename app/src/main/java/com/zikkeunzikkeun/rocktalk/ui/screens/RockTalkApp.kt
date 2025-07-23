package com.zikkeunzikkeun.rocktalk.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zikkeunzikkeun.rocktalk.data.UserInfoData
import com.zikkeunzikkeun.rocktalk.ui.components.BottomNavBar
import com.zikkeunzikkeun.rocktalk.ui.theme.RockTalkTheme

@Composable
fun RockTalkApp(
    userInfo: UserInfoData?,
    setUserInfo: (UserInfoData?) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    SideEffect {
        Log.i("RockTalkApp", "Recomposed with userInfo = ${userInfo?.editYn}")
    }
    RockTalkTheme {
        Scaffold(
            bottomBar = {
                if (
                    currentRoute != "login_screen" &&
                    !(currentRoute == "user_profile_screen" && userInfo?.editYn == false)
                ) {
                    BottomNavBar(navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = when {
                    userInfo == null -> "login_screen"
                    userInfo.editYn == false -> "user_profile_screen"
                    else -> "main_screen"
                },
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("login_screen") {
                    LoginScreen(navController)
                }

                composable("main_screen") {
                    MainScreen(navController,userInfo?: UserInfoData())
                }

                composable("user_profile_screen") {
                    UserProfileScreen(
                        navController = navController,
                        userInfo = userInfo,
                        setUserInfo = setUserInfo
                    )
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
                    route = "board_regist_screen?centerId={centerId}&userId={userId}&boardType={boardType}",
                    arguments = listOf(
                        navArgument("centerId") { type = NavType.StringType; },
                        navArgument("userId") { type = NavType.StringType; },
                        navArgument("boardType") { type = NavType.StringType; }
                    )
                ){
                    backStackEntry ->
                    BoardRegistScreen(
                        navController,
                        centerId = backStackEntry.arguments?.getString("centerId"),
                        userId = backStackEntry.arguments?.getString("userId"),
                        boardType = backStackEntry.arguments?.getString("boardType")
                    )
                }
            }
        }
    }
}
