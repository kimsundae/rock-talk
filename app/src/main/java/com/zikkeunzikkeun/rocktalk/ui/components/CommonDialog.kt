package com.zikkeunzikkeun.rocktalk.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

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
                        onConfirm?.invoke()      // 확인 콜백 실행
                        onDismiss()
                    }
                ) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onCancel?.invoke()      // 취소 콜백 실행
                        onDismiss()
                    }
                ) {
                    Text(cancelText)
                }
            }
        )
    }
}
