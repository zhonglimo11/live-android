package com.example.administrator.live.bean

import java.util.Date

data class UserInteractions(
    var id: String = "",
    var user: User,
    var video: Video,
    var action: String = "",
    var comments: Comments? = null,
    var time: Date? = Date(),
    var isRead: Boolean = false
)