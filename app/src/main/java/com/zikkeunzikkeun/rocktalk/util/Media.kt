package com.zikkeunzikkeun.rocktalk.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun generateThumbnailFromVideo(context: Context, videoUri: Uri): Uri? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, videoUri)
        val bitmap = retriever.getFrameAtTime(0)
        retriever.release()

        // Bitmap을 임시 파일로 저장
        val file = File.createTempFile("thumb", ".jpg", context.cacheDir)
        val out = FileOutputStream(file)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
        out.close()

        Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}