package com.example.administrator.live.bean

sealed class Image {
    data class StringImage(val value: String) : Image()
    data class IntImage(val value: Int) : Image()
}

val Image.modelValue: Any
    get() = when (this) {
        is Image.StringImage -> value
        is Image.IntImage -> value
    }