package com.example.administrator.live.core.ui.main.message

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.administrator.live.R
import com.example.administrator.live.adapters.NewFansListAdapter
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.RecyclerList
import com.example.administrator.live.core.viewmodels.ChatViewModel
import com.example.administrator.live.utils.fixtures.FixturesData.Companion.getNewFansList

@Composable
fun NewFansScreen(navController: NavController,viewModel: ChatViewModel) {
    val data = getNewFansList()
    val adapter = NewFansListAdapter(R.layout.item_fans, data)
    BaseScreen(
        title = "新增关注",
        backgroundColor = Color.White,
        navController = navController
    ) {
        RecyclerList(20,adapter)
    }
}