package com.zikkeunzikkeun.rocktalk.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.zikkeunzikkeun.rocktalk.api.getUserInfo
import com.zikkeunzikkeun.rocktalk.data.UserInfoData
import com.zikkeunzikkeun.rocktalk.ui.components.CommonProgress
import com.zikkeunzikkeun.rocktalk.util.getUserId
import androidx.compose.runtime.*

@Composable
fun RockTalkRoot() {
    var userInfo by remember { mutableStateOf<UserInfoData?>(null) }
    var isUserInfoLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val userId = getUserId()
        if (!userId.isNullOrEmpty()) {
            userInfo = getUserInfo(userId)
        }
        isUserInfoLoading = false
    }

    if (isUserInfoLoading) {
        CommonProgress(isLoading = true)
    } else {
        RockTalkApp(
            userInfo = userInfo,
            setUserInfo = { userInfo = it }
        )
    }
}