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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zikkeunzikkeun.rocktalk.api.saveBoard
import com.zikkeunzikkeun.rocktalk.data.AlertDialogData
import com.zikkeunzikkeun.rocktalk.data.BoardInfoData
import com.zikkeunzikkeun.rocktalk.ui.components.CommonAlertDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonConfirmDialog
import com.zikkeunzikkeun.rocktalk.ui.components.CommonProgress
import com.zikkeunzikkeun.rocktalk.ui.theme.LightGreen40
import kotlinx.coroutines.launch

@Composable
fun BoardRegistScreen(
    navController: NavController,
    centerId:String?,
    userId:String?,
    userName:String?,
    boardType:String?
){
    var registBoardInfo by remember { mutableStateOf(BoardInfoData(
        centerId = centerId ?: "",
        boardType = boardType ?: "",
        registerId = userId ?: "",
        registerName = userName ?: "")
    )}
    var isOpenDialog by remember { mutableStateOf(false) }
    var isOpenSaveDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var dialogData by remember { mutableStateOf(AlertDialogData(){isOpenDialog = false}) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(5.dp))
                        .background(
                            color = LightGreen40,
                        )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(top = 20.dp, start = 10.dp)
                            ) {
                                Icon(
                                    if (boardType == "0") Icons.Default.Notifications else Icons.Default.Face,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    if(boardType == "0") "공지사항" else "함께해요",
                                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 26.sp, fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = (-30).dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    "제목",
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .width(40.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .fillMaxWidth()
                                        .border(
                                            width = 1.dp,
                                            color = Color(0xFFDDDDDD),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .background(Color.White, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    BasicTextField(
                                        value = registBoardInfo.boardTitle,
                                        onValueChange = { title -> registBoardInfo = registBoardInfo.copy(boardTitle = title) },
                                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        decorationBox = { innerTextField ->
                                            if (registBoardInfo.boardTitle.isEmpty()) {
                                                Text(
                                                    text = "제목을 입력하세요",
                                                    color = Color.Gray,
                                                    fontSize = 16.sp
                                                )
                                            }
                                            innerTextField()
                                        }
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    "내용",
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .width(40.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .height(250.dp)
                                        .fillMaxWidth()
                                        .border(
                                            width = 1.dp,
                                            color = Color(0xFFDDDDDD),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .background(Color.White, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    BasicTextField(
                                        value = registBoardInfo.boardContent,
                                        onValueChange = { content -> registBoardInfo = registBoardInfo.copy(boardContent = content)},
                                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        decorationBox = { innerTextField ->
                                            if (registBoardInfo.boardContent.isEmpty()) {
                                                Text(
                                                    text = "내용을 입력하세요",
                                                    color = Color.Gray,
                                                    fontSize = 16.sp
                                                )
                                            }
                                            innerTextField()
                                        }
                                    )
                                }
                            }
                        }

                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        when {
                            registBoardInfo.boardTitle.isNullOrBlank() -> {
                                dialogData = dialogData.copy(
                                    title = "알림",
                                    text = "제목을 입력해주세요."
                                )
                                isOpenDialog = true
                                return@Button
                            }
                            registBoardInfo.boardContent.isNullOrBlank() -> {
                                dialogData = dialogData.copy(
                                    title = "알림",
                                    text = "내용을 입력해주세요."
                                )
                                isOpenDialog = true
                                return@Button
                            }
                        }
                        isOpenSaveDialog = true
                    },
                    shape = RoundedCornerShape(30),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF2B166),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .width(150.dp)
                        .height(48.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("글 등록하기", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
    CommonAlertDialog(
        isShow = isOpenDialog,
        onDismiss = dialogData.onDismiss,
        title = dialogData.title,
        text = dialogData.text
    )
    CommonConfirmDialog(
        isShow = isOpenSaveDialog,
        onDismiss = {isOpenSaveDialog = false},
        title = "알림",
        text = "저장하시겠습니까?",
        confirmText = "확인",
        cancelText = "취소",
        onConfirm = {
            coroutineScope.launch {
                isLoading = true
                val result = saveBoard(registBoardInfo)
                dialogData = dialogData.copy(title = "알림", text = if(result) "게시글이 저장되었습니다." else "일시적인 오류가 발생되어 저장에 실패하였습니다."){
                    navController.navigateUp()
                }
                isLoading = false
                isOpenDialog = true
            }
        }
    )
    CommonProgress(isLoading = isLoading)
}