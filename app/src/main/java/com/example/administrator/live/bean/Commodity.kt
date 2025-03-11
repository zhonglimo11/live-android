package com.example.administrator.live.bean

data class Commodity(
    var id: String = "",
    var name: String = "",
    var price: Float = 0f,
    var cover: String = "",
    var isLike: Boolean = false,
    var likeCount: Int = 0,
    var isFailure: Boolean = false,
)
