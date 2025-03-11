package com.example.administrator.live.core.repository

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import timber.log.Timber
import java.io.File
import java.io.IOException

enum class MediaType {
    VIDEO,
    IMAGE
}

class MediaRepository(private val context: Context) {
    fun saveFileToMediaStore(file: File, type: MediaType) {
        if (file.exists()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ 的存储方法
                val contentResolver = context.contentResolver
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, file.name) // 文件名
                    put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(type)) // 文件类型
                    put(MediaStore.MediaColumns.RELATIVE_PATH, getRelativePath(type)) // 保存路径
                    put(
                        MediaStore.MediaColumns.DATE_ADDED,
                        System.currentTimeMillis() / 1000
                    ) // 文件添加时间
                }

                val uri: Uri? = when (type) {
                    MediaType.VIDEO -> contentResolver.insert(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        values
                    )

                    MediaType.IMAGE -> contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values
                    )
                }

                uri?.let {
                    try {
                        // 写入文件到 MediaStore
                        contentResolver.openOutputStream(it)?.use { outputStream ->
                            file.inputStream().use { inputStream ->
                                inputStream.copyTo(outputStream) // 复制文件内容
                            }
                        }
                        Timber.d("$type 已成功保存到 MediaStore: $uri")

                        // 删除原文件
                        if (file.delete()) {
                            Timber.d("原文件已成功删除: ${file.absolutePath}")
                        } else {
                            Timber.e("删除原文件失败: ${file.absolutePath}")
                        }

                    } catch (e: IOException) {
                        Timber.e("保存 $type 到 MediaStore 时出错: ${e.message}")
                    }
                } ?: run {
                    Timber.e("无法获取 MediaStore URI")
                }
            } else {
                // Android 9 以下的存储方法
                val destFile = File(context.getExternalFilesDir(null), file.name)
                try {
                    file.copyTo(destFile, overwrite = true) // 复制文件到目标位置
                    Timber.d("$type 已成功保存到: ${destFile.absolutePath}")

                    // 删除原文件
                    if (file.delete()) {
                        Timber.d("原文件已成功删除: ${file.absolutePath}")
                    } else {
                        Timber.e("删除原文件失败: ${file.absolutePath}")
                    }

                } catch (e: IOException) {
                    Timber.e("保存 $type 到本地时出错: ${e.message}")
                }
            }
        } else {
            Timber.e("$type 文件不存在: ${file.absolutePath}")
        }
    }

    // 获取 MIME 类型
    private fun getMimeType(type: MediaType): String {
        return when (type) {
            MediaType.VIDEO -> "video/mp4"
            MediaType.IMAGE -> "image/jpeg"
        }
    }

    // 获取相对路径
    private fun getRelativePath(type: MediaType): String {
        return when (type) {
            MediaType.VIDEO -> "Movies/Live"
            MediaType.IMAGE -> "Pictures/Live"
        }
    }

    fun deleteFileFromMediaStore(filePath: String, type: MediaType): Boolean {
        val contentResolver: ContentResolver = context.contentResolver
        val file = File(filePath)

        if (!file.exists()) {
            Timber.e("文件不存在: ${file.absolutePath}")
            return false
        }

        val selection: String
        val selectionArgs: Array<String>

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ 的删除方法
            val relativePath = getRelativePath(type)
            selection =
                "${MediaStore.MediaColumns.RELATIVE_PATH} = ? AND ${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
            selectionArgs = arrayOf(relativePath, file.name)
        } else {
            // Android 9 以下的删除方法
            selection = "${MediaStore.MediaColumns.DATA} = ?"
            selectionArgs = arrayOf(file.absolutePath)
        }

        val uri: Uri = when (type) {
            MediaType.VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            MediaType.IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val rowsDeleted = contentResolver.delete(uri, selection, selectionArgs)
        if (rowsDeleted > 0) {
            Timber.d("$type 文件已成功从 MediaStore 删除: ${file.absolutePath}")
            return true
        } else {
            Timber.e("删除 $type 文件失败: ${file.absolutePath}")
            return false
        }
    }
}