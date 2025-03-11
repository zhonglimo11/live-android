package com.example.administrator.live.core.ui.main.message.chatmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.MessageList
import com.example.administrator.live.core.viewmodels.ChatViewModel

@Composable
fun OwnerSpeak(navController: NavController, viewModel: ChatViewModel) {
    BaseScreen(
        title = "群主发言",
        backgroundColor = Color.White,
        navController = navController
    ) {
        Column(
            Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            MessageList(messageList = viewModel.messageList, isOwnerSpeak = true)
        }
    }
}