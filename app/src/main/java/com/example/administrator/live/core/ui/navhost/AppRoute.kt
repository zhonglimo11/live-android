package com.example.administrator.live.core.ui.navhost

// 定义Screen
sealed class Screen(val route: String) {
    /** 主页 */
    data object Home : Screen("home")

    /** 好友页面 */
    data object Friend : Screen("friend")

    /** 消息页面 */
    data object Message : Screen("message")

    /** 新粉丝页面 */
    data object NewFans : Screen("newFans")

    /** 系统通知页面 */
    data object SystemNotice : Screen("systemNotice")

    /** 用户通知页面 */
    data object UserNotice : Screen("userNotice")

    /** 聊天室页面 */
    data object ChatRoom : Screen("chatRoom")

    /** 聊天菜单页面 */
    data object ChatMenu : Screen("chatMenu")

    /** 聊天背景设置页面 */
    data object ChatBackgroundSet : Screen("chatBackgroundSet")

    /** 聊天图片页面 */
    data object ChatPhoto : Screen("chatPhoto")

    /** 群管理页面 */
    data object GroupAdmin : Screen("groupAdmin")

    /** 聊天记录页面 */
    data object ChatHistory : Screen("chatHistory")

    /** 群通知页面 */
    data object GroupNotice : Screen("groupNotice")

    /** 邀请成员页面 */
    data object InviteMember : Screen("inviteMember")

    /** 群主发言页面 */
    data object OwnerSpeak : Screen("ownerSpeak")

    /** 举报聊天页面 */
    data object ReportChat : Screen("reportChat")

    /** 举报证据页面 */
    data object ReportingEvidence : Screen("reportingEvidence")

    /** 我的页面 */
    data object Mine : Screen("mine")

    /** 拍摄页面 */
    data object Camera : Screen("camera")

    /** 随手拍文字模式页面 */
    data object CameraText : Screen("cameraText")

    /** 发布页面 */
    data object Publish : Screen("publish")

    /** 选择背景图片页面，使用参数 from */
    data class SelectBgPhoto(val from: String) : Screen("selectBgPhoto/$from")

    /** 用户详情界面 */
    data object UserDetail : Screen("userDetail")
}

// 定义Graph
sealed class Graph(val route: String) {
    /** 主页导航图 */
    data object Home : Graph("home_graph")

    /** 好友导航图 */
    data object Friend : Graph("friend_graph")

    /** 聊天室设置导航图 */
    data object Camera : Graph("camera_graph")

    /** 消息导航图 */
    data object Message : Graph("message_graph")

    /** 聊天室导航图 */
    data object ChatRoom : Graph("chatRoom_graph")

    /** 聊天菜单导航图 */
    data object ChatMenu : Graph("chatMenu_graph")

    /** 举报聊天导航图 */
    data object ReportChat : Graph("reportChat_graph")

    /** 我的导航图 */
    data object Mine : Graph("mine_graph")
}