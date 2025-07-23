package com.zikkeunzikkeun.rocktalk.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.zikkeunzikkeun.rocktalk.R
import com.zikkeunzikkeun.rocktalk.api.callUpdateUserInfoCloudFunction
import com.zikkeunzikkeun.rocktalk.api.clearUserInfoCache
import com.zikkeunzikkeun.rocktalk.api.getUserInfo
import com.zikkeunzikkeun.rocktalk.util.getUserId
import com.zikkeunzikkeun.rocktalk.api.uploadProfileImageAndGetUrl
import com.zikkeunzikkeun.rocktalk.data.AlertDialogData
import com.zikkeunzikkeun.rocktalk.data.UserInfoData
import com.zikkeunzikkeun.rocktalk.ui.components.CommonAlertDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonCenterSelectDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonConfirmDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonProgress
import com.zikkeunzikkeun.rocktalk.ui.components.CommonRadioGroup
import com.zikkeunzikkeun.rocktalk.ui.components.InputField
import com.zikkeunzikkeun.rocktalk.ui.components.InputFieldWithIcon
import com.zikkeunzikkeun.rocktalk.ui.theme.LightGray40
import com.zikkeunzikkeun.rocktalk.ui.theme.Orange40
import com.zikkeunzikkeun.rocktalk.ui.theme.Strings
import com.zikkeunzikkeun.rocktalk.util.moveToLogin
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@Composable
fun UserProfileScreen(
    navController: NavController,
    userInfo: UserInfoData?,
    setUserInfo: (UserInfoData?) -> Unit
) {
    val context = LocalContext.current
    val userId: String? = getUserId()
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var centerId by remember { mutableStateOf("") }
    var centerName by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    // dialog 상태관리
    var isShowDialog by remember { mutableStateOf(false) }
    var isShowConfirmDialog by remember {mutableStateOf(false)}
    var isShowCenterDialog by remember { mutableStateOf(false) }
    var dialogData by remember { mutableStateOf(AlertDialogData(){isShowDialog = false}) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val contentWidth = screenWidth * 0.7f

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> selectedUri = uri }

    // firebase storage 이미지 설정
    val imageModel = selectedUri ?: userInfo?.profileImageUrl
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .crossfade(true)
            .okHttpClient {
                OkHttpClient.Builder()
                    .connectTimeout(8, TimeUnit.SECONDS)
                    .readTimeout(8, TimeUnit.SECONDS)
                    .writeTimeout(8, TimeUnit.SECONDS)
                    .build()
            }
            .build()
    }

    val painter = rememberAsyncImagePainter(
        model = imageModel,
        imageLoader = imageLoader,
        placeholder = painterResource(R.drawable.default_profile_img),
        error = painterResource(R.drawable.default_profile_img)
    )

    val state = painter.state

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        isLoading = true
        if (userId.isNullOrEmpty() || userInfo == null) {
            dialogData = AlertDialogData(
                "오류",
                "사용자 정보를 불러올 수 없습니다. \n다시 로그인 해주시기 바랍니다.",
                "확인"
            ){
                isShowDialog = false
                moveToLogin(navController)
            }
            isShowDialog = true
        } else {
            userInfo.let {
                age = it.age.toString()
                gender = it.gender
                nickname = it.nickname
                centerId = it.centerId
                centerName = it.centerName
            }
        }
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(contentWidth),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(Strings.Text.MY_PAGE, fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(24.dp))

            // 프로필 사진 영역 (기본 회색 원)
            Box {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .then(
                            if (userInfo == null && selectedUri == null) {
                                Modifier.border(
                                    width = 3.dp,
                                    color = LightGray40,
                                    shape = CircleShape
                                )
                            } else {
                                Modifier
                            }
                        )
                ) {
                    Image(
                        painter = painter,
                        contentDescription = "프로필 이미지",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                    if (state is AsyncImagePainter.State.Loading) {
                        Log.i("state", "loading");
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.Center),
                            strokeWidth = 3.dp,
                            color = Color.Gray
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.LightGray)
                        .padding(4.dp)
                        .align(Alignment.BottomEnd)
                        .clickable { pickImageLauncher.launch("image/*") }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 입력 필드 구성
            InputField("닉네임", nickname) { nickname = it }
//            InputField("나이", age) { age = it }
            CommonRadioGroup(
                items = listOf("남자", "여자"),
                selectedItem = gender,
                onSelect = { gender = it },
                label = "성별"
            )
            InputFieldWithIcon("내센터", centerName){isShowCenterDialog = true}

            Spacer(modifier = Modifier.height(32.dp))

            // 저장 버튼
            Button(
                onClick = { isShowConfirmDialog = true },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Orange40),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(Strings.Text.SAVE, fontWeight = FontWeight.Bold)
            }
        }
    }

    // loading composable
    CommonProgress(isLoading = isLoading);
    // dialog composable
    CommonAlertDialog(
        isShow = isShowDialog,
        onDismiss = dialogData.onDismiss,
        title = dialogData.title,
        text = dialogData.text,
        buttonText = dialogData.buttonText
    )
    CommonCenterSelectDialog(
        isShow = isShowCenterDialog,
        onDismiss = {isShowCenterDialog = false},
        onCenterClick = { centerInfoDto ->
            centerId = centerInfoDto.centerId
            centerName = centerInfoDto.centerName
            isShowCenterDialog = false
        }
    )
    CommonConfirmDialog(
        isShow = isShowConfirmDialog,
        onDismiss = { isShowConfirmDialog = false },
        title = "알림",
        text = "저장하시겠습니까?",
        confirmText = "확인",
        cancelText = "취소",
        onConfirm = {
            coroutineScope.launch {
                isLoading = true
                val imageUrl = selectedUri?.let {
                    uploadProfileImageAndGetUrl(it, "profile", "img")
                }
                val userInfoData = UserInfoData(
                    userId = userId,
                    age = age.toIntOrNull() ?: 0,
                    gender = gender,
                    nickname = nickname,
                    centerId = centerId,
                    profileImageUrl = imageUrl
                )
                val success = callUpdateUserInfoCloudFunction(userInfoData)
                isLoading = false
                if (success) {
                    clearUserInfoCache()
                    val updatedUserInfo = getUserInfo(userId ?: "")
                    setUserInfo(updatedUserInfo?.copy())

                    dialogData = AlertDialogData(
                        title = "알림",
                        text = "저장에 성공했습니다.",
                        buttonText = "확인"
                    ){
                        isShowDialog = false
                    }
                    isShowDialog = true
                    navController.navigate("main_screen") {
                        popUpTo("user_profile_screen") { inclusive = true }
                    }
                }
                else {
                    dialogData = AlertDialogData(
                        title = "알림",
                        text = "저장에 실패했습니다.",
                        buttonText = "확인"
                    ){
                        isShowDialog = false
                    }
                    isShowDialog = true;
                }
            }
        },
        onCancel = null
    )
}

