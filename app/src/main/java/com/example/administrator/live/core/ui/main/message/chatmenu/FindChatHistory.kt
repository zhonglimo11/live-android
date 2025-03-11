package com.example.administrator.live.core.ui.main.message.chatmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.Margin
import com.example.administrator.live.core.ui.customui.MessageList
import com.example.administrator.live.core.ui.customui.SearchView
import com.example.administrator.live.core.ui.customui.SimpleImageWithTextBox
import com.example.administrator.live.core.ui.navhost.Screen
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.viewmodels.ChatViewModel

@Composable
fun FindChatHistory(navController: NavController, viewModel: ChatViewModel) {
    with(viewModel) {
        BaseScreen(title = "查找聊天记录",
            backgroundColor = Color.White,
            navController = navController,
            hasTitle = false,
            hasLine = false,
            centerContent = {
                Margin(0, 8)
                SearchView(chatHistoryInput, { updateChatHistoryInput(it) }, "搜索聊天内容")
            }) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (chatHistoryInput.isNotEmpty()) {
                    val isSearching = chatHistoryInput.isNotEmpty()
                    val filteredFriends = remember(messageList, chatHistoryInput) {
                        if (isSearching) {
                            messageList.filter {
                                it.text.contains(
                                    chatHistoryInput,
                                    ignoreCase = true
                                )
                            }
                        } else {
                            messageList
                        }
                    }
                    Margin(12)
                    MessageList(filteredFriends, chatHistoryInput)
                } else {
                    Margin(80)
                    Text("快速搜索聊天内容", color = FontColor2, fontSize = 16.sp)
                    Margin(38)
                    Row {
                        var width = 36
                        val title = "分享内容"
                        val titleTwo = "聊天相册"
                        if (users.size > 1) {
                            SimpleImageWithTextBox(
                                R.drawable.icon_chat,
                                "群主发言"
                            ) { navController.navigate(Screen.OwnerSpeak.route) }
                            Margin(0, 36)
                        } else {
                            width = 76
                        }
                        SimpleImageWithTextBox(
                            R.drawable.icon_img,
                            titleTwo
                        ) {
                            viewModel.title = titleTwo
                            navController.navigate(Screen.ChatPhoto.route)
                        }
                        Margin(0, width)
                        SimpleImageWithTextBox(
                            R.drawable.icon_videos,
                            title
                        ) {
                            viewModel.title = title
                            navController.navigate(Screen.ChatPhoto.route)
                        }
                    }
                }
            }
        }
    }
}