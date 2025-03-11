package com.example.administrator.live.core.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.administrator.live.R
import com.example.administrator.live.bean.Commodity
import com.example.administrator.live.bean.Image
import com.example.administrator.live.bean.Music
import com.example.administrator.live.bean.User
import com.example.administrator.live.bean.Video
import com.example.administrator.live.utils.fixtures.DialogsFixtures.getFavoriteCommodities
import com.example.administrator.live.utils.fixtures.DialogsFixtures.getMusics
import com.example.administrator.live.utils.fixtures.DialogsFixtures.getUser
import com.example.administrator.live.utils.fixtures.DialogsFixtures.getUsers
import com.example.administrator.live.utils.fixtures.DialogsFixtures.getVideos
import kotlinx.coroutines.launch

class MineViewModel : ViewModel() {
    var userId by mutableStateOf("0")
    var user by mutableStateOf(User(""))
    var bgImg by mutableStateOf<Image>(Image.IntImage(R.mipmap.img_header))

    //作品列表
    var worksVideoList = mutableStateListOf<Video>()

    //私密作品列表
    var privateVideoList = mutableStateListOf<Video>()

    //收藏列表
    var favoriteVideoList = mutableStateListOf<Video>()
    var favoriteMusicList = mutableStateListOf<Music>()
    var favorCommodityList = mutableStateListOf<Commodity>()

    //喜欢视频列表
    var likedVideoList = mutableStateListOf<Video>()

    //粉丝列表
    var fansList = mutableStateListOf<User>()

    //关注列表
    var followList = mutableStateListOf<User>()

    //朋友列表
    var friendList = mutableStateListOf<User>()

    //获赞数
    var likesReceivedNum by mutableIntStateOf(0)

    //收藏数
    var favoritesNum by mutableIntStateOf(0)

    //消费红包数
    var consumptionRedPacketNum by mutableFloatStateOf(0f)

    //现金红包数
    var cashRedPacketNum by mutableFloatStateOf(0f)

    init {
        viewModelScope.launch {
            loadInitData()
        }
    }

    private fun loadInitData() {
        userId = "1"
        user = getUser(userId)
        worksVideoList.addAll(getVideos(10))
        privateVideoList.addAll(getVideos(10))
        favoriteVideoList.addAll(getVideos(10))
        favoriteMusicList.addAll(getMusics())
        favorCommodityList.addAll(getFavoriteCommodities())
        likedVideoList.addAll(getVideos(10))
        fansList.addAll(getUsers(10))
        followList.addAll(getUsers(10))
        friendList.addAll(getUsers(10))
        likesReceivedNum = 100
        favoritesNum = 100
        consumptionRedPacketNum = 100f
        cashRedPacketNum = 100f
    }
}