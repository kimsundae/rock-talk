package com.zikkeunzikkeun.rocktalk.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zikkeunzikkeun.rocktalk.api.getBoardList
import com.zikkeunzikkeun.rocktalk.data.BoardInfoData
import com.zikkeunzikkeun.rocktalk.ui.components.CommonProgress
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
    var boardList: List<BoardInfoData> by remember { mutableStateOf(emptyList<BoardInfoData>()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        val boardListData = getBoardList(boardType ?: "", centerId = centerId ?: "")
        boardList = boardListData
        isLoading = false
    }

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
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        "제목",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(0.7f)
                    )
                    Text(
                        "등록자",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(0.3f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 목록
                boardList.forEach { boardInfoData ->
                    @OptIn(ExperimentalMaterial3Api::class)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(6.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        onClick = {
                            navController.navigate("board_info_screen?userId=${userId}&boardId=${boardInfoData.boardId}") {
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
                                text = boardInfoData.boardTitle,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                modifier = Modifier.weight(0.7f)
                            )
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(16.dp)
                                    .padding(horizontal = 8.dp)
                                    .background(Color.LightGray.copy(alpha = 0.5f))
                            )
                            Text(
                                text = boardInfoData.registerName,
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
                navController.navigate("board_regist_screen?centerId=${centerId}&userId=${userId}&userName=${userName}&boardType=${boardType}") {
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
    CommonProgress(isLoading = isLoading)
}