package com.example.administrator.live.core.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.administrator.live.R
import com.example.administrator.live.bean.Chat
import com.example.administrator.live.bean.FollowState
import com.example.administrator.live.bean.FollowState.FOLLOW
import com.example.administrator.live.bean.FollowState.FOLLOWED
import com.example.administrator.live.bean.FollowState.FOLLOW_EACH_OTHER
import com.example.administrator.live.bean.FollowState.UNFOLLOW
import com.example.administrator.live.bean.Image
import com.example.administrator.live.bean.Message
import com.example.administrator.live.bean.User
import com.example.administrator.live.bean.UserInteractions
import com.example.administrator.live.utils.fixtures.DialogsFixtures.getChats
import com.example.administrator.live.utils.fixtures.DialogsFixtures.getSystemChat
import com.example.administrator.live.utils.fixtures.DialogsFixtures.getUsers
import com.example.administrator.live.utils.fixtures.UserInteractionsFixtures.getUserInteractions
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.utils.DateFormatter
import kotlinx.coroutines.launch
import java.util.Date

/**
 * 对话框类型
 * 0：退出群聊，1：删除消息，2：加入黑名单,3:二维码,4：修改昵称
 * 5：私聊设置备注名，6：设置群聊名称
 */
enum class DialogType {
    EXIT_GROUP,
    DELETE_MESSAGE,
    BLACK_LIST,
    QR_CODE,
    EDIT_NICK_NAME,
    CHAT_REMARK,
    EDIT_GROUP_NAME,
    NONE
}

/**
 * 权限类型
 * 0：群主，1：管理员，2：普通成员
 */
enum class PermissionType {
    OWNER,
    ADMIN,
    MEMBER
}

/**
 * 用户行为
 */
enum class UserAction(val action: String) {
    E_TER("@我"),
    COMMENTS("评论"),
    THUMBS_UP("获赞")
}

class ChatViewModel : ViewModel() {

    var userId by mutableStateOf("1")

    //当前聊天
    var chat: Chat by mutableStateOf(Chat("", isGroup = false))

    //当前聊天背景
    var chatBg by mutableStateOf<Image>(Image.IntImage(R.drawable.chat_bg_00))
    val imageLoader = ImageLoader { imageView, url, _ -> Picasso.get().load(url).into(imageView) }

    //系统聊天列表（通知）
    var systemChats = mutableStateListOf<Chat>()

    //用户聊天列表
    var userChats = mutableStateListOf<Chat>()

    //聊天房消息列表
    var messageList = mutableStateListOf<Message>()

    //用户交互通知类型
    var action by mutableStateOf(UserAction.E_TER)

    //点赞通知列表
    var likes = mutableStateListOf<UserInteractions>()

    //评论通知列表
    var comments = mutableStateListOf<UserInteractions>()

    //@通知列表
    var ets = mutableStateListOf<UserInteractions>()

    //未读点赞数
    var unreadLikeCount by mutableIntStateOf(0)

    //未读评论数
    var unreadCommentCount by mutableIntStateOf(0)

    //未读@数
    var unreadETCount by mutableIntStateOf(0)

    // 好友列表
    var friends = mutableStateListOf<User>()
    var bgChecked by mutableIntStateOf(0)

    //选中的好友
    var selectedFriends = mutableStateListOf<User>()

    //邀请搜索输入框
    var searchInput: String by mutableStateOf("")

    //图片消息列表
    var imgMessageList = mutableListOf<Message>()

    // 聊天用户列表
    var users = mutableStateListOf<User>()

    // 标题
    var title by mutableStateOf("")

    // 用户所在群权限
    var userPermissions by mutableStateOf(PermissionType.OWNER)

    // 群公告
    var groupInfo by mutableStateOf("")

    // 群名称
    var groupName by mutableStateOf("")

    // 群名称输入框
    var groupNameInput by mutableStateOf("")

    // 群二维码
    var groupQRCode: String by mutableStateOf("")

    // 群二维码有效期
    var dateQRCode by mutableStateOf("")

    // 我的群昵称
    var nickName by mutableStateOf("")

    // 我的群昵称文本输入框
    var nickNameInput by mutableStateOf("")

    var chatHistoryInput by mutableStateOf("")

    //私聊备注名
    var chatRemarks by mutableStateOf("")

    // 私聊备注名输入框
    var chatRemarksInput by mutableStateOf("")

    // 最大群人数
    var maxGroupSize by mutableIntStateOf(0)

    // 免打扰开关
    var switchDoNotDisturb by mutableStateOf(false)

    // 置顶开关
    var switchPinned by mutableStateOf(false)

    // 私聊关注状态
    var followState by mutableStateOf(FOLLOW)

    // 对话框类型
    var dialogType by mutableStateOf(DialogType.NONE)



    //所有举报选项
    val reportItems = listOf(
        "侮辱谩骂、网络暴力",
        "色情低俗",
        "违法犯罪",
        "涉嫌欺诈",
        "政治敏感",
        "违规卖货、假冒商品",
        "骚扰、侵害未成年",
        "其他"
    )

    //举报选中状态
    val reportChecked = mutableStateListOf(*Array(reportItems.size) { false })

    //举报补充说明
    var reportRemarks by mutableStateOf(TextFieldValue())

    //举报图片证明
    val reportImages = mutableStateListOf<String>()

    init {
        viewModelScope.launch {
            loadInitData()
        }
    }

    private suspend fun loadInitData() {
        systemChats.addAll(getSystemChat())
        userChats.addAll(getChats())
        likes.addAll(getUserInteractions(UserAction.THUMBS_UP.action))
        ets.addAll(getUserInteractions(UserAction.E_TER.action))
        comments.addAll(getUserInteractions(UserAction.COMMENTS.action))
        val friendsData = getUsers(20)
        // 添加好友
        friends.addAll(friendsData)
        // 设置输入框的初始值
        groupNameInput = groupName
        nickNameInput = nickName
    }

    fun setBg(img:String){
        chatBg = Image.StringImage(img)
        bgChecked = -1
    }

    fun updateSelectedFriends(user: User) {
        selectedFriends = selectedFriends.apply {
            if (contains(user)) {
                remove(user)
            } else {
                add(user)
            }
        }
    }

    // 更新群公告
    fun updateGroupInfo(newGroupInfo: String) {
        groupInfo = newGroupInfo
    }

    // 更新群名称
    fun updateGroupName(newGroupName: String) {
        groupName = newGroupName
    }

    // 更新我的群昵称
    fun updateNickName(newNickName: String) {
        nickName = newNickName
    }

    // 更新昵称输入框
    fun updateNickNameInput(newNickName: String) {
        nickNameInput = newNickName
    }

    fun updateChatHistoryInput(newChatHistoryInput: String) {
        chatHistoryInput = newChatHistoryInput
    }

    // 更新私聊备注名
    fun updateChatRemarks(newChatRemarks: String) {
        chatRemarks = newChatRemarks
    }

    // 更新私聊备注名输入框
    fun updateChatRemarksInput(newChatRemarks: String) {
        chatRemarksInput = newChatRemarks
    }

    // 更新最大群人数
    fun updateMaxSize(newMaxSize: Int) {
        maxGroupSize = newMaxSize
    }

    // 更新私聊关注状态
    fun updateFollowState(newFollowState: FollowState) {
        followState = newFollowState
    }

    fun updateChatId(chatId: String) {
        chat =
            systemChats.firstOrNull { it.id == chatId } ?: userChats.firstOrNull { it.id == chatId }
                    ?: return
        chat.readAll()
        chat.let { chatData ->
            val group = chatData.getGroup()
            val user = chatData.getUser()
            users.clear()
            messageList.clear()
            imgMessageList.clear()
            group?.let {
                val groupQR = it.groupQRCode
                users.addAll(it.member)
                groupInfo = it.announcement
                groupName = it.name
                maxGroupSize = it.maxSize
                nickName = it.userNickname
                // 设置二维码和过期时间
                groupQRCode = groupQR?.qrcode ?: ""
                dateQRCode = if (groupQR != null) {
                    DateFormatter.format(groupQR.expireTime, "MM月dd日")
                } else {
                    DateFormatter.format(Date(), "MM月dd日")
                }
                // 设置用户权限
                userPermissions = when {
                    it.owner.id == userId -> PermissionType.OWNER
                    it.adminList?.any { admin -> admin.id == userId } == true -> PermissionType.ADMIN
                    else -> PermissionType.MEMBER
                }
            }
            user?.let {
                users.add(it)
                followState = it.followState
            }
            // 添加消息列表
            messageList.addAll(chatData.getMessageList())
            imgMessageList.addAll(messageList.filter { it.imageUrl != null })
            switchDoNotDisturb = chatData.getIsDoNotDisturb()
            switchPinned = chatData.getIsPinned()
        }
    }

    // 切换免打扰状态
    fun setDoNotDisturbEnabled(enabled: Boolean) {
        switchDoNotDisturb = !enabled
    }

    // 切换置顶状态
    fun setPinnedEnabled(enabled: Boolean) {
        switchPinned = !enabled
    }

    /**
     * 处理关注点击事件
     * 0：双向未关注，1：单向被关注，2：单向已关注，3：互相关注
     */
    fun onFollowClicked() {
        val newFollowState = when (followState) {
            UNFOLLOW -> FOLLOW // 从未关注切换到单向已关注
            FOLLOWED -> FOLLOW_EACH_OTHER // 从单向被关注切换到互相关注
            FOLLOW -> UNFOLLOW // 从单向已关注切换到未关注
            FOLLOW_EACH_OTHER -> FOLLOWED // 从互相关注切换到单向被关注
        }
        updateFollowState(newFollowState)
    }

    /**
     * 提交群公告
     */
    fun onSubmitGroupInfoClicked() {
        // 更新群公告

    }

    /**
     * 处理退出群聊点击事件
     */
    fun onExitGroupClicked() {
        dialogType = DialogType.EXIT_GROUP // 设置对话框类型为退出群聊
    }

    /**
     * 处理删除消息点击事件
     */
    fun onDeleteMessageClicked() {
        dialogType = DialogType.DELETE_MESSAGE // 设置对话框类型为删除消息
    }

    // 处理取消点击事件
    fun onCancelClicked() {
        dialogType = DialogType.NONE // 重置对话框类型
    }

    // 处理确认点击事件
    fun onConfirmClicked() {
        when (dialogType) {
            DialogType.EXIT_GROUP -> {
                // 执行退出群聊的逻辑
            }

            DialogType.DELETE_MESSAGE -> {
                // 执行删除消息的逻辑
            }

            DialogType.BLACK_LIST -> {
                // 执行加入黑名单的逻辑
            }

            DialogType.QR_CODE -> {
                // 执行二维码操作的逻辑
            }

            DialogType.EDIT_NICK_NAME -> {
                updateNickName(nickNameInput)
            }

            DialogType.CHAT_REMARK -> {
                updateChatRemarks(chatRemarksInput)
            }

            DialogType.EDIT_GROUP_NAME -> {
                updateGroupName(groupNameInput)
            }

            else -> throw IllegalStateException("Invalid dialog type") // 抛出异常，状态无效
        }
        dialogType = DialogType.NONE // 重置对话框类型
    }

    // 展示所有群成员
    fun showAllUsers() {
        // TODO: 展示所有群成员
    }

    //修改群名称
    fun modifyGroupName() {
        dialogType = DialogType.EDIT_GROUP_NAME
    }

    //设置用户备注名
    fun setRemarkName() {
        dialogType = DialogType.CHAT_REMARK
    }

    //显示群二维码
    fun showGroupQRCode() {
        dialogType = DialogType.QR_CODE
    }

    //修改群内昵称
    fun modifyNickName() {
        dialogType = DialogType.EDIT_NICK_NAME
    }

    //拉黑
    fun blacklist() {
        dialogType = DialogType.BLACK_LIST
    }
}