package com.zikkeunzikkeun.rocktalk.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zikkeunzikkeun.rocktalk.api.getCenterList
import com.zikkeunzikkeun.rocktalk.dto.CenterInfoData
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.zikkeunzikkeun.rocktalk.R
import com.zikkeunzikkeun.rocktalk.ui.theme.LightGray40

@Composable
fun CommonAlertDialog(
    isShow: Boolean,
    onDismiss: () -> Unit,
    title: String = "",
    text: String = "",
    buttonText: String = "확인"
){
    if (isShow) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Text(text) },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(buttonText)
                }
            }
        )
    }
}

@Composable
fun CommonConfirmDialog(
    isShow: Boolean,
    onDismiss: () -> Unit,
    title: String = "",
    text: String = "",
    confirmText: String = "확인",
    cancelText: String = "취소",
    onConfirm: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null
) {
    if (isShow) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Text(text) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm?.invoke()
                        onDismiss()
                    }
                ) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onCancel?.invoke()
                        onDismiss()
                    }
                ) {
                    Text(cancelText)
                }
            }
        )
    }
}

@Composable
fun CommonCenterSelectDialog(
    isShow: Boolean,
    onDismiss: () -> Unit,
    onCenterClick: (CenterInfoData) -> Unit
){
    val title = "센터 목록"
    val buttonText = "닫기"
    var centerList by remember { mutableStateOf<List<CenterInfoData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(isShow) {
        if (isShow) {
            isLoading = true
            centerList = getCenterList() ?: emptyList()
            isLoading = false
        }
    }
    if (isShow) {
        when{
            isLoading -> CommonProgress(isLoading = isLoading)
            else -> {
                AlertDialog(
                    onDismissRequest = onDismiss,
                    title = { Text(title) },
                    text = {
                        Box(
                            modifier = Modifier
                                .heightIn(max = 300.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Column {
                                Divider(
                                    color = Color.LightGray,
                                    thickness = 1.dp
                                )

                                centerList.forEach { center ->
                                    CenterRow(center, onCenterClick)
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = onDismiss) {
                            Text(buttonText)
                        }
                    },
                    containerColor = Color.White
                )
            }
        }
    }
}

@Composable
fun CenterRow(
    centerInfo: CenterInfoData,
    onCenterClick: (CenterInfoData) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onCenterClick(centerInfo) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 이미지 아이콘
            Image(
                painter = painterResource(id = R.drawable.rock_icon),
                contentDescription = "센터 아이콘",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
            )

            // 텍스트 정보
            Column {
                Text(
                    text = centerInfo.centerName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = centerInfo.centerAddress,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        // 하단 연한 회색 구분선
        Divider(color = LightGray40, thickness = 1.dp)
    }
}
