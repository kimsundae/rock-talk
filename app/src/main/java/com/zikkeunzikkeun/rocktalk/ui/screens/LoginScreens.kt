package com.zikkeunzikkeun.rocktalk.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.zikkeunzikkeun.rocktalk.R
import com.zikkeunzikkeun.rocktalk.ui.auth.GoogleSignInActivity
import com.zikkeunzikkeun.rocktalk.ui.components.GoogleSignInButton
import com.zikkeunzikkeun.rocktalk.ui.theme.Strings


@Composable
fun LoginScreen(){
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val contentWidth = configuration.screenWidthDp.dp * 0.6f // 60%
    // 결과를 받는 런처 선언
    val loginLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // 4. 여기서 Activity의 결과(Intent data 등)를 받음!
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            // data에서 로그인 결과 꺼내기 (예: 토큰, 사용자 정보 등)
            // 예: val token = data?.getStringExtra("id_token")
        } else {

        }
    }
    // kakao login callback
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        when{
            error != null -> Log.e(null, Strings.Errors.KAKAO_ACCOUNT_LOGIN_FAIL, error)
            token != null -> Log.i(null, "${Strings.Info.KAKAO_ACCOUNT_LOGIN_SUCCESS} ${token.accessToken}")
        }
    }

    fun launchGoogleLoginActivity() {
        val intent = Intent(context, GoogleSignInActivity::class.java)
        loginLauncher.launch(intent)
    }
    fun onClickKakaoBtn(context: Context){
        when{
            UserApiClient.instance.isKakaoTalkLoginAvailable(context) -> {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    when{
                        error != null -> {
                            Log.e(null, Strings.Errors.KAKAO_LOGIN_FAIL, error)

                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled)
                                return@loginWithKakaoTalk

                            // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                        }
                        token != null -> {
                            Log.i(null, "${Strings.Info.KAKAO_ACCOUNT_LOGIN_SUCCESS} ${token.accessToken}")
                        }
                    }
                }
            }
            else ->  UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }
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
                        onClickKakaoBtn(context)
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



