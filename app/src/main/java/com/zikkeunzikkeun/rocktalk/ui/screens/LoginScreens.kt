package com.zikkeunzikkeun.rocktalk.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.zikkeunzikkeun.rocktalk.R
import com.zikkeunzikkeun.rocktalk.api.firebaseLoginWithProviderToken
import com.zikkeunzikkeun.rocktalk.ui.components.CommonAlertDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonProgress
import com.zikkeunzikkeun.rocktalk.ui.theme.Strings

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val contentWidth = screenWidth * 0.7f
    val logoHeight = screenHeight * 0.35f
    val infoHeight = screenHeight * 0.2f
    val buttonHeight = screenHeight * 0.07f
    val verticalSpacing = screenHeight * 0.02f

    var isLoading by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var onDismiss by remember {mutableStateOf({showErrorDialog = false})}

    val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = remember {
        { token, error ->
            when {
                error != null -> {
                    isLoading=false
                    showErrorDialog = true
                    Log.e(null, Strings.Errors.KAKAO_ACCOUNT_LOGIN_FAIL, error)
                }
                token != null -> {
                    Log.i(null, "${Strings.Notice.KAKAO_ACCOUNT_LOGIN_SUCCESS} ${token.accessToken}")
                    firebaseLoginWithProviderToken(
                        provider = "KAKAO",
                        token.accessToken,
                        onSuccess = {
                            isLoading = false;
                            navController.navigate("main_screen") {
                                popUpTo("login_screen") { inclusive = true }
                            }
                        },
                        onFailure = {
                            isLoading = false;
                            showErrorDialog = true
                        }
                    )
                }
            }
        }
    }

    val onClickKakaoBtn = remember {
        {
            isLoading = true;
            try{
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                    UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                        if (error != null) {
                            Log.e(null, Strings.Errors.KAKAO_LOGIN_FAIL, error)
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) return@loginWithKakaoTalk
                            UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback)
                        } else if (token != null) {
                            Log.i(null, "${Strings.Notice.KAKAO_ACCOUNT_LOGIN_SUCCESS} ${token.accessToken}")
                            firebaseLoginWithProviderToken(
                                provider = "KAKAO",
                                token.accessToken,
                                onSuccess = {
                                    isLoading = false
                                    navController.navigate("main_screen") {
                                        popUpTo("login_screen") { inclusive = true }
                                    }
                                },
                                onFailure = {e->
                                    isLoading = false
                                    showErrorDialog = true
                                    Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback)
                }
            }catch(e: Exception){
                isLoading=false
                showErrorDialog = true
                Log.e("KAKAO_LOGIN", "예외 발생", e)
            }
        }
    }

    val onClickNaverBtn = remember {
        {
            isLoading = true
            val callback = object : OAuthLoginCallback {
                override fun onSuccess() {
                    val accessToken = NaverIdLoginSDK.getAccessToken()
//                    Toast.makeText(context, "네이버 로그인 성공! 토큰: $accessToken", Toast.LENGTH_SHORT).show()
                    Log.i("NaverLogin", "accessToken: $accessToken")
                    isLoading = true;
                    firebaseLoginWithProviderToken(
                        provider = "NAVER",
                        accessToken.toString(),
                        onSuccess = {
                            isLoading = false;
                            navController.navigate("main_screen") {
                                popUpTo("login_screen") { inclusive = true }
                            }
                        },
                        onFailure = {
                            isLoading = false;
                            showErrorDialog = true
                            Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(context, "Naver 로그인 실패", Toast.LENGTH_SHORT).show()
                    Log.e("NaverLogin", "errorCode: $errorCode, $errorDescription")
                    isLoading = false
                    showErrorDialog = true
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            }
            NaverIdLoginSDK.authenticate(context, callback)
        }
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(contentWidth),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.rock_talk_login_logo),
                contentDescription = Strings.Text.ROCK_TALK_LOGIN_LOGO,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(logoHeight)
                    .clip(RoundedCornerShape(2.dp)),
                contentScale = ContentScale.FillBounds
            )

            Spacer(modifier = Modifier.height(verticalSpacing))

            Image(
                painter = painterResource(id = R.drawable.rock_talk_login_info),
                contentDescription = Strings.Text.ROCK_TALK_LOGIN_TEXT,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(infoHeight)
                    .clip(RoundedCornerShape(2.dp)),
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.height(verticalSpacing))

            Image(
                painter = painterResource(id = R.drawable.kakao_login_btn),
                contentDescription = Strings.Text.KAKAO_LOGIN,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .clip(RoundedCornerShape(2.dp))
                    .clickable { onClickKakaoBtn() },
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(id = R.drawable.naver_login_btn),
                contentDescription = Strings.Text.NAVER_LOGIN,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .clip(RoundedCornerShape(2.dp))
                    .clickable { onClickNaverBtn() },
                contentScale = ContentScale.FillWidth
            )
        }
    }

    CommonProgress(isLoading = isLoading);
    CommonAlertDialog(
        isShow = showErrorDialog,
        onDismiss = onDismiss,
        "",
        "일시적인 오류가 발생했습니다",
        "확인"
    )
}
