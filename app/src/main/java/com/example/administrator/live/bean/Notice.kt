package com.example.administrator.live.bean
import java.util.Date

data class Notice(
    var id: String = "",
    var user: User,
    var action: String = "",
    var time: Date? = Date(),
)