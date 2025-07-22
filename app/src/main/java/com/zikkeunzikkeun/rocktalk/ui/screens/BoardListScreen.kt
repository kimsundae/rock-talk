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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.zikkeunzikkeun.rocktalk.ui.theme.Orange40

@Composable
fun BoardListScreen(
    title: String,
    icon: ImageVector?,
    items: List<Pair<String, String>>?,
    onWriteClick: () -> Unit?
){
    Box(modifier = Modifier.fillMaxWidth()) {
        // 내용 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // 상단 타이틀
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 제목 / 등록자 헤더
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("제목", style = MaterialTheme.typography.labelSmall)
                    Text("등록자", style = MaterialTheme.typography.labelSmall)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 목록
                items?.forEach { (subject, author) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = subject,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = author,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Button(
            onClick = {},
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp)
                .height(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange40,       // ✅ 배경색
                contentColor = Color.White       // ✅ 텍스트 색상
            )
        ) {
            Text("글 등록하기", style = MaterialTheme.typography.labelMedium)
        }
    }
}