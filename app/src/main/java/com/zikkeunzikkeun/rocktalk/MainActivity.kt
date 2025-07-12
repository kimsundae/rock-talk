package com.zikkeunzikkeun.rocktalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zikkeunzikkeun.rocktalk.ui.screens.LoginScreen
import com.zikkeunzikkeun.rocktalk.ui.theme.RockTalkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RockTalkTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) {
                LoginScreen();
//                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RockTalkTheme {
            LoginScreen()
    }
}