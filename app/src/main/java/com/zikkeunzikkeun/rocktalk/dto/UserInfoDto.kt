package com.zikkeunzikkeun.rocktalk.dto

data class UserInfoDto(
    val userId: String? = "",
    val nickname: String = "",
    val gender: String = "",
    val age: Int = 0,
    val provider: String = "",
    val updatedAt: String = "",
    val profileImageUrl: String? = "",
    val center: String = "",
    val editYn: Boolean = false
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UserInfoDto
        return userId == other.userId
    }
    override fun hashCode(): Int {
        return userId.hashCode()
    }
}