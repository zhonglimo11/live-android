package com.example.administrator.live.core.ui.main.message.chatmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.ImageMessageList
import com.example.administrator.live.core.viewmodels.ChatViewModel

@Composable
fun ChatPhoto(navController: NavController,viewModel: ChatViewModel) {
    BaseScreen(
        title = viewModel.title,
        backgroundColor = Color.White,
        navController = navController) {
        Column {
            ImageMessageList(viewModel.imgMessageList)
        }
    }
}