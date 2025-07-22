package com.zikkeunzikkeun.rocktalk.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.zikkeunzikkeun.rocktalk.ui.theme.LightGreen40
import com.zikkeunzikkeun.rocktalk.ui.theme.Orange40

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = LightGreen40,) {
        NavigationBarItem(
            selected = currentRoute == "record_screen",
            onClick = { navController.navigate("record_screen") },
            icon = { Icon(Icons.Default.Edit, contentDescription = "기록", modifier = Modifier.size(34.dp)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                indicatorColor = Orange40
            )
        )
        NavigationBarItem(
            selected = currentRoute == "main_screen",
            onClick = { navController.navigate("main_screen") },
            icon = { Icon(Icons.Default.Home, contentDescription = "메인", modifier = Modifier.size(34.dp)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                indicatorColor = Orange40
            )
        )
        NavigationBarItem(
            selected = currentRoute == "user_profile_screen",
            onClick = { navController.navigate("user_profile_screen") },
            icon = { Icon(Icons.Default.Person, contentDescription = "프로필", modifier = Modifier.size(34.dp)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                indicatorColor = Orange40
            )
        )
    }
}
