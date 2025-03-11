package com.example.administrator.live.bean

data class PlayerDTO(
    var path: String = "",
    var code: Int = 0,
    var msg: String = "",
    var result: List<String> = emptyList(),
    var timestamp: Long = 0L
)
