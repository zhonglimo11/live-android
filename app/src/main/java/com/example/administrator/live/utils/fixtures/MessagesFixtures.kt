package com.example.administrator.live.utils.fixtures

import com.example.administrator.live.bean.Chat
import com.example.administrator.live.bean.Group
import com.example.administrator.live.bean.Message
import com.example.administrator.live.bean.User
import com.example.administrator.live.utils.fixtures.DialogsFixtures.getMessages
import com.example.administrator.live.utils.fixtures.DialogsFixtures.getUsers
import kotlin.random.Random

internal object MessagesFixtures : FixturesData() {

    /**
     * 获取一条包含图片的消息对象
     *
     * @return 包含随机图片的Message对象
     */
    fun getImageMessage(): Message {
        val message = Message(getRandomId(), getUser("0"))
        message.setImage(getRandomImage())
        message.setGroupId(getRandomGroupId())
        return message
    }

    /**
     * 获取一条包含语音的消息对象
     *
     * @return 包含随机语音时长的Message对象
     */
    fun getVoiceMessage(): Message {
        val message = Message(getRandomId(), getUser("0"))
        message.setVoice(Message.Voice("http://example.com", Random.nextInt(30, 230)))
        return message
    }

    /**
     * 获取一条包含文本的消息对象
     *
     * @return 包含随机文本的Message对象
     */
    fun getTextMessage(): Message {
        return getTextMessage(getRandomMessage())
    }

    /**
     * 获取一条包含文本的消息对象
     *
     * @param text 消息的具体文本内容
     * @return 包含指定文本的Message对象
     */
    fun getTextMessage(text: String): Message {
        val message = Message(getRandomId(), getUser("0"), text)
        message.setGroupId(getRandomGroupId())
        return message
    }

    /**
     * 获取一个随机的用户对象
     *
     * @return 随机的User对象
     */
    fun getUser(id: String): User {
        val even = Random.nextBoolean()
        return User(
            id,
            generateRandomChineseName(),
            if (even) avatars[0] else avatars[1],
            true
        )
    }

    /**
     * 获取一个随机的群组对象
     */
    fun getGroup():Group{
        val id = getRandomId()
        val maxSize = getRandomInt(500)
        val groupNumber = 88888888
        val name = getRandomGroupChatTitle()
        val members = getUsers(20)
        val owner = members[getRandomInt(members.size)]
        val nonOwnerMembers = members.filter { it != owner }
        val admin: List<User> = nonOwnerMembers.shuffled().take(3)
        return Group(id,groupNumber,maxSize,name,owner,admin,"",members)
    }

    /**
     * 获取一个随机的聊天对象
     */
    fun getChat(id: String = getRandomId()):Chat{
        val isGroup = getRandomBoolean()
        var user: User? = getUser(getRandomId())
        var group: Group? = null
        val messageList = getMessages(20)
        val isDoNotDisturb = getRandomBoolean()
        val isPinned = getRandomBoolean()
        var title = user!!.name
        val chatPhoto = getRandomGroupChatImage()
        if (isGroup){
            user = null
            group = getGroup()
            title = group.name
        }
        return Chat(id,title,chatPhoto,isGroup,user,group,messageList,isDoNotDisturb,isPinned)
    }
}