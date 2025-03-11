package com.example.administrator.live.utils


import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

object OkHttpUtils {

    val CLIENT: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * GET请求
     * @param url 请求URL
     * @return 响应体字符串
     * @throws IOException 请求或响应过程中发生的错误
     */
    @Throws(IOException::class)
    fun get(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()
        CLIENT.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body?.string() ?: throw IOException("Response body is null")
        }
    }

    /**
     * POST请求
     * @param url 请求URL
     * @param requestBody 请求体
     * @param headers 请求头
     * @return 响应体字符串
     * @throws IOException 请求或响应过程中发生的错误
     */
    @Throws(IOException::class)
    fun post(url: String, requestBody: RequestBody, headers: Map<String, String>?): String {
        val builder = Request.Builder()
            .url(url)
            .post(requestBody)

        headers?.let {
            for ((key, value) in it) {
                builder.addHeader(key, value)
            }
        }

        val request = builder.build()
        val response = CLIENT.newCall(request).execute()
        return if (response.isSuccessful) {
            response.body?.string() ?: ""
        } else {
            throw IOException("Unexpected code $response")
        }
    }

    /**
     * 构造JSON请求体
     * @param jsonStr JSON字符串
     * @return JSON请求体
     */
    fun buildJsonRequestBody(jsonStr: String): RequestBody {
        return jsonStr.toRequestBody("application/json".toMediaTypeOrNull())
    }

    /**
     * 构造表单请求体
     * @param formParams 表单参数
     * @return 表单请求体
     */
    fun buildFormRequestBody(formParams: Map<String, String>?): RequestBody {
        val builder = FormBody.Builder()
        formParams?.let {
            for ((key, value) in it) {
                builder.add(key, value)
            }
        }
        return builder.build()
    }

    /**
     * 构造Multipart请求体
     * @param multipartParams Multipart参数
     * @return Multipart请求体
     */
    fun buildMultipartRequestBody(multipartParams: Map<String, Any>?): RequestBody {
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

        multipartParams?.let {
            for ((key, value) in it) {
                when (value) {
                    is String -> builder.addFormDataPart(key, value)
                    is ByteArray -> builder.addFormDataPart(
                        key, null,
                        value.toRequestBody("application/octet-stream".toMediaTypeOrNull())
                    )

                    is RequestBody -> builder.addFormDataPart(key, null, value)
                }
            }
        }
        return builder.build()
    }

    /**
     * 上传文件
     * @param url 请求URL
     * @param file 上传的文件
     * @param headers 请求头
     * @return 响应体字符串
     * @throws IOException 请求或响应过程中发生的错误
     */
    @Throws(IOException::class)
    fun uploadFile(url: String, file: UploadFile, headers: Map<String, String>?): String {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", file.name,
                file.file.toRequestBody(file.mimeType.toMediaTypeOrNull())
            )
            .build()

        val builder = Request.Builder()
            .url(url)
            .post(requestBody)

        headers?.let {
            for ((key, value) in it) {
                builder.addHeader(key, value)
            }
        }

        val request = builder.build()
        val response = CLIENT.newCall(request).execute()
        return if (response.isSuccessful) {
            response.body?.string() ?: ""
        } else {
            throw IOException("Unexpected code $response")
        }
    }

    /**
     * 封装文件上传参数
     */
    class UploadFile(val name: String, val mimeType: String, val file: ByteArray)

    /**
     * WebSocket连接
     * @param url WebSocket URL
     * @param listener WebSocket监听器
     */
    fun connectToChatSocket(url: String, listener: WebSocketListener) {
        val request = Request.Builder().url(url).build()
        CLIENT.newWebSocket(request, listener)
    }

    /**
     * 发送WebSocket消息
     * @param webSocket WebSocket实例
     * @param message 消息内容
     */
    fun sendMessage(webSocket: WebSocket, message: String) {
        webSocket.send(message)
    }
}