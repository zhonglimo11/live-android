package com.example.administrator.live.bean

import java.util.Date

data class NewFans(
    var id: String = "",
    var user: User? = null,
    var date: Date? = Date()
)