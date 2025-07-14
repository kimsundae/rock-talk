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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.zikkeunzikkeun.rocktalk.R
import com.zikkeunzikkeun.rocktalk.ui.components.InputField
import com.zikkeunzikkeun.rocktalk.ui.components.InputFieldWithIcon
import com.zikkeunzikkeun.rocktalk.ui.theme.Strings

@Composable
fun UserProfileScreen() {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var center by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var uploading by remember { mutableStateOf(false) }
    var uploadMessage by remember { mutableStateOf<String?>(null) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val contentWidth = screenWidth * 0.7f

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> selectedUri = uri }

    Column(
        modifier = Modifier
            .width(contentWidth)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(Strings.Text.MY_PAGE, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        Box(contentAlignment = Alignment.BottomEnd) {
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
                // 2. 필요하다면 반투명 회색 오버레이
                 Box(
                     modifier = Modifier
                         .matchParentSize()
                         .background(Color.Gray.copy(alpha = 0.4f))
                 )
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
        InputField("성명", name) { name = it }
        InputField("나이", age) { age = it }
        InputFieldWithIcon("성별", gender) { gender = it }
        InputField("닉네임", nickname) { nickname = it }
        InputFieldWithIcon("내센터", center) { center = it }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* Save */ },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC80)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(Strings.Text.SAVE, fontWeight = FontWeight.Bold)
        }
    }
}

