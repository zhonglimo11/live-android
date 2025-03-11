package com.example.administrator.live.utils.fixtures

import com.example.administrator.live.bean.Comments
import com.example.administrator.live.bean.UserInteractions
import com.example.administrator.live.bean.Video
import com.example.administrator.live.core.viewmodels.UserAction
import com.example.administrator.live.utils.fixtures.DialogsFixtures.getUser

internal object UserInteractionsFixtures : FixturesData() {

    fun getUserInteractions(action: String): ArrayList<UserInteractions> {
        val userInteractionsList = ArrayList<UserInteractions>()
        repeat(10) {
            val userInteractions = UserInteractions(
                id = getRandomId(),
                user = getUser(),
                action = action,
                comments = when(action){
                    UserAction.COMMENTS.action -> getComments()
                    UserAction.THUMBS_UP.action -> if (getRandomBoolean())getCommentsReply()else null
                    UserAction.E_TER.action -> getETerComments()
                    else -> null
                },
                video = getOneVideo(),
                time = getRandomDate()
            )
            userInteractionsList.add(userInteractions)
        }
        return userInteractionsList
    }

    fun getComments(): Comments {
        return Comments(
            id = getRandomId(),
            user = getUser(),
            content = getRandomMessage(),
            video = getOneVideo(),
            replyTo = if (getRandomBoolean()) getCommentsReply() else null,
            existence = getRandomBoolean(),
        )
    }

    fun getCommentsReply(): Comments {
        return Comments(
            id = getRandomId(),
            user = getUser(),
            content = getRandomMessage(),
            video = getOneVideo(),
            replyTo = null,
            existence = getRandomBoolean(),
        )
    }

    fun getETerComments(): Comments {
        return Comments(
            id = getRandomId(),
            user = getUser(),
            content = getETerMessage(),
            video = getOneVideo(),
            replyTo = null,
            existence = getRandomBoolean(),
        )
    }

    fun getVideoList(): ArrayList<Video> {
        val videoList = ArrayList<Video>()
        repeat(10) {
            videoList.add(getOneVideo())
        }
        return videoList
    }
}