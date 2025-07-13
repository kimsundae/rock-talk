package com.zikkeunzikkeun.rocktalk.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zikkeunzikkeun.rocktalk.ui.components.InputField
import com.zikkeunzikkeun.rocktalk.ui.components.InputFieldWithIcon
import com.zikkeunzikkeun.rocktalk.ui.theme.Strings

@Composable
fun UserProfileScreen() {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var center by remember { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val contentWidth = screenWidth * 0.7f

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    .background(Color.Gray)
            )

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile",
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.LightGray)
                    .padding(4.dp)
                    .align(Alignment.BottomEnd)
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

