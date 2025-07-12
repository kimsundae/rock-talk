package com.zikkeunzikkeun.rocktalk.api

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

fun getKakaoUserInfo(accessToken: String){
    requestApi(ApiUrlList.URL_KAKAO_USER_INFO, accessToken, object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // 실패 처리
            Log.i("response", "내부에서 정의: kakao fail")
        }
        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val body = response.body?.string()
                Log.i("response", "내부에서 정의: $body")
            }
        }
    })
}

fun getNaverUserInfo(accessToken: String){
    requestApi(ApiUrlList.URL_NAVER_USER_INFO, accessToken, object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // 실패 처리
            Log.i("response", "내부에서 정의: kakao fail")
        }
        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val body = response.body?.string()
                Log.i("response", "내부에서 정의: $body")
            }
        }
    })
}

fun requestApi(requestUrl: String, accessToken: String, callback: Callback){
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(requestUrl) // 예시: 카카오 사용자 정보 API
        .addHeader("Authorization", "Bearer $accessToken")
        .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
        .build()

    client.newCall(request).enqueue(callback);
}