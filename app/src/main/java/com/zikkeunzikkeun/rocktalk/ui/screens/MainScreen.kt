package com.zikkeunzikkeun.rocktalk.ui.screens

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.zikkeunzikkeun.rocktalk.api.callGetRecordList
import com.zikkeunzikkeun.rocktalk.api.getBoardList
import com.zikkeunzikkeun.rocktalk.api.getUserInfo
import com.zikkeunzikkeun.rocktalk.data.BoardInfoData
import com.zikkeunzikkeun.rocktalk.data.CenterInfoData
import com.zikkeunzikkeun.rocktalk.data.RecordInfoData
import com.zikkeunzikkeun.rocktalk.data.UserInfoData
import com.zikkeunzikkeun.rocktalk.ui.components.CommonAlertDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonCenterSelectDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonConfirmDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonProgress
import com.zikkeunzikkeun.rocktalk.ui.components.InfoCard
import com.zikkeunzikkeun.rocktalk.ui.components.RecordInfoDialog
import com.zikkeunzikkeun.rocktalk.util.getUserId
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavController){
    var userInfo by remember { mutableStateOf<UserInfoData>(UserInfoData()) }
    var centerInfo by remember { mutableStateOf(CenterInfoData())}
    var recordInfoList by remember {mutableStateOf<List<RecordInfoData>>(emptyList())}
    var boardInfoList by remember {mutableStateOf<List<BoardInfoData>>(emptyList())}
    var selectedCenter by remember { mutableStateOf(CenterInfoData())}
    var selectedRecordInfo by remember { mutableStateOf<RecordInfoData?>(null)}
    var isOpenCenterDialog by remember { mutableStateOf(false) }
    var isOpenSelectConfirm by remember { mutableStateOf(false) }
    var isOpenRecordInfoModal by remember { mutableStateOf(false) }
    var isOpenAlert by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val hasMediaRecord = recordInfoList.filter {
        val uri = it.recordMediaUri?.lowercase() ?: ""
        uri.contains(".jpg") || uri.contains(".jpeg") ||
        uri.contains(".png") || uri.contains(".webp") || uri.contains(".gif") || it.thumbnailUri.isNotBlank()
    }
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { hasMediaRecord.size }
    )

    val onSearch: suspend (String) -> Unit = { centerId ->
        coroutineScope.launch {
            recordInfoList = callGetRecordList(centerId = centerId)
            boardInfoList = getBoardList(boardType = null, centerId = centerId)
        }
    }
    LaunchedEffect(centerInfo) {
        isLoading = true

        val userId = getUserId()
        if (!userId.isNullOrEmpty()) {
            val userInfoData = getUserInfo(userId) ?: UserInfoData()
            userInfo = userInfoData

            if (selectedCenter.centerId.isBlank()) {
                centerInfo = CenterInfoData(
                    centerId = userInfoData.centerId,
                    centerName = userInfoData.centerName
                )
                onSearch(userInfoData.centerId)
            } else {
                centerInfo = selectedCenter.copy()
                onSearch(selectedCenter.centerId)
            }
        }
        isLoading = false
    }
    LaunchedEffect(userInfo) {
        if(!userInfo.userId.isNullOrBlank() && userInfo.editYn == false){
            isOpenAlert = true
        }
    }

    val noticeList: List<BoardInfoData> = boardInfoList
        .filter { it.boardType == "0" }
        .take(4)
        .map {
            if (it.boardTitle.isBlank()) it.copy(boardTitle = "제목 없음") else it
        }

    val togetherList: List<BoardInfoData> = boardInfoList
        .filter { it.boardType == "1" }
        .take(4)
        .map {
            if (it.boardTitle.isBlank()) it.copy(boardTitle = "제목 없음") else it
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
            val record = hasMediaRecord.getOrNull(page)
            val mediaUri = if(record?.thumbnailUri.isNullOrBlank()) record?.recordMediaUri else record?.thumbnailUri

            val painter = rememberAsyncImagePainter(mediaUri)
            val painterState = painter.state

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        selectedRecordInfo = record
                        isOpenRecordInfoModal = true
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    if (painterState is AsyncImagePainter.State.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(36.dp),
                            strokeWidth = 3.dp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(hasMediaRecord.size) { i ->
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
            navController,
            title = "공지사항",
            icon = Icons.Default.Notifications,
            onClickIcon = {
                navController.navigate("board_list_screen?userId=${userInfo.userId}&userName=${userInfo.nickname}&boardType=0&centerId=${centerInfo.centerId}&centerName=${centerInfo.centerName}") {
                    popUpTo("main_screen") { inclusive = true }
                }
            },
            items = if (noticeList.isEmpty()) listOf(BoardInfoData(boardTitle = "게시글이 없습니다.")) else noticeList
        )

        Spacer(modifier = Modifier.height(16.dp))

        InfoCard(
            navController,
            title = "함께해요",
            icon = Icons.Default.Face,
            onClickIcon = {
                navController.navigate("board_list_screen?userId=${userInfo.userId}&userName=${userInfo.nickname}&boardType=1&centerId=${centerInfo.centerId}&centerName=${centerInfo.centerName}") {
                    popUpTo("main_screen") { inclusive = true }
                }
            },
            items = if (togetherList.isEmpty()) listOf(BoardInfoData(boardTitle = "게시글이 없습니다.")) else togetherList
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
    RecordInfoDialog(
        isEdit = true,
        userInfo = userInfo,
        recordInfo = selectedRecordInfo,
        isShow = isOpenRecordInfoModal,
        onDismiss = { isOpenRecordInfoModal = false },
        onSearch = {}
    )
    CommonProgress(isLoading = isLoading);
}

