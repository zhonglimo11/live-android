package com.example.administrator.live.bean

data class Video(
    var videoId: Int,//视频ID
    var createTime: String? = null,//发布时间
    var updateTime: String? = null,//更新时间
    var title: String,//标题
    var cover: String? = null,//封面
    var author: Int? = 0,//作者ID
    var isPrivate: Boolean? = false,//是否为私密视频
    var lng: Double? = 0.0,//经度
    var lat: Double? = 0.0,//纬度
    var bgmId: Int? = 1,//背景音乐ID
    var url: String? = null,//视频地址
    var status: Int? = 1,//状态
    var city: String? = "利川市",//城市
    var likeCount: Int? = 0,//点赞数
    var isLike: Boolean = false,//是否点赞
    var commentCount: Int? = 0,//评论数
    var isFriend: Boolean? = false,//是否为好友
    var collectionCount: Int? = 0,//收藏数
    var transposedCount: Int? = 0,//转发数
    var lastViewedTimestamp: Long? = 0L
)
