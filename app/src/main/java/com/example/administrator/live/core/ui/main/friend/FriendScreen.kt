package com.example.administrator.live.core.ui.main.friend

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.customui.CustomTitleBar
import com.example.administrator.live.core.ui.customui.VideoListScreen
import com.example.administrator.live.core.viewmodels.FriendViewModel

@Composable
fun FriendScreen(navController: NavHostController,viewModel: FriendViewModel) {
    val onFriendSearch = {}
    val friendVideos = viewModel.friendVideoList
    Box {
        VideoListScreen(friendVideos)
        CustomTitleBar(
            customBackRes = R.drawable.back_white,
            hasLine = false,
            hasTitle = false,
        ) {
            Image(painter = painterResource(id = R.mipmap.search_icon),
                contentDescription = null,
                modifier = Modifier
                    .clickable { onFriendSearch() }
                    .size(20.dp))
        }
    }
}