package com.example.zippick.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileUtil {
    fun getFileFromUri(context: Context, uri: Uri): File {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val file = File.createTempFile("upload", ".jpg", context.cacheDir)
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                file
            } ?: throw IOException("InputStream is null for uri: $uri")
        } catch (e: SecurityException) {
            throw IOException("권한이 없는 파일 접근 시도: $uri", e)
        }
    }
}
