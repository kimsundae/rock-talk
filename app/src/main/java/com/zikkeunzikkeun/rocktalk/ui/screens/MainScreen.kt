package com.zikkeunzikkeun.rocktalk.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.zikkeunzikkeun.rocktalk.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.zikkeunzikkeun.rocktalk.api.getUserInfo
import com.zikkeunzikkeun.rocktalk.data.CenterInfoData
import com.zikkeunzikkeun.rocktalk.data.UserInfoData
import com.zikkeunzikkeun.rocktalk.ui.components.CommonAlertDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonCenterSelectDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonConfirmDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonProgress
import com.zikkeunzikkeun.rocktalk.ui.components.InfoCard
import com.zikkeunzikkeun.rocktalk.util.getUserId

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavController){
    var userInfo by remember { mutableStateOf<UserInfoData>(UserInfoData()) }
    var centerInfo by remember { mutableStateOf(CenterInfoData())}
    var selectedCenter by remember { mutableStateOf(CenterInfoData())}
    var isOpenCenterDialog by remember { mutableStateOf(false) }
    var isOpenSelectConfirm by remember { mutableStateOf(false) }
    var isOpenAlert by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val imageUrls = listOf(
        "https://via.placeholder.com/300x200?text=Image1",
        "https://via.placeholder.com/300x200?text=Image2",
        "https://via.placeholder.com/300x200?text=Image3",
        "https://via.placeholder.com/300x200?text=Image4",
        "https://via.placeholder.com/300x200?text=Image5"
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { imageUrls.size }
    )
    LaunchedEffect(Unit) {
        isLoading = true
        val userId = getUserId()
        if (!userId.isNullOrEmpty()) {
            val userInfoData = getUserInfo(userId) ?: UserInfoData()
            userInfo = userInfoData
            centerInfo = CenterInfoData(
                centerId = userInfoData.centerId,
                centerName = userInfoData.centerName
            )
        }
        isLoading = false
    }
    LaunchedEffect(userInfo) {
        if(!userInfo.userId.isNullOrBlank() && userInfo.editYn == false){
            isOpenAlert = true
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color.Black.copy(alpha = 0.15f),
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF2F2F2))
                .clickable { isOpenCenterDialog = true }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "센터명 검색",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.rock_icon),
                contentDescription = "기본 아이콘",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = centerInfo.centerName,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        // HorizontalPager
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(120.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 100.dp),
            pageSpacing = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) { page ->
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrls[page]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(imageUrls.size) { i ->
                val selected = i == pagerState.currentPage
                Box(
                    modifier = Modifier
                        .size(if (selected) 10.dp else 6.dp)
                        .padding(2.dp)
                        .background(
                            color = if (selected) Color.Gray else Color.LightGray,
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        InfoCard(
            title = "공지사항",
            icon = Icons.Default.Notifications,
            onClickIcon = {
                navController.navigate("board_list_screen?userId=${userInfo.userId}&userName=${userInfo.nickname}&boardType=0&centerId=${centerInfo.centerId}&centerName=${centerInfo.centerName}") {
                    popUpTo("main_screen") { inclusive = true }
                }
            },
            items = listOf(
                "운영시간 안내! 이번 주 운영 일정 확인하세요!",
                "이벤트: 이 달의 챌린지 완료하고 선물 받자!",
                "새로운 장비! 나이로이벤트 무료 개방 안내",
                "주의공지! 사고 예방을 위한 기본 규칙"
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 6. 함께해요 카드
        InfoCard(
            title = "함께해요",
            icon = Icons.Default.Face,
            onClickIcon = {
                navController.navigate("board_list_screen?userId=${userInfo.userId}&userName=${userInfo.nickname}&boardType=1&centerId=${centerInfo.centerId}&centerName=${centerInfo.centerName}") {
                    popUpTo("main_screen") { inclusive = true }
                }
            },
            items = listOf(
                "주말 모임! 도쿄와 오븐 루트 클라이머 같이 하실 분",
                "참여는 O.K! 지원자 용무가 많이 오릅니다",
                "미트업 부트캠프합니다! 토욜 점심에 모일까요?",
                "초보 환영! OCR부터 크럭스 구간 완등 함께 원해요"
            )
        )
    }
    CommonCenterSelectDialog(
        isShow = isOpenCenterDialog,
        onDismiss = {isOpenCenterDialog = false},
        onCenterClick = { centerInfoData ->
            selectedCenter = CenterInfoData(
                centerId = centerInfoData.centerId,
                centerName = centerInfoData.centerName
            )
            isOpenCenterDialog = false
            isOpenSelectConfirm = true
        }
    )
    CommonConfirmDialog(
        isShow = isOpenSelectConfirm,
        onDismiss = { isOpenSelectConfirm = false },
        title = "알림",
        text = "센터를 변경하시겠습니까?",
        confirmText = "확인",
        cancelText = "취소",
        onConfirm = { centerInfo = selectedCenter.copy() },
        onCancel = { isOpenSelectConfirm = false }
    )
    CommonAlertDialog(
        isShow = isOpenAlert,
        onDismiss = {
            isOpenAlert = false
            navController.navigate("user_profile_screen") {
                popUpTo("main_screen") { inclusive = true }
            }
        },
        title = "알림",
        text = "최초 프로필 변경이 필요합니다.",
        buttonText = "확인"
    )
    CommonProgress(isLoading = isLoading);
}

