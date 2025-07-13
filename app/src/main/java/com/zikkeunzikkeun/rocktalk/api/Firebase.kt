package com.zikkeunzikkeun.rocktalk.api

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.collections.get

fun firebaseLoginWithProviderToken(
    provider: String,
    accessToken: String,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val functions = FirebaseFunctions.getInstance("asia-northeast3")
//    functions.useEmulator("10.0.2.2", 5001)
    val auth = FirebaseAuth.getInstance()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            // provider에 따라 Function 이름 결정
            val functionName = when (provider.uppercase()) {
                "KAKAO" -> "kakaoCustomAuth"
                "NAVER" -> "naverCustomAuth"
                else -> throw IllegalArgumentException("Unsupported provider: $provider")
            }

            // Firebase Function 호출
            val result = functions
                .getHttpsCallable(functionName)
                .call(mapOf("token" to accessToken))
                .await()

            val customToken = (result.data as Map<*, *>)["custom_token"] as String
            Log.d("FirebaseAuth", "받은 커스텀 토큰: $customToken")

            // Firebase 로그인
            auth.signInWithCustomToken(customToken).await()
            withContext(Dispatchers.Main) {
                onSuccess()
                Log.d("FirebaseAuth", "Firebase 로그인 성공! UID: ${auth.currentUser?.uid}")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onFailure(e)
                Log.e("FirebaseAuth", "Firebase 로그인 실패", e)
            }
        }
    }
}
