package com.zikkeunzikkeun.rocktalk.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.zikkeunzikkeun.rocktalk.R
import com.zikkeunzikkeun.rocktalk.api.callUpdateUserInfoCloudFunction
import com.zikkeunzikkeun.rocktalk.util.getUserId
import com.zikkeunzikkeun.rocktalk.api.uploadProfileImageAndGetUrl
import com.zikkeunzikkeun.rocktalk.dto.AlertDialogData
import com.zikkeunzikkeun.rocktalk.dto.UserInfoDto
import com.zikkeunzikkeun.rocktalk.ui.components.CommonAlertDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonConfirmDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonRadioGroup
import com.zikkeunzikkeun.rocktalk.ui.components.InputField
import com.zikkeunzikkeun.rocktalk.ui.components.InputFieldWithIcon
import com.zikkeunzikkeun.rocktalk.ui.theme.Orange40
import com.zikkeunzikkeun.rocktalk.ui.theme.Strings
import com.zikkeunzikkeun.rocktalk.util.moveToLogin
import kotlinx.coroutines.launch

@Composable
fun UserProfileScreen(navController: NavController) {
    val userId: String? = getUserId()
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var myCenter by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    // dialog 상태관리
    var showDialog by remember { mutableStateOf(false) }
    var dialogData by remember { mutableStateOf(AlertDialogData(){showDialog = false}) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val contentWidth = screenWidth * 0.7f

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> selectedUri = uri }
    // 저장 시 사용하는 coroutine
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        if (userId.isNullOrEmpty()) {
            dialogData = AlertDialogData(
                "오류",
                "사용자 정보를 불러올 수 없습니다. \n다시 로그인 해주시기 바랍니다.",
                "확인"
            ){
                showDialog = false
                moveToLogin(navController)
            }
            showDialog = true
        }
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
                ) {
                    if (selectedUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedUri),
                            contentDescription = "프로필",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.default_profile_img),
                            contentDescription = "기본 프로필",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
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
            InputField("나이", age) { age = it }
            CommonRadioGroup(
                items = listOf("남자", "여자"),
                selectedItem = gender,
                onSelect = { gender = it },
                label = "성별"
            )
            InputFieldWithIcon("내 센터", myCenter) { myCenter = it }

            Spacer(modifier = Modifier.height(32.dp))

            // 저장 버튼
            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        val imageUrl = selectedUri?.let {
                            uploadProfileImageAndGetUrl(it, "profile", "img")
                        }
                        val success = callUpdateUserInfoCloudFunction(UserInfoDto(
                            userId = userId,
                            age = age.toIntOrNull() ?: 0,
                            gender = gender,
                            nickname = nickname,
                            center = myCenter,
                            profileImageUrl = imageUrl
                        ))

                        isLoading = false
                        if (success) {
                            dialogData = AlertDialogData(
                                title = "알림",
                                text = "저장에 성공했습니다.",
                                buttonText = "확인"
                            ){
                                showDialog = false
                            }
                            showDialog = true
                        }
                        else {
                            dialogData = AlertDialogData(
                                title = "알림",
                                text = "저장에 실패했습니다.",
                                buttonText = "확인"
                            ){
                                showDialog = false
                            }
                            showDialog = true;
                        }
                    }
                },
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
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
                    .padding(top = 24.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
    // dialog composable
    CommonAlertDialog(
        isShow = showDialog,
        onDismiss = dialogData.onDismiss,
        title = dialogData.title,
        text = dialogData.text,
        buttonText = dialogData.buttonText
    )
    CommonConfirmDialog(
        isShow: Boolean = ,
        onDismiss: () -> Unit = ,
        title: String = "알림",
        text: String = "저장하시겠습니까?",
        confirmText: String = "확인",
        cancelText: String = "취소",
        onConfirm: (() -> Unit)? = null,
        onCancel: (() -> Unit)? = null
    )
}

