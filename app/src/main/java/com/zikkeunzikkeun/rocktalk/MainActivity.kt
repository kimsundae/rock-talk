package com.zikkeunzikkeun.rocktalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.zikkeunzikkeun.rocktalk.ui.screens.LoginScreen
import com.zikkeunzikkeun.rocktalk.ui.screens.RockTalkApp
import com.zikkeunzikkeun.rocktalk.ui.screens.UserProfileScreen
import com.zikkeunzikkeun.rocktalk.ui.theme.RockTalkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RockTalkTheme {
                RockTalkApp();
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
        val navController = rememberNavController()
//        LoginScreen(navController)
        UserProfileScreen();

}