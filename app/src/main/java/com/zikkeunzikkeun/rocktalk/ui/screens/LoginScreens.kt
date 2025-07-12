package com.zikkeunzikkeun.rocktalk.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.zikkeunzikkeun.rocktalk.R
import com.zikkeunzikkeun.rocktalk.api.getKakaoUserInfo
import com.zikkeunzikkeun.rocktalk.ui.theme.Strings


@Composable
fun LoginScreen(){
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val contentWidth = configuration.screenWidthDp.dp * 0.6f // 60%

    // kakao login callback
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        when{
            error != null -> Log.e(null, Strings.Errors.KAKAO_ACCOUNT_LOGIN_FAIL, error)
            token != null -> {
                Log.i(null, "${Strings.Notice.KAKAO_ACCOUNT_LOGIN_SUCCESS} ${token.accessToken}")
                getKakaoUserInfo(token.accessToken);
            }
        }
    }
    // kakao 로그인 콜백
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
                            Log.i(null, "${Strings.Notice.KAKAO_ACCOUNT_LOGIN_SUCCESS} ${token.accessToken}")
                        }
                    }
                }
            }
            else ->  UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    // 네이버 로그인 콜백
    fun onClickNaverBtn(context: Context) {
        val callback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 로그인 성공! 토큰 등 정보 활용
                val accessToken = NaverIdLoginSDK.getAccessToken()
                val refreshToken = NaverIdLoginSDK.getRefreshToken()
                val expiresAt = NaverIdLoginSDK.getExpiresAt()
                val tokenType = NaverIdLoginSDK.getTokenType()
                val state = NaverIdLoginSDK.getState()

                // 예시: 토스트 출력 또는 ViewModel 등으로 넘기기
                Toast.makeText(context, "네이버 로그인 성공! 토큰: $accessToken", Toast.LENGTH_SHORT).show()
                Log.i("NaverLogin", "accessToken: $accessToken, refreshToken: $refreshToken")
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(context, "Naver 로그인 실패: $errorCode, $errorDescription", Toast.LENGTH_SHORT).show()
                Log.e("NaverLogin", "errorCode: $errorCode, $errorDescription")
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(context, callback)
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center) // 가로는 시작(왼쪽), 세로는 중앙!
                .width(contentWidth)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.rock_talk_login_logo),
                contentDescription = Strings.Text.ROCK_TALK_LOGIN_LOGO,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
                    .padding(top = 100.dp, bottom = 80.dp)
                    .clip(RoundedCornerShape(2.dp)),
                contentScale = ContentScale.FillBounds
            )

            Image(
                painter = painterResource(id = R.drawable.rock_talk_login_info),
                contentDescription = Strings.Text.ROCK_TALK_LOGIN_LOGO,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
                    .clip(RoundedCornerShape(2.dp)),
                contentScale = ContentScale.FillWidth
            )

            Image(
                painter = painterResource(id = R.drawable.kakao_login_btn),
                contentDescription = Strings.Text.KAKAO_LOGIN,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .clickable {
                        onClickKakaoBtn(context)
                    },
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(id = R.drawable.naver_login_btn),
                contentDescription = Strings.Text.NAVER_LOGIN,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .clickable {
                        onClickNaverBtn(context);
                    },
                contentScale = ContentScale.FillWidth
            )
        }
    }
}



