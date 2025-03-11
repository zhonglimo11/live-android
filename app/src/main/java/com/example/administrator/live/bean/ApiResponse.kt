package com.example.administrator.live.bean

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    val code: Int,
    val msg: String,
    @SerializedName("result") val result: Result<T>?,
    val path: String,
    val timestamp: Long
)

data class Result<T>(
    val code: Int,
    val msg: String?,
    val data: T?
)