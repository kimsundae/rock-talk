package com.zikkeunzikkeun.rocktalk.api

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.storage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zikkeunzikkeun.rocktalk.data.BoardInfoData
import com.zikkeunzikkeun.rocktalk.data.CenterInfoData
import com.zikkeunzikkeun.rocktalk.data.RecordInfoData
import com.zikkeunzikkeun.rocktalk.data.UserInfoData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.collections.get

private val userInfoCache = mutableMapOf<String, UserInfoData?>()
private var centerInfoListCache: List<CenterInfoData>? = null

fun clearUserInfoCache(){ userInfoCache.clear() }
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
            val functionName = when (provider.uppercase()) {
                "KAKAO" -> "kakaoCustomAuth"
                "NAVER" -> "naverCustomAuth"
                else -> throw IllegalArgumentException("Unsupported provider: $provider")
            }

            val result = functions
                .getHttpsCallable(functionName)
                .call(mapOf("token" to accessToken))
                .await()

            val customToken = (result.data as Map<*, *>)["custom_token"] as String
            Log.d("FirebaseAuth", "받은 커스텀 토큰: $customToken")

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

suspend fun uploadMediaFileAndGetUrl(
    context: Context,
    fileUri: Uri,
    filePath: String,
    fileType: String
): String? = withContext(Dispatchers.IO) {
    try {
        val mime = context.contentResolver.getType(fileUri)
        val ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(mime) ?: "file"
        val fileName = "${filePath}/${System.currentTimeMillis()}_${fileType}.$ext"
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
    userInfoData: UserInfoData
): Boolean = withContext(Dispatchers.IO) {
    val gson = Gson()
    val json = gson.toJson(userInfoData)
    val mapType = object : TypeToken<Map<String, Any?>>() {}.type
    val data: MutableMap<String, Any?> = gson.fromJson(json, mapType)

    if (!userInfoData.profileImageUrl.isNullOrBlank()) {
        data["profileImageUrl"] = userInfoData.profileImageUrl
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

suspend fun getUserInfo(userId: String): UserInfoData? = withContext(Dispatchers.IO) {
    // 캐시 확인
    userInfoCache[userId]?.let { return@withContext it.copy() }

    val data = hashMapOf("userId" to userId)
    return@withContext try {
        val functions = FirebaseFunctions.getInstance("asia-northeast3")
        val result = Tasks.await(
            functions.getHttpsCallable("getUserInfo").call(data)
        )
        val map = result.data as? Map<*, *>
        val userMap = map?.get("user") as? Map<*, *>
        val gson = Gson()
        val jsonString = gson.toJson(userMap)
        val userInfo = gson.fromJson(jsonString, UserInfoData::class.java)

        // 캐시에 저장
        userInfoCache[userId] = userInfo

        userInfo.copy()
    } catch (e: Exception) {
        Log.e("getUserInfo error", e.toString())
        null
    }
}

suspend fun getCenterList(): List<CenterInfoData>? = withContext(Dispatchers.IO) {

    centerInfoListCache?.let { return@withContext it }

    return@withContext try {
        val functions = FirebaseFunctions.getInstance("asia-northeast3")
        val result = Tasks.await(
            functions.getHttpsCallable("getCenterList").call()
        )

        val data = result.data as? Map<*, *>
        val centerListRaw = data?.get("centerData") as? List<Map<String, Any>>

        if (centerListRaw != null) {
            val gson = Gson()
            val jsonString = gson.toJson(centerListRaw)
            val type = object : TypeToken<List<CenterInfoData>>() {}.type
            val centerList: List<CenterInfoData> = gson.fromJson(jsonString, type)

            centerInfoListCache = centerList

            centerList
        } else {
            null
        }
    } catch (e: Exception){
        Log.e("getCenterList error", e.toString())
        null
    }
}

suspend fun saveBoard(boardInfoData: BoardInfoData) = withContext(Dispatchers.IO){

    return@withContext try{
        val gson = Gson()
        val json = gson.toJson(boardInfoData)
        val mapType = object : TypeToken<Map<String, Any?>>() {}.type
        val data: MutableMap<String, Any?> = gson.fromJson(json, mapType)

        val functions = FirebaseFunctions.getInstance("asia-northeast3")
        Tasks.await(
            functions
                .getHttpsCallable("saveBoard")
                .call(data)
        )
        true
    }catch (e: Exception){
        Log.e("saveBoard error", e.toString())
        false
    }
}

suspend fun getBoardById(boardId: String): BoardInfoData? = withContext(Dispatchers.IO) {
    return@withContext try {
        val data = mapOf("boardId" to boardId)
        val functions = FirebaseFunctions.getInstance("asia-northeast3")
        Log.i("getBoardById", boardId)
        val result = Tasks.await(
            functions
                .getHttpsCallable("getBoardById")
                .call(data)
        )

        val resultMap = result.data as? Map<*, *>
        val boardMap = resultMap?.get("board") as? Map<*, *>

        val gson = Gson()
        val json = gson.toJson(boardMap)
        gson.fromJson(json, BoardInfoData::class.java)
    } catch (e: Exception) {
        Log.e("getBoardById error", e.toString())
        null
    }
}

suspend fun getBoardList(boardType: String?, centerId: String): List<BoardInfoData> = withContext(Dispatchers.IO) {
    return@withContext try {
        val data = mapOf(
            "boardType" to boardType,
            "centerId" to centerId
        )

        val functions = FirebaseFunctions.getInstance("asia-northeast3")
//        functions.useEmulator("10.0.2.2", 5001)
        val result = Tasks.await(
            functions
                .getHttpsCallable("getBoardList")
                .call(data)
        )

        val resultMap = result.data as? Map<*, *>
        val boardListRaw = resultMap?.get("boardList") as? List<*>

        val gson = Gson()
        val json = gson.toJson(boardListRaw)
        val type = object : TypeToken<List<BoardInfoData>>() {}.type
        Log.i("getBoardList", gson.fromJson<List<BoardInfoData>>(json, type).toString())
        gson.fromJson<List<BoardInfoData>>(json, type)
    } catch (e: Exception) {
        Log.e("getBoardList error", e.toString())
        emptyList<BoardInfoData>()
    }
}

suspend fun callSaveRecord(recordInfo: RecordInfoData): Boolean = withContext(Dispatchers.IO) {
    return@withContext try {

        val gson = Gson()
        val json = gson.toJson(recordInfo)
        val mapType = object : TypeToken<Map<String, Any?>>() {}.type
        val data: MutableMap<String, Any?> = gson.fromJson(json, mapType)

        val functions = FirebaseFunctions.getInstance("asia-northeast3")
        Tasks.await(
            functions
                .getHttpsCallable("saveRecord")
                .call(data)
        )
        true
    } catch (e: Exception) {
        Log.e("callSaveRecordCloudFunction error", e.toString())
        false
    }
}

suspend fun callGetRecord(userId: String, recordId: String): RecordInfoData = withContext(Dispatchers.IO) {
    return@withContext try {
        val data = mapOf(
            "userId" to userId,
            "recordId" to recordId
        )
        val functions = FirebaseFunctions.getInstance("asia-northeast3")
        val result = Tasks.await(
            functions
                .getHttpsCallable("getRecord")
                .call(data)
        )

        val resultMap = result.data as? Map<*, *>
        val recordMap = resultMap?.get("record") as? Map<*, *>

        if (recordMap != null) {
            val gson = Gson()
            val json = gson.toJson(recordMap)
            gson.fromJson(json, RecordInfoData::class.java)
        } else {
            RecordInfoData()
        }
    } catch (e: Exception) {
        Log.e("callGetRecord error", e.toString())
        RecordInfoData()
    }
}

suspend fun callGetRecordList(
    userId: String? = null,
    centerId: String? = null
): List<RecordInfoData> = withContext(Dispatchers.IO) {
    return@withContext try {
        val data = mutableMapOf<String, Any?>()
        userId?.let { data["userId"] = it }
        centerId?.let { data["centerId"] = it }

        val functions = FirebaseFunctions.getInstance("asia-northeast3")
        val result = Tasks.await(
            functions
                .getHttpsCallable("getRecordList")
                .call(data)
        )

        val resultMap = result.data as? Map<*, *>
        val recordListRaw = resultMap?.get("recordList") as? List<*>

        if (recordListRaw != null) {
            val gson = Gson()
            val json = gson.toJson(recordListRaw)
            val type = object : TypeToken<List<RecordInfoData>>() {}.type
            gson.fromJson<List<RecordInfoData>>(json, type)
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        Log.e("callGetRecordList error", e.toString())
        emptyList()
    }
}