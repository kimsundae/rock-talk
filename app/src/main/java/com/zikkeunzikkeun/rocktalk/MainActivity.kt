package com.zikkeunzikkeun.rocktalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.zikkeunzikkeun.rocktalk.ui.screens.RockTalkApp
import com.zikkeunzikkeun.rocktalk.ui.theme.RockTalkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            RockTalkTheme {
                RockTalkApp();
            }
        }
    }
}