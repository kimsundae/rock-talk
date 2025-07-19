package com.zikkeunzikkeun.rocktalk.dto

data class AlertDialogData(
    val title: String = "",
    val text: String = "",
    val buttonText: String = "확인",
    val onDismiss: () -> Unit = {}
)
