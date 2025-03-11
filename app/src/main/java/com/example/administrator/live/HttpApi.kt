package com.example.administrator.live

import com.example.administrator.live.bean.ApiResponse
import com.example.administrator.live.utils.OkHttpUtils.get
import com.example.administrator.live.utils.OkHttpUtils.post
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import java.io.IOException

object HttpApi {
    //private const val BASE_URL = "http://192.168.0.112:8000/"
    // 基础 URL
    private const val BASE_URL = "http://192.168.0.103:8000/"

    // API URL 常量
    const val GET_URL = "${BASE_URL}getUrl"
    const val GET_PLAYERS = "${BASE_URL}list"

    // BGM 关系
    const val COLLECTION_BGM = "${BASE_URL}user/bgm-collection/getMyCollectionBgm"

    // BGM
    const val RECOMMEND_BGM = "${BASE_URL}bgm/getRecommendBgm"

    // 添加个人 BGM
    const val INSERT_BGM = "${BASE_URL}bgm/insertBgmByUserId"

    // 获取收藏商品
    const val GOODS_COLLECTION_LIST = "${BASE_URL}user/goods-collect/getMyCollectGoodsList"

    // 用户商品列表
    const val SHOP_LIST = "${BASE_URL}user/goods/getShopListByUserId"

    // 获取小黄车
    const val GET_YELLOW_CAR_LIST = "${BASE_URL}yellow-car/yellow-car/getYellowCarByLiveId"

    // 获取互动列表
    const val GET_INTERACTION_LIST = "${BASE_URL}user-interaction/getInteractionList"

    // 添加互动 POST
    const val INSERT_INTERACTION = "${BASE_URL}user-interaction/insertInteraction"

    // 删除用户关系
    const val DELETE_RELATION = "${BASE_URL}user-relation/deleteRelation"

    // 获取好友列表
    const val GET_FRIEND_LIST = "${BASE_URL}user-relation/getFriendList"

    // 获取粉丝列表
    const val GET_FANS_LIST = "${BASE_URL}user-relation/getFansList"

    const val GET_FOLLOW_LIST = "${BASE_URL}user-relation/getMyFollowList"

    // 获取个人信息
    const val GET_USER_INFO = "${BASE_URL}userInfo/getMyInfo"

    // 更新个人资料
    const val UPDATE_USER_INFO = "${BASE_URL}userInfo/updateUserInfo"

    // 用户登录
    const val USER_LOGIN = "${BASE_URL}userInfo/login"

    // 退出登录
    const val USER_LOGOUT = "${BASE_URL}userInfo/logout"

    // 注销
    const val USER_DELETE = "${BASE_URL}userInfo/deleteUser"

    // 更新直播信息
    const val UPDATE_LIVE_USER = "${BASE_URL}user/live-user-relation/updateLiveUser"

    // 更新直播可见状态
    const val UPDATE_USER_SEE = "${BASE_URL}user/live-user-relation/updateUserSee"

    // 获取直播信息
    const val GET_LIVE_INFO = "${BASE_URL}live/getLiveInfo"

    // 获取直播列表
    const val GET_LIVE_LIST = "${BASE_URL}live/getLiveList"

    // 更新直播信息
    const val UPDATE_LIVE_INFO = "${BASE_URL}live/updateLiveInfo"

    // 获取礼物列表
    const val GET_GIFT_LIST = "${BASE_URL}gift/getGiftList"

    // 打赏
    const val SEND_GIFT = "${BASE_URL}gift/SendGift"

    // 查看收藏视频
    const val GET_MY_COLLECTION_VIDEO = "${BASE_URL}videoCollection/getMyCollectionVideo"

    // 查看点赞视频
    const val GET_LIKE_VIDEO = "${BASE_URL}video-like/getLikeVideo"

    // 新增关注
    const val INSERT_VIDEO_USER_SEE = "${BASE_URL}user/video-user-relation/insertVideoUserSee"

    // 取消关注
    const val DELETE_VIDEO_USER_SEE = "${BASE_URL}user/video-user-relation/deleteVideoUserSee"

    // 加入黑名单
    const val INSERT_VIDEO_UN_USER_SEE = "${BASE_URL}user/video-user-relation/insertVideoUnUserSee"

    // 删除黑名单
    const val DELETE_VIDEO_UN_USER_SEE = "${BASE_URL}user/video-user-relation/deleteVideoUnUserSee"

    // 草稿视频
    const val DRAFT_VIDEO = "${BASE_URL}video/getMyDraftVideo"

    // 上传草稿视频
    const val UP_DRAFT_VIDEO = "${BASE_URL}video/sendDraftVideo"

    // 私密视频
    const val PRIVATE_VIDEO = "${BASE_URL}video/getMyPrivateVideo"

    // 我的视频
    const val MY_VIDEO = "${BASE_URL}video/getMyVideo"

    // BGM 视频列表
    const val VIDEO_LIST_BY_BGM = "${BASE_URL}video/getVideoListByBgm"

    // 同城视频
    const val VIDEO_LIST_BY_CITY = "${BASE_URL}video/getVideoListByCity"

    // 其他视频
    const val VIDEO_LIST_OF_OTHER = "${BASE_URL}video/getVideoListOfOther"

    // 条件搜索视频
    const val SELECT_VIDEO_LIST = "${BASE_URL}video/selectVideoList"

    // 发布视频
    const val SEND_VIDEO = "${BASE_URL}video/sendVideo"

    // 加入粉丝团
    const val JOIN_FANS_CLUB = "${BASE_URL}fan-club/joinFansClub"

    // 创建粉丝团
    const val CREATE_FANS_CLUB = "${BASE_URL}fan-club/createFansClub"

    // 踢出粉丝团
    const val KICK_OUT_FANS_CLUB = "${BASE_URL}fan-club/kickOutFansClub"

    // 退出粉丝团
    const val QUIT_FANS_CLUB = "${BASE_URL}fan-club/quitFansClub"

    // 解散粉丝团
    const val DELETE_FANS_CLUB = "${BASE_URL}fan-club/deleteFansClub"

    // 删除评论
    const val DELETE_COMMENT = "${BASE_URL}comment/deleteCommentById"

    // 子评论
    const val GET_COMMENT_BY_COMMENT_ID = "${BASE_URL}comment/getCommentByCommentId"

    // 评论列表
    const val GET_COMMENT_LIST = "${BASE_URL}comment/getCommentByVideoId"

    // 发布评论
    const val SEND_COMMENT = "${BASE_URL}comment/sendCommentByVideoId"

    // 发布子评论
    const val SEND_COMMENT_BY_COMMENT_ID = "${BASE_URL}comment/sendCommentByCommentId"

    //上传视频
    const val UPLOAD_VIDEO = "${BASE_URL}video/uploadVideo"

    suspend inline fun <reified T> getFromUrl(url: String): ApiResponse<T>? {
        return withContext(Dispatchers.IO) {
            try {
                val json = get(url)
                val typeToken = object : TypeToken<ApiResponse<T>>() {}.type
                val response = Gson().fromJson<ApiResponse<T>>(json, typeToken)

                // 检查状态码
                if (response.code == 200) {
                    response
                } else {
                    println("Request failed with code: ${response.code}, message: ${response.msg}")
                    null
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend inline fun <reified T> postToUrl(
        url: String,
        requestBody: RequestBody,
        headers: Map<String, String>? = null
    ): ApiResponse<T>? {
        return withContext(Dispatchers.IO) {
            try {
                val json = post(url, requestBody, headers)
                val typeToken = object : TypeToken<ApiResponse<T>>() {}.type
                val response = Gson().fromJson<ApiResponse<T>>(json, typeToken)

                // 检查状态码
                if (response.code == 200) {
                    response
                } else {
                    println("Request failed with code: ${response.code}, message: ${response.msg}")
                    null
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                null
            }
        }
    }

}

