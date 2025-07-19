package com.zikkeunzikkeun.rocktalk.util

import androidx.navigation.NavController

fun moveToLogin(navController: NavController) {
    navController.navigate("login_screen") {
        popUpTo("main_screen") { inclusive = true }
    }
}
