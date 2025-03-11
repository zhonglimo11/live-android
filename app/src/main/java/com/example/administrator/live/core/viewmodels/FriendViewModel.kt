package com.example.administrator.live.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.administrator.live.bean.Video
import com.example.administrator.live.utils.fixtures.UserInteractionsFixtures.getVideos
import kotlinx.coroutines.launch

class FriendViewModel: ViewModel() {
    var friendVideoList = mutableListOf<Video>()

    init {
        viewModelScope.launch {
            loadInitData()
        }
    }

    private fun loadInitData() {
        friendVideoList.addAll(getVideos())
    }

}