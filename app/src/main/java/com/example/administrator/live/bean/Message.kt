package com.example.administrator.live.bean

import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.MessageContentType
import java.util.Date

class Message(
    private val id: String,
    private val user: User? = null,
    private var text: String = "",
    private var createdAt: Date = Date(),
    private var image: String? = null,
    private var voice: Voice? = null,
    private var groupId: String? = null,
    private var isRead: Boolean = false
) : IMessage, MessageContentType.Image {

    constructor(id: String, user: User, text: String) : this(id, user, text, Date())
    override fun getId(): String = id

    override fun getText(): String = text

    override fun getCreatedAt(): Date = createdAt

    override fun getUser(): User? = user

    override fun getImageUrl(): String? = image

    fun getVoice(): Voice? = voice

    fun getIsRead(): Boolean = isRead

    fun setIsRead(isRead: Boolean){
        this.isRead = isRead
    }

    fun setText(text: String) {
        this.text = text
    }

    fun setCreatedAt(createdAt: Date) {
        this.createdAt = createdAt
    }

    fun setImage(image: String) {
        this.image = image
    }

    fun setGroupId(groupId: String?) {
        this.groupId = groupId
    }

    fun getGroupId(): String? = groupId

    fun setVoice(voice: Voice?) {
        this.voice = voice
    }
    class Voice(val url: String, val duration: Int)
}