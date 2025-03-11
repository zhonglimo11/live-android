package com.example.administrator.live.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.administrator.live.HttpApi
import com.example.administrator.live.HttpApi.getFromUrl
import com.example.administrator.live.bean.Video
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeViewModel: ViewModel() {
    private val _recommendVideoList = MutableStateFlow(emptyList<Video>())
    val recommendVideoList: StateFlow<List<Video>> = _recommendVideoList

    private val _followVideoList = MutableStateFlow(emptyList<Video>())
    val followVideoList: StateFlow<List<Video>> = _followVideoList

    init {
        viewModelScope.launch {
            loadInitData()
        }
    }

    private suspend fun loadInitData() {
        val videos = getRecommendVideo()
        _recommendVideoList.value = videos
        _followVideoList.value = videos
    }

    private suspend fun getRecommendVideo(): List<Video> {
        val response = getFromUrl<List<Video>>(HttpApi.SELECT_VIDEO_LIST)
        return response?.result?.data ?: listOf()
    }
}