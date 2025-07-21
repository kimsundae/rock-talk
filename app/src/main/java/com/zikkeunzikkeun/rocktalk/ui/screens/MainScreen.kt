package com.zikkeunzikkeun.rocktalk.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.zikkeunzikkeun.rocktalk.ui.components.InfoCard

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    val imageUrls = listOf(
        "https://via.placeholder.com/300x200?text=Image1",
        "https://via.placeholder.com/300x200?text=Image2"
    )

    var searchQuery by remember { mutableStateOf("") }
    val pagerState = rememberPagerState(
        pageCount = { imageUrls.size }
    )
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // 1. SearchBar (Material3)
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { /* search logic */ },
            active = false,
            onActiveChange = {},
            placeholder = { Text("센터명을 입력하세요.") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "검색") },
            modifier = Modifier.fillMaxWidth()
        ) {}

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Title
        Text(
            text = "더클라임 강남점",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 3. Carousel (HorizontalPager)
        HorizontalPager(
            pageCount = imageUrls.size,   // 여기에 pageCount 직접 명시
            state = pagerState,
            pageSize = PageSize.Fixed(120.dp), // 고정 크기로 여러 개 보이게
            contentPadding = PaddingValues(horizontal = 16.dp),
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

        Spacer(modifier = Modifier.height(8.dp))

        // 4. Page Indicator
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

        // 5. 공지사항 카드
        InfoCard(
            title = "공지사항",
            icon = Icons.Default.Notifications,
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
            items = listOf(
                "주말 모임! 도쿄와 오븐 루트 클라이머 같이 하실 분",
                "참여는 O.K! 지원자 용무가 많이 오릅니다",
                "미트업 부트캠프합니다! 토욜 점심에 모일까요?",
                "초보 환영! OCR부터 크럭스 구간 완등 함께 원해요"
            )
        )
    }
}

