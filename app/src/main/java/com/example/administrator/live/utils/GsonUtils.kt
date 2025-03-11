package com.example.administrator.live.utils

import com.google.gson.Gson

object GsonUtils {
    private val gson = Gson()

    fun <T> fromJson(json: String, clazz: Class<T>): T? {
        return gson.fromJson(json, clazz)
    }

    fun <T> toJson(obj: T): String {
        return gson.toJson(obj)
    }
}