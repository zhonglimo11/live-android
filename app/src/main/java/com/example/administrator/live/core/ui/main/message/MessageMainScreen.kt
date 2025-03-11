package com.example.administrator.live.core.ui.main.message

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.administrator.live.R
import com.example.administrator.live.bean.Chat
import com.example.administrator.live.core.ui.customui.CustomTabRow
import com.example.administrator.live.core.ui.navhost.Screen
import com.example.administrator.live.core.ui.theme.GradientBrush
import com.example.administrator.live.core.ui.theme.GradientBrush2
import com.example.administrator.live.core.ui.theme.GradientBrush3
import com.example.administrator.live.core.ui.theme.Red
import com.example.administrator.live.core.viewmodels.ChatViewModel
import com.example.administrator.live.core.viewmodels.UserAction
import com.example.administrator.live.holder.DialogViewHolder
import com.example.administrator.live.utils.AppUtils.dpToPx
import com.stfalcon.chatkit.dialogs.DialogsList
import com.stfalcon.chatkit.dialogs.DialogsListAdapter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageMainScreen(navController: NavHostController, viewModel: ChatViewModel) {
    val tabs = listOf("通知", "消息")
    val pagerState = rememberPagerState { tabs.size }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(bottom = 56.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.padding(horizontal = 20.dp)) {
            CustomTabRow(Modifier.padding(horizontal = 80.dp), pagerState, tabs, 18)
            Image(
                painterResource(R.mipmap.search_black), null,
                Modifier
                    .size(20.dp)
                    .align(Alignment.CenterEnd)
            )
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> NotificationScreen(navController, viewModel)
                1 -> MessageScreen(navController, viewModel)
            }
        }
    }
}

@Composable
fun NotificationScreen(
    navController: NavHostController,
    viewModel: ChatViewModel
) {
    val adapter: DialogsListAdapter<Chat> = remember {
        DialogsListAdapter<Chat>(
            R.layout.item_message,
            DialogViewHolder::class.java,
            viewModel.imageLoader
        ).apply {
            setItems(viewModel.systemChats)
            setOnDialogClickListener { chat ->
                viewModel.updateChatId(chat.id)
                when (chat.id) {
                    "systemNotice" -> {
                        navController.navigate(Screen.SystemNotice.route)
                    }

                    "newFollow" -> {
                        navController.navigate(Screen.NewFans.route)
                    }

                    else -> {
                        navController.navigate(Screen.ChatRoom.route)
                    }
                }
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp, 31.dp, 16.dp)
    ) {
        ChatListView(adapter = adapter)
    }
}

@Composable
fun MessageScreen(
    navController: NavHostController,
    viewModel: ChatViewModel
) {
    val adapter: DialogsListAdapter<Chat> = remember {
        DialogsListAdapter<Chat>(
            R.layout.item_message,
            DialogViewHolder::class.java,
            viewModel.imageLoader
        ).apply {
            setItems(viewModel.userChats)
            setOnDialogClickListener {
                viewModel.updateChatId(it.id)
                navController.navigate(Screen.ChatRoom.route)
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp, 32.dp, 16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            with(viewModel) {
                IconWithCount(
                    GradientBrush,
                    "@我",
                    R.drawable.emoji_01,
                    unreadETCount
                ) {
                    action = UserAction.E_TER
                    unreadETCount = 0
                    navController.navigate(Screen.UserNotice.route)
                }
                IconWithCount(
                    GradientBrush2,
                    "评论",
                    R.drawable.emoji_12,
                    unreadCommentCount
                ) {
                    action = UserAction.COMMENTS
                    unreadCommentCount = 0
                    navController.navigate(Screen.UserNotice.route)
                }
                IconWithCount(
                    GradientBrush3,
                    "获赞",
                    R.drawable.emoji_02,
                    unreadLikeCount
                ) {
                    action = UserAction.THUMBS_UP
                    unreadLikeCount = 0
                    navController.navigate(Screen.UserNotice.route)
                }
            }
        }
        ChatListView(32, adapter)
    }
}

@Composable
fun IconWithCount(
    backgroundColor: Brush,
    text: String,
    iconResId: Int,
    count: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(98.dp)
            .height(55.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .width(95.dp)
                .height(50.dp)
                .align(Alignment.BottomStart)
                .background(brush = backgroundColor, shape = RoundedCornerShape(16.dp))
                .padding(start = 16.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.BottomEnd)
            )
        }
        if (count > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .border(2.dp, Color.White, RoundedCornerShape(10.dp))
                    .widthIn(min = 20.dp)
                    .padding(horizontal = 1.dp)
                    .height(20.dp)
            ) {
                Box(
                    Modifier
                        .widthIn(min = 18.dp)
                        .height(18.dp)
                        .align(Alignment.CenterStart)
                        .background(Red, RoundedCornerShape(10.dp))
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "$count",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

            }
        }
    }
}

@Composable
fun ChatListView(
    paddingTop: Int = 1,
    adapter: DialogsListAdapter<Chat>
) {
    AndroidView(
        factory = {
            DialogsList(it).apply {
                setAdapter(adapter)
                setPadding(0, it.dpToPx(paddingTop), 0, 0)
                isNestedScrollingEnabled = false
            }
        },
        Modifier.fillMaxSize()
    )
}