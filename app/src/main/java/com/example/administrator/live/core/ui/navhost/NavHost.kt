package com.example.administrator.live.core.ui.navhost

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.administrator.live.bean.Image
import com.example.administrator.live.core.ui.main.camera.CameraScreen
import com.example.administrator.live.core.ui.main.camera.CameraTextScreen
import com.example.administrator.live.core.ui.main.camera.PublishScreen
import com.example.administrator.live.core.ui.main.friend.FriendScreen
import com.example.administrator.live.core.ui.main.home.HomeScreen
import com.example.administrator.live.core.ui.main.message.ChatRoomScreen
import com.example.administrator.live.core.ui.main.message.MessageMainScreen
import com.example.administrator.live.core.ui.main.message.NewFansScreen
import com.example.administrator.live.core.ui.main.message.SystemNoticeScreen
import com.example.administrator.live.core.ui.main.message.UserNoticeScreen
import com.example.administrator.live.core.ui.main.message.chatmenu.ChatBackgroundSet
import com.example.administrator.live.core.ui.main.message.chatmenu.ChatMenu
import com.example.administrator.live.core.ui.main.message.chatmenu.ChatPhoto
import com.example.administrator.live.core.ui.main.message.chatmenu.FindChatHistory
import com.example.administrator.live.core.ui.main.message.chatmenu.GroupNotice
import com.example.administrator.live.core.ui.main.message.chatmenu.InvitationMember
import com.example.administrator.live.core.ui.main.message.chatmenu.OwnerSpeak
import com.example.administrator.live.core.ui.main.message.chatmenu.ReportChat
import com.example.administrator.live.core.ui.main.message.chatmenu.ReportingEvidence
import com.example.administrator.live.core.ui.main.message.chatmenu.SelectFromPhoto
import com.example.administrator.live.core.ui.main.mine.MineScreen
import com.example.administrator.live.core.viewmodels.CameraViewModel
import com.example.administrator.live.core.viewmodels.ChatViewModel
import com.example.administrator.live.core.viewmodels.FriendViewModel
import com.example.administrator.live.core.viewmodels.HomeViewModel
import com.example.administrator.live.core.viewmodels.MineViewModel
import com.example.administrator.live.core.viewmodels.SelectFromPhotoViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
) {
    // 聊天相关的ViewModel
    val chatViewModel: ChatViewModel = viewModel()
    // 我的相关ViewModel
    val mineViewModel: MineViewModel = viewModel()
    // 首页相关ViewModel
    val homeViewModel: HomeViewModel = viewModel()
    // 好友相关ViewModel
    val friendViewModel: FriendViewModel = viewModel()
    // 拍摄相关ViewModel
    val cameraViewModel: CameraViewModel = viewModel()
    // 图片选择ViewModel
    val selectFromPhotoViewModel: SelectFromPhotoViewModel = viewModel()
    NavHost(navController = navController, startDestination = Graph.Home.route) {
        homeGraph(navController, homeViewModel)
        friendGraph(navController, friendViewModel)
        messageGraph(navController, chatViewModel)
        mineGraph(navController, mineViewModel)
        cameraGraph(navController, cameraViewModel)
        //选择图片
        composable(Screen.SelectBgPhoto("{from}").route) { backStackEntry ->
            val from = backStackEntry.arguments?.getString("from")
            when (from) {
                Screen.ChatBackgroundSet.route -> {
                    selectFromPhotoViewModel.selectMode = false
                    selectFromPhotoViewModel.onImageClick = {
                        chatViewModel.setBg(it)
                        navController.popBackStack()
                    }
                }
                Screen.ReportChat.route -> {
                    selectFromPhotoViewModel.selectedImgList.clear()
                    selectFromPhotoViewModel.selectedImgList.addAll(chatViewModel.reportImages)
                    selectFromPhotoViewModel.selectMode = true
                    selectFromPhotoViewModel.onImageClick = { img ->
                        if (chatViewModel.reportImages.contains(img)) {
                            chatViewModel.reportImages.remove(img)
                            selectFromPhotoViewModel.selectedImgList.remove(img)
                        } else {
                            if (chatViewModel.reportImages.size < 6) {
                                chatViewModel.reportImages.add(img)
                                selectFromPhotoViewModel.selectedImgList.add(img)
                            }
                        }
                    }
                }
                Screen.CameraText.route->{
                    selectFromPhotoViewModel.selectMode = false
                    selectFromPhotoViewModel.onImageClick = {
                        cameraViewModel.imageFilePath = Image.StringImage(it)
                        navController.popBackStack()
                    }
                }
            }
            SelectFromPhoto(navController, selectFromPhotoViewModel)
        }
    }
}

/**
 * 主页
 * @receiver NavGraphBuilder
 * @param navController NavHostController
 * @param viewModel HomeViewModel
 */
fun NavGraphBuilder.homeGraph(navController: NavHostController, viewModel: HomeViewModel) {
    navigation(startDestination = Screen.Home.route, route = Graph.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController, viewModel) }
    }

}

/**
 * 好友
 * @receiver NavGraphBuilder
 * @param navController NavHostController
 * @param viewModel FriendViewModel
 */
fun NavGraphBuilder.friendGraph(navController: NavHostController,viewModel: FriendViewModel) {
    navigation(startDestination = Screen.Friend.route, route = Graph.Friend.route) {
        composable(Screen.Friend.route) { FriendScreen(navController, viewModel) }
    }
}

/**
 * 发布
 * @receiver NavGraphBuilder
 * @param navController NavHostController
 * @param viewModel CameraViewModel
 */
fun NavGraphBuilder.cameraGraph(navController: NavHostController,viewModel: CameraViewModel) {
    navigation(startDestination = Screen.Camera.route, route = Graph.Camera.route) {
        composable(Screen.Camera.route) { CameraScreen(navController,viewModel) }
        composable(Screen.CameraText.route) { CameraTextScreen(navController,viewModel) }
        composable(Screen.Publish.route) { PublishScreen() }
    }
}


/**
 * 消息
 * @receiver NavGraphBuilder
 * @param navController NavHostController
 * @param viewModel ChatViewModel
 */
fun NavGraphBuilder.messageGraph(navController: NavHostController, viewModel: ChatViewModel) {
    navigation(startDestination = Screen.Message.route, route = Graph.Message.route) {
        composable(Screen.Message.route) { MessageMainScreen(navController, viewModel) }
        composable(Screen.NewFans.route) { NewFansScreen(navController, viewModel) }
        composable(Screen.SystemNotice.route) { SystemNoticeScreen(navController, viewModel) }
        composable(Screen.UserNotice.route) { UserNoticeScreen(navController, viewModel) }
        chatRoom(navController, viewModel)
    }
}

/**
 * 聊天室
 * @receiver NavGraphBuilder
 * @param navController NavHostController
 * @param viewModel ChatViewModel
 */
fun NavGraphBuilder.chatRoom(navController: NavHostController, viewModel: ChatViewModel) {
    navigation(startDestination = Screen.ChatRoom.route, route = Graph.ChatRoom.route) {
        composable(Screen.ChatRoom.route) { ChatRoomScreen(navController, viewModel) }
        chatMenuGraph(navController, viewModel)
    }
}

/**
 * 聊天菜单
 * @receiver NavGraphBuilder
 * @param navController NavHostController
 * @param viewModel ChatViewModel
 */
fun NavGraphBuilder.chatMenuGraph(navController: NavHostController, viewModel: ChatViewModel) {
    navigation(startDestination = Screen.ChatMenu.route, route = Graph.ChatMenu.route) {
        composable(Screen.ChatMenu.route) { ChatMenu(navController, viewModel) }
        composable(Screen.ChatBackgroundSet.route) { ChatBackgroundSet(navController, viewModel) }
        composable(Screen.ChatPhoto.route) { ChatPhoto(navController, viewModel) }
        composable(Screen.ChatHistory.route) { FindChatHistory(navController, viewModel) }
        composable(Screen.GroupNotice.route) { GroupNotice(navController, viewModel) }
        composable(Screen.InviteMember.route) { InvitationMember(navController, viewModel) }
        composable(Screen.OwnerSpeak.route) { OwnerSpeak(navController, viewModel) }
        reportChat(navController, viewModel)
    }
}

/**
 * 举报
 * @receiver NavGraphBuilder
 * @param navController NavHostController
 * @param viewModel ChatViewModel
 */
fun NavGraphBuilder.reportChat(navController: NavHostController, viewModel: ChatViewModel) {
    navigation(startDestination = Screen.ReportChat.route, route = Graph.ReportChat.route){
        composable(Screen.ReportChat.route) { ReportChat(navController, viewModel) }
        composable(Screen.ReportingEvidence.route) { ReportingEvidence(navController, viewModel) }
    }
}

/**
 * 我的
 * @receiver NavGraphBuilder
 * @param navController NavHostController
 * @param viewModel MineViewModel
 */
fun NavGraphBuilder.mineGraph(navController: NavHostController,viewModel: MineViewModel) {
    navigation(startDestination = Screen.Mine.route, route = Graph.Mine.route) {
        composable(Screen.Mine.route) { MineScreen(navController, viewModel) }
    }
}