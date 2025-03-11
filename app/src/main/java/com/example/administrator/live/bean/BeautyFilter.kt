package com.example.administrator.live.bean

data class BeautyFilter(
    val id: String = "",
    val name: String = "",
    val icon: Image? = null,
    val url: String = "",
    var strength: Float = 0.5f
)