package com.zikkeunzikkeun.rocktalk.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zikkeunzikkeun.rocktalk.R
import com.zikkeunzikkeun.rocktalk.ui.auth.GoogleSignInActivity
import com.zikkeunzikkeun.rocktalk.ui.components.GoogleSignInButton


@Composable
fun LoginScreen(){
    val context = LocalContext.current
    // 1. 결과를 받는 런처 선언
    val loginLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // 4. 여기서 Activity의 결과(Intent data 등)를 받음!
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            // data에서 로그인 결과 꺼내기 (예: 토큰, 사용자 정보 등)
            // 예: val token = data?.getStringExtra("id_token")
        } else {
            // 로그인 실패, 취소 등 처리
        }
    }

    fun launchGoogleLoginActivity() {
        val intent = Intent(context, GoogleSignInActivity::class.java)
        loginLauncher.launch(intent)
    }

    val configuration = LocalConfiguration.current
    val contentWidth = configuration.screenWidthDp.dp * 0.6f // 60%

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(contentWidth),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.kakao_login_btn),
                contentDescription = "Kakao Login",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .clickable {

                    },
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(5.dp))
            GoogleSignInButton(onClick = {
                launchGoogleLoginActivity()
            });
        }
    }
}

