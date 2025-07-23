package com.zikkeunzikkeun.rocktalk.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zikkeunzikkeun.rocktalk.data.BoardInfoData
import com.zikkeunzikkeun.rocktalk.ui.theme.LightGreen40
import com.zikkeunzikkeun.rocktalk.ui.theme.Orange40

@Composable
fun BoardListScreen(
    navController:NavController,
    userId: String?,
    userName: String?,
    boardType: String?,
    centerId: String?,
    centerName: String?
){
    var items by remember { mutableStateOf(listOf(
        BoardInfoData(boardTitle = "운영시간 안내! 이번 주 운영 일정 확인하세요!", registerName = "더클라임 강남점"),
        BoardInfoData(boardTitle = "이벤트: 이 달의 챌린지 완료하고 선물 받자!", registerName = "더클라임 강남점"),
        BoardInfoData(boardTitle = "새로운 장비! 나이로이벤트 무료 개방 안내", registerName = "더클라임 강남점"),
        BoardInfoData(boardTitle = "주의공지! 사고 예방을 위한 기본 규칙", registerName = "더클라임 강남점")
    ))}

    Box(modifier = Modifier.fillMaxWidth()) {
        // 내용 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LightGreen40),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // 상단 타이틀
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(if (boardType == "0") Icons.Default.Notifications else Icons.Default.Face , contentDescription = null, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (boardType == "0") "공지사항" else "함께해요",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start // SpaceBetween 대신 Start로!
                ) {
                    Text(
                        "제목",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(0.7f) // 70%
                    )
                    Text(
                        "등록자",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(0.3f) // 30%
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 목록
                items.forEach { boardInfo ->
                    @OptIn(ExperimentalMaterial3Api::class)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(6.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        onClick = {
                            navController.navigate("board_info_screen?centerId=${boardInfo.centerId}&userId=${userId}&boardId=${boardInfo.boardId}") {
                                popUpTo("board_list_screen") { inclusive = true }
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 12.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = boardInfo.boardTitle,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                modifier = Modifier.weight(0.7f)
                            )
                            Text(
                                text = boardInfo.registerName,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                modifier = Modifier.weight(0.3f)
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                navController.navigate("board_regist_screen?centerId=${centerId}&userId=${userId}&boardType=${boardType}") {
                    popUpTo("board_list_screen") { inclusive = true }
                }
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp)
                .height(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange40,
                contentColor = Color.White
            )
        ) {
            Text("글 등록하기", style = MaterialTheme.typography.labelMedium)
        }
    }
}