package com.zikkeunzikkeun.rocktalk.data

data class BoardInfoData(
    val centerId:String = "",
    val centerName:String = "",
    val boardId:String = "",
    val boardTitle:String = "",
    val boardContent:String = "",
    val boardType:String = "",
    val createDate:String = "",
    val updateDate:String = "",
    val registerId:String = "",
    val registerName:String = "",
    val deleteYn:Boolean = false
)
