package com.zikkeunzikkeun.rocktalk.ui.screens

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.zikkeunzikkeun.rocktalk.R
import com.zikkeunzikkeun.rocktalk.api.callUpdateUserInfoCloudFunction
import com.zikkeunzikkeun.rocktalk.api.uploadProfileImageAndGetUrl
import com.zikkeunzikkeun.rocktalk.ui.components.CommonRadioGroup
import com.zikkeunzikkeun.rocktalk.ui.components.InputField
import com.zikkeunzikkeun.rocktalk.ui.components.InputFieldWithIcon
import com.zikkeunzikkeun.rocktalk.ui.theme.Orange40
import com.zikkeunzikkeun.rocktalk.ui.theme.Strings
import kotlinx.coroutines.launch

@Composable
fun UserProfileScreen() {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    val uid = user?.uid
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var myCenter by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val contentWidth = screenWidth * 0.7f

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> selectedUri = uri }

    val coroutineScope = rememberCoroutineScope()
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

            Box {
                // 프로필 사진 (기본 회색 원)
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

            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        val imageUrl = selectedUri?.let {
                            uploadProfileImageAndGetUrl(it, "profile", "img")
                        }
                        val success = callUpdateUserInfoCloudFunction(
                            userId = uid.toString(),
                            age = age,
                            gender = gender,
                            nickname = nickname,
                            center = myCenter,
                            profileImageUrl = imageUrl
                        )
                        isLoading = false
                        if (success)
                            Toast.makeText(context, "성공 @@", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(context, "실패 !!", Toast.LENGTH_SHORT).show()
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
}

