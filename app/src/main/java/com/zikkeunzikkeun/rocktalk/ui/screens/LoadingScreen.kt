package com.zikkeunzikkeun.rocktalk.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.zikkeunzikkeun.rocktalk.data.UserInfoData

@Composable
fun LoadingScreen(navController: NavHostController, userInfo: UserInfoData?) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    LaunchedEffect(Unit) {
        when {
            currentUser == null -> {
                navController.navigate("login_screen") {
                    popUpTo("loading_screen") { inclusive = true }
                }
            }
            userInfo?.editYn == false -> {
                navController.navigate("user_profile_screen") {
                    popUpTo("loading_screen") { inclusive = true }
                }
            }
            else -> {
                navController.navigate("main_screen") {
                    popUpTo("loading_screen") { inclusive = true }
                }
            }
        }
    }
}