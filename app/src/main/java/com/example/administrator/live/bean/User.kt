package com.example.administrator.live.bean

import com.stfalcon.chatkit.commons.models.IUser

data class User(
    private val id: String,
    private val name: String = "",
    private val avatar: String = "",
    private val online: Boolean = false,
    private val inLive: Boolean = false,
    var remarks: String? = null,
    var followState: FollowState = FollowState.UNFOLLOW,
    var info:String = ""
) : IUser {

    override fun getId(): String = id

    override fun getName(): String = name

    override fun getAvatar(): String = avatar

    fun isOnline(): Boolean = online

    fun isInLive(): Boolean = inLive
}

enum class FollowState {
    UNFOLLOW,
    FOLLOWED,
    FOLLOW,
    FOLLOW_EACH_OTHER
}
