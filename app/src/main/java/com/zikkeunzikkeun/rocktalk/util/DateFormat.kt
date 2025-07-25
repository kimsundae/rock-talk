package com.zikkeunzikkeun.rocktalk.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatTimeStampToYYYYMMDD(utcDateString: String?): String {
    return try {
        if (utcDateString.isNullOrBlank()) return ""

        val instant = Instant.parse(utcDateString)
        val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        localDateTime.format(formatter)
    } catch (e: Exception) {
        "" // 파싱 실패 시 빈 문자열 반환
    }
}