package com.zikkeunzikkeun.rocktalk.api

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.storage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zikkeunzikkeun.rocktalk.dto.UserInfoDto
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

suspend fun uploadProfileImageAndGetUrl(
    fileUri: Uri,
    filePath: String,
    fileType: String
): String? = withContext(Dispatchers.IO) {
    try {
        val fileName = "${filePath}/${System.currentTimeMillis()}_${fileType}"
        val storageRef = Firebase.storage.reference.child(fileName)
        val uploadTask = storageRef.putFile(fileUri)
        Tasks.await(uploadTask)
        val urlTask = storageRef.downloadUrl
        Tasks.await(urlTask)
        urlTask.result.toString()
    } catch (e: Exception) {
        Log.e("upload error", e.toString())
        null
    }
}

suspend fun callUpdateUserInfoCloudFunction(
    userInfoDto: UserInfoDto
): Boolean = withContext(Dispatchers.IO) {
    val gson = Gson()
    val json = gson.toJson(userInfoDto)
    val mapType = object : TypeToken<Map<String, Any?>>() {}.type
    val data: MutableMap<String, Any?> = gson.fromJson(json, mapType)

    if (!userInfoDto.profileImageUrl.isNullOrBlank()) {
        data["profileImageUrl"] = userInfoDto.profileImageUrl
    }

    try {
        val functions = FirebaseFunctions.getInstance("asia-northeast3")
        Tasks.await(
            functions
                .getHttpsCallable("updateUserInfo")
                .call(data)
        )
        true
    } catch (e: Exception) {
        Log.e("update error", e.toString())
        false
    }
}

suspend fun getUserInfo(userId: String): UserInfoDto? = withContext(Dispatchers.IO) {
    val data = hashMapOf("userId" to userId)
    Log.i("userid", userId)
    return@withContext try {
        val functions = FirebaseFunctions.getInstance("asia-northeast3")
        val result = Tasks.await(
            functions.getHttpsCallable("getUserInfo").call(data)
        )
        val map = result.data as? Map<*, *>
        val gson = Gson()
        val jsonString = gson.toJson(map)
        gson.fromJson(jsonString, UserInfoDto::class.java)
    } catch (e: Exception) {
        Log.e("getUserInfo error", e.toString())
        null
    }
}
