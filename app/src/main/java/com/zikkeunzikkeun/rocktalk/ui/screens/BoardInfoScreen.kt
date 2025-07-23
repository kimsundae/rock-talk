package com.zikkeunzikkeun.rocktalk.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun BoardInfoScreen(
    navController: NavController,
    centerId:String,
    boardId:String,
    userId:String
){
    Column(
        modifier = Modifier
//            .background(color = Color(0xFFF8F6F7)) // 전체 연핑크 배경
            .fillMaxSize()
    ) {
        // 상단 연녹색 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFDDE7CE), shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFF6595B5)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "공지사항",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                // 제목, 등록일시, 등록자
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "제목",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF666666)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "[운영시간 안내] 이번 주 운영 일정 확인하세요!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF6C7A89),
                                textDecoration = TextDecoration.Underline
                            ),
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "등록일시",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF666666)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "2025-01-01 12:05",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF6C7A89))
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "등록자",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF666666)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "더클라임 강남점",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF6C7A89))
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "내용",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF666666)
                    )
                }
            }
        }

        // 본문 영역
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        "안녕하세요. 더클라임 강남점입니다.\n이번 주 운영시간은 아래와 같이 안내드립니다.\n방문 예정이신 회원분들께서는 참고해 주세요!\n\n" +
                                "[2025년 1월 1일(월) ~ 2025년 1월 7일 (일)]\n" +
                                "월/수 13:00 ~ 22:00 / 정상운영\n" +
                                "화요일 13:00 ~ 22:00 / 정상운영\n" +
                                "목요일 13:00 ~ 22:00 / 정상운영\n" +
                                "금요일 13:00 ~ 22:00 / 푸른 좌석 일부 구역 제한\n" +
                                "토요일 10:00 ~ 20:00 / 정상운영\n" +
                                "일요일 10:00 ~ 20:00 / 푸른운영\n",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF222222)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    // 공지사항(리스트)
                    Text(
                        "\uD83D\uDCCC 공지사항",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFFF2B166))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "1) 1/4(목)은 일부 푸른 작업이 진행되어, 푸른존 A는 이용이 제한됩니다.\n" +
                                "2) 신규 푸른 오픈은 1/5(금) 예정입니다.\n",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF222222)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "더운 날씨에 건강 유의하시고, 즐거운 클라이밍 되시길 바랍니다. 감사합니다.\n\n- 더클라임 강남점 드림 -",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF222222)
                    )
                }
            }
        }
    }
}