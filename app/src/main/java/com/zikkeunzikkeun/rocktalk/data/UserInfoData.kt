package com.zikkeunzikkeun.rocktalk.data

data class UserInfoData(
    val userId: String? = "",
    val nickname: String = "",
    val gender: String = "",
    val age: Int = 0,
    val provider: String = "",
    val updatedAt: String = "",
    val profileImageUrl: String? = "",
    val centerId: String = "",
    val centerName: String = "",
    val editYn: Boolean = false
)