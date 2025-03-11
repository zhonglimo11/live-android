package com.example.administrator.live.bean

data class Group(
    val groupId: String,
    var groupNumber: Int,
    var maxSize: Int,
    var name: String,
    var owner: User,
    var adminList: List<User>? = null,
    var announcement: String = "",
    var member: List<User>,
    var userNickname: String = "",
    var groupQRCode: QRCode? = null,
)