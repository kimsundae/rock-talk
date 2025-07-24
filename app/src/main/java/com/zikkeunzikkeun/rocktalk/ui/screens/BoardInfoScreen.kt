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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zikkeunzikkeun.rocktalk.api.getBoardById
import com.zikkeunzikkeun.rocktalk.data.BoardInfoData
import com.zikkeunzikkeun.rocktalk.ui.components.CommonProgress

@Composable
fun BoardInfoScreen(
    navController: NavController,
    boardId:String?,
    userId:String?
){
    var boardInfo by remember { mutableStateOf<BoardInfoData>(BoardInfoData()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        val boardInfoData = getBoardById(boardId = boardId ?: "") ?: BoardInfoData()
        boardInfo = boardInfoData
        isLoading = false
    }
    Column(
        modifier = Modifier
//            .background(color = Color(0xFFF8F6F7)) // 전체 연핑크 배경
            .fillMaxSize()
    ) {
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
                        if (boardInfo.boardType == "0") Icons.Default.Notifications else Icons.Default.Face,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFF6595B5)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (boardInfo.boardType == "0") "공지사항" else "함께해요",
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
                            boardInfo.boardTitle,
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
                            boardInfo.createDate,
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
                            boardInfo.registerName,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF6C7A89))
                        )
                    }
                }
            }
        }

        if(!isLoading){
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
                            boardInfo.boardContent,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF222222)
                        )
                    }
                }
            }
        }
        CommonProgress(isLoading = isLoading)
    }
}