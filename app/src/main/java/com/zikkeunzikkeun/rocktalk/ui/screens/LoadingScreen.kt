package com.zikkeunzikkeun.rocktalk.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoadingScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    LaunchedEffect(Unit) {
        if (currentUser != null) {
            Log.i("loadingscreen", "exist currentUser");
            navController.navigate("user_profile_screen") {
                popUpTo("loading_screen") { inclusive = true }
            }
        } else {
            navController.navigate("login_screen") {
                popUpTo("loading_screen") { inclusive = true }
            }
        }
    }

    // 로딩 UI (원형 프로그레스)
    CircularProgressIndicator(
        modifier = Modifier.fillMaxSize()
    )
}