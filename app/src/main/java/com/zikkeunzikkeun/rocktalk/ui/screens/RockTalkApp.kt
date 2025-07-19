package com.zikkeunzikkeun.rocktalk.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zikkeunzikkeun.rocktalk.ui.components.BottomNavBar
import com.zikkeunzikkeun.rocktalk.ui.theme.RockTalkTheme

@Composable
fun RockTalkApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    RockTalkTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (currentRoute !in listOf("login_screen")) {
                    BottomNavBar(navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = "loading_screen"
            ) {
                composable("loading_screen") {
                    LoadingScreen(navController)
                }

                composable("login_screen") {
                    LoginScreen(navController)
                }

                composable("main_screen") {
                    MainScreen()
                }

                composable("user_profile_screen") {
                    UserProfileScreen(navController);
                }
            }
        }
    }
}
