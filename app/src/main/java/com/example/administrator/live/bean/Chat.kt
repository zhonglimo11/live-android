package com.example.administrator.live.bean

import com.stfalcon.chatkit.commons.models.IDialog

data class Chat(
    private val id: String,
    private val name: String = "",
    private val chatPhoto: String = "",
    private var isGroup: Boolean,
    private var user: User? = null,
    private var group: Group? = null,
    private var messageList: List<Message>? = null,
    private var isDoNotDisturb: Boolean = false,
    private var isPinned: Boolean = false,
    private var backGroundImg: Image? = null,
    private var lastMessage: Message? = null,
    private var unreadCount: Int = 0
) : IDialog<Message> {

    override fun getId(): String = id

    override fun getDialogPhoto(): String = chatPhoto

    override fun getDialogName(): String = name

    override fun getUsers(): ArrayList<User?> {
        return if (isGroup) arrayListOf(user) else ArrayList(group?.member ?: ArrayList())
    }

    override fun getLastMessage(): Message? {
        return messageList?.maxBy { it.createdAt }
    }

    override fun setLastMessage(lastMessage: Message) {
        this.lastMessage = lastMessage
    }

    override fun getUnreadCount(): Int {
        return messageList?.count { !it.getIsRead() } ?: 0
    }

    fun readAll() {
        unreadCount = 0
        messageList?.forEach {
            it.setIsRead(true)
        }
    }

    fun getIsGroup(): Boolean = isGroup

    fun getUser(): User? = user

    fun getGroup(): Group? = group

    fun getMessageList(): List<Message> = messageList ?: emptyList()

    fun getIsDoNotDisturb(): Boolean = isDoNotDisturb

    fun getIsPinned(): Boolean = isPinned
}