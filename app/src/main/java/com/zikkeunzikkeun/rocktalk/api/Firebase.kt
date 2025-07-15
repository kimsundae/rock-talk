package com.zikkeunzikkeun.rocktalk.api

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.storage
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


fun uploadToFirebaseStorage(context: Context, fileUri: Uri, filePath: String, fileType: String, onResult: (String?) -> Unit) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val fileName = "${filePath}/${System.currentTimeMillis()}_${fileType}"
    val fileRef = storageRef.child(fileName)

    // Storage에 업로드
    fileRef.putFile(fileUri)
        .addOnSuccessListener {
            // 업로드 성공 → 다운로드 URL 얻기
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                onResult(uri.toString())
            }.addOnFailureListener {
                onResult(null)
            }
        }
        .addOnFailureListener {
            onResult(null)
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
    userId: String,
    age: String,
    gender: String,
    nickname: String,
    center: String,
    profileImageUrl: String?
): Boolean = withContext(Dispatchers.IO) {
    val data = hashMapOf(
        "userId" to userId,
        "age" to age,
        "gender" to gender,
        "nickname" to nickname,
        "center" to center
    )
    if (!profileImageUrl.isNullOrBlank()) {
        data["profileImageUrl"] = profileImageUrl
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
