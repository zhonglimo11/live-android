package com.example.administrator.live.bean

data class Music(
    var id: String = "",
    var name: String = "",
    var url: String = "",
    var cover: String = "",
    var author: String = "",
    var usedCount: Int = 0,
    var isLike: Boolean = false,
    var duration: Int = 240
)
