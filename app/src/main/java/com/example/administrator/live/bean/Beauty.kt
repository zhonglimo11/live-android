package com.example.administrator.live.bean

data class Beauty(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val icon: Image? = null,
    val url: String = "",
    var strength: Float = 0.5f,
    val filterList: List<BeautyFilter>? = null
)

