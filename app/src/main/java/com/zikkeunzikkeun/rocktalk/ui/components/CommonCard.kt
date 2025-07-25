package com.zikkeunzikkeun.rocktalk.ui.components

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Menu
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
import androidx.navigation.NavController
import com.zikkeunzikkeun.rocktalk.data.BoardInfoData

@Composable
fun InfoCard(navController: NavController, title: String, icon: ImageVector, items: List<BoardInfoData>, onClickIcon: ()->Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = title, style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "더보기",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onClickIcon() },
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            items.forEach {
                Text(
                    text = "• ${it.boardTitle}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 2.dp)
                    .clickable{
                        if (it.boardId.isNotBlank()) {
                            navController.navigate("board_info_screen?boardId=${it.boardId}") {
                                popUpTo("board_list_screen") { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    }
}


