package com.zikkeunzikkeun.rocktalk.data

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class RecordInfoData(
    val centerId:String = "",
    val recordId:String = "",
    val userId:String = "",
    val nickname:String = "",
    val recordMediaUri:String = "",
    val thumbnailUri:String = "",
    val recordContent:String = "",
    val isBoardShare: Boolean = true,
    val createDate:String = ""
){
    fun getLocalDateTime(): LocalDateTime? = try {
        if (createDate.isBlank()) null
        else LocalDateTime.ofInstant(
            Instant.parse(createDate),
            ZoneId.systemDefault()
        )
    } catch (e: Exception) { null }

    // "yyyy-MM-dd" 포맷의 로컬 날짜 문자열 반환
    fun getLocalDateString(): String? = getLocalDateTime()?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}
