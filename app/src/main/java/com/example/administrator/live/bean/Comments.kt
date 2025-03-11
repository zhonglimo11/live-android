package com.example.administrator.live.bean

import java.util.Date

data class Comments(
    val id: String = "",
    val user: User,
    val content: String = "",
    val video: Video,
    val replyTo: Comments? = null,
    val existence:Boolean = true,
    val time: Date = Date(),
)