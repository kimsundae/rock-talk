package com.zikkeunzikkeun.rocktalk.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import android.content.Context
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.media3.ui.PlayerView
import com.zikkeunzikkeun.rocktalk.api.callSaveRecord
import com.zikkeunzikkeun.rocktalk.api.uploadMediaFileAndGetUrl
import com.zikkeunzikkeun.rocktalk.data.AlertDialogData
import com.zikkeunzikkeun.rocktalk.data.RecordInfoData
import com.zikkeunzikkeun.rocktalk.data.UserInfoData
import com.zikkeunzikkeun.rocktalk.ui.theme.Orange40
import kotlinx.coroutines.launch


@Composable
fun RecordInfoDialog(
    isEdit: Boolean,
    userInfo: UserInfoData?,
    recordInfo: RecordInfoData?,
    isShow: Boolean,
    onDismiss: () -> Unit
) {
    if (isShow) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                tonalElevation = 8.dp,
                shadowElevation = 16.dp,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .wrapContentHeight()
            ) {
                RecordInfoContent(
                    isEdit = isEdit,
                    onDismiss = onDismiss,
                    userInfo = userInfo,
                    recordInfo = recordInfo
                )
            }
        }
    }
}

@Composable
fun RecordInfoContent(
    recordInfo: RecordInfoData?,
    userInfo: UserInfoData?,
    isEdit: Boolean,
    onDismiss: () -> Unit
) {
    // 새로 선택한 미디어
    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) }
    // 폼 입력 상태: recordInfo 초기값 반영
    var recordInfoData by remember {
        mutableStateOf(
            recordInfo ?: RecordInfoData(
                centerId = userInfo?.centerId ?: "",
                userId = userInfo?.userId ?: "",
                nickname = userInfo?.nickname ?: ""
            )
        )
    }
    var isOpenConfirmDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isShowDialog by remember { mutableStateOf(false) }
    var dialogData by remember { mutableStateOf(AlertDialogData { isShowDialog = false }) }
    val context = LocalContext.current

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> selectedMediaUri = uri }

    // 미디어 타입 판별 선택된 새 미디어가 있을 때만 체크
    val selectedMimeType = selectedMediaUri?.let { getMimeType(context, it) }
    val isVideo = selectedMediaUri != null && selectedMimeType?.startsWith("video") == true
    val coroutineScope = rememberCoroutineScope()

    // recordInfo가 바뀌면 입력 필드에 반영
    LaunchedEffect(recordInfo) {
        if (recordInfo != null) {
            recordInfoData = recordInfo
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "닫기",
                    tint = Color(0xFF9D9D9D)
                )
            }
        }
        // 미디어 업로드 박스
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF7F8F1))
                .clickable { pickMediaLauncher.launch("image/* video/*") }
                .border(
                    width = 2.dp,
                    color = Color(0xFFE1E1E1),
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {

            when {
                selectedMediaUri != null -> {
                    if (isVideo) {
                        VideoPlayerPreview(uri = selectedMediaUri!!)
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(selectedMediaUri),
                            contentDescription = "미리보기",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                !recordInfoData.recordMediaUri.isNullOrBlank() -> {
                    if (recordInfoData.recordMediaUri.endsWith(".mp4") || recordInfoData.recordMediaUri.contains("/videos/")) {
                        // Video
                        VideoPlayerPreview(url = recordInfoData.recordMediaUri)
                    } else {
                        // Image
                        Image(
                            painter = rememberAsyncImagePainter(recordInfoData.recordMediaUri),
                            contentDescription = "기존 기록 미디어",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                else -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "이미지 또는 동영상 추가",
                            color = Color(0xFFC2C2C2),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        OutlinedTextField(
            value = recordInfoData.recordContent,
            onValueChange = { recordInfoData = recordInfoData.copy(recordContent = it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("오늘의 기록을 남겨주세요", color = Color(0xFFB0B0B0)) },
            maxLines = 5,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFECB764),
                unfocusedBorderColor = Color(0xFFE1E1E1)
            )
        )
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "내 게시판 공유",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5A6953)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = recordInfoData.isBoardShare,
                    onCheckedChange = { recordInfoData = recordInfoData.copy(isBoardShare = it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFECB764),
                        checkedTrackColor = Color(0xFFFFEBC2)
                    )
                )
            }
        }
        Spacer(Modifier.height(28.dp))
        Button(
            onClick = { isOpenConfirmDialog = true },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange40,
                contentColor = Color.White
            )
        ) {
            Text("저장", style = MaterialTheme.typography.titleMedium)
        }
    }
    CommonProgressConfrimDialog(
        isShow = isOpenConfirmDialog,
        onDismiss = {},
        title = "알림",
        text = "저장하시겠습니까?",
        confirmText = "확인",
        cancelText = "취소",
        onConfirm = {
            coroutineScope.launch {
                isLoading = true
                try {
                    val mediaUrl = selectedMediaUri?.let { uri ->
                        val fileType = if (isVideo) "video" else "img"
                        uploadMediaFileAndGetUrl(
                            context,
                            uri,
                            "records",
                            fileType
                        )
                    } ?: recordInfoData.recordMediaUri // 새 미디어 없으면 기존 URL 유지

                    val newRecordInfo = recordInfoData.copy(
                        recordMediaUri = mediaUrl,
                        userId = userInfo?.userId ?: "",
                        nickname = userInfo?.nickname ?: "",
                        centerId = userInfo?.centerId ?: ""
                    )
                    val saveSuccess = callSaveRecord(newRecordInfo)
                    isLoading = false
                    if (saveSuccess) {
                        dialogData = AlertDialogData(
                            title = "알림",
                            text = "저장에 성공했습니다.",
                            buttonText = "확인"
                        ) {
                            isShowDialog = false
                            onDismiss()
                        }
                        isShowDialog = true
                    } else {
                        dialogData = AlertDialogData(
                            title = "알림",
                            text = "저장에 실패했습니다.",
                            buttonText = "확인"
                        ) {
                            isShowDialog = false
                        }
                        isShowDialog = true
                    }
                } catch (e: Exception) {
                    isLoading = false
                    dialogData = AlertDialogData(
                        title = "오류",
                        text = "예상치 못한 오류가 발생했습니다.\n${e.localizedMessage ?: ""}",
                        buttonText = "확인"
                    ) {
                        isShowDialog = false
                    }
                    isShowDialog = true
                }
            }
        },
        isLoading = isLoading
    )
    CommonAlertDialog(
        isShow = isShowDialog,
        onDismiss = dialogData.onDismiss,
        title = dialogData.title,
        text = dialogData.text,
        buttonText = dialogData.buttonText
    )
}


@Composable
fun VideoPlayerPreview(uri: Uri? = null, url: String? = null) {
    val context = LocalContext.current
    val exoPlayer = remember(context, uri, url) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = when {
                uri != null -> MediaItem.fromUri(uri)
                url != null -> MediaItem.fromUri(url)
                else -> null
            }
            if (mediaItem != null) {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = false
            }
        }
    }
    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }
    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = true
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
// MimeType 판별 함수
private fun getMimeType(context: Context, uri: Uri): String? {
    return context.contentResolver.getType(uri)
        ?: MimeTypeMap.getFileExtensionFromUrl(uri.toString())?.let {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(it)
        }
}
