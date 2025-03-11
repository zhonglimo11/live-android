package com.example.administrator.live.utils.fixtures

import com.example.administrator.live.bean.Chat
import com.example.administrator.live.bean.Message
import com.example.administrator.live.bean.User
import com.example.administrator.live.utils.fixtures.MessagesFixtures.getChat
import java.util.Calendar
import java.util.Date

/**
 * 该类用于生成对话列表的测试数据
 */
internal object DialogsFixtures : FixturesData() {

    /**
     * 获取一组对话数据
     * @return 返回一个包含生成的对话对象的ArrayList
     */
    fun getChats(): ArrayList<Chat> {
        val chats = ArrayList<Chat>()
        for (i in 0 until 20) {
            chats.add(getChat(i.toString()))
        }
        return chats
    }

    /**
     * 获取一组随机用户数据
     * @return 返回一个包含生成的用户对象的ArrayList
     */
    fun getUsers(number: Int? = null): ArrayList<User> {
        val users = ArrayList<User>()
        var usersCount = 1 + rnd.nextInt(20)
        if (number != null) {
            usersCount = number
        } else {
            if (getRandomBoolean()) {
                usersCount = 1
            }
        }
        repeat(usersCount) {
            users.add(getUser())
        }
        return users
    }

    /**
     * 生成一个随机用户对象
     * @return 返回生成的用户对象
     */
    fun getUser(id:String = getRandomId()): User {
        return User(
            id,
            generateRandomChineseName(),
            getRandomAvatar(),
            getRandomBoolean(),
            getRandomBoolean()
        )
    }

    /**
     * 生成一个带有指定时间的随机消息对象
     * @param date 消息的创建时间
     * @return 返回生成的消息对象
     */
    fun getMessage(date: Date): Message {
        var message = Message(
            getRandomId(),
            getUser(),
            getRandomMessage(),
            date
        )
        if (getRandomBoolean()) {
            message = Message(
                getRandomId(),
                getUser(),
                createdAt = date,
                image = getRandomImage()
            )
        }
        return message
    }

    fun getMessages(number: Int = getRandomInt(20)): List<Message> {
        val messages = mutableListOf<Message>()
        // 初始化起始时间为当前时间
        var startTime = Date()
        repeat(number) {
            // 创建消息并设置时间戳
            val message = getMessage(startTime)
            messages.add(message)
            // 减去 12 小时
            startTime = subtractHours(startTime)
        }
        return messages
    }

    private fun subtractHours(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR_OF_DAY, -getRandomInt(24))
        return calendar.time
    }

    fun getSystemChat(): ArrayList<Chat> {
        val chatLists = arrayListOf(
            Chat(
                "systemNotice",
                "系统公告",
                "",
                false,
                messageList = listOf(
                    Message(
                        "0",
                        MessagesFixtures.getUser("1"),
                        "生命中曾经有过的所有喧嚣..."
                    )
                )
            ),
            Chat(
                "newFollow",
                "新增粉丝",
                "",
                false,
                messageList = listOf(
                    Message(
                        "0",
                        MessagesFixtures.getUser("2"),
                        "Ta们关注你 快去看看吧"
                    )
                )
            ),
            Chat(
                "customerService",
                "官方客服",
                "",
                false,
                messageList = listOf(
                    Message(
                        "0",
                        MessagesFixtures.getUser("3"),
                        "还没有新的通知"
                    )
                )
            ),
        )
        return chatLists
    }
}