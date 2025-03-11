package com.example.administrator.live.core.ui.main.message

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.administrator.live.R
import com.example.administrator.live.adapters.NoticeListAdapter
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.RecyclerList
import com.example.administrator.live.core.viewmodels.ChatViewModel
import com.example.administrator.live.utils.fixtures.FixturesData.Companion.getNoticeList

@Composable
fun SystemNoticeScreen(navController: NavController, chatViewModel: ChatViewModel) {
    BaseScreen(
        title = "系统公告",
        navController = navController
    ) {
        val adapter = NoticeListAdapter(R.layout.item_notice, getNoticeList())
        RecyclerList(16, adapter)
    }
}