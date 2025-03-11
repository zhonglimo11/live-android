package com.example.administrator.live.core.ui.main.message

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.administrator.live.R
import com.example.administrator.live.adapters.UserInteractionsListAdapter
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.RecyclerList
import com.example.administrator.live.core.viewmodels.ChatViewModel
import com.example.administrator.live.core.viewmodels.UserAction

@Composable
fun UserNoticeScreen(navController: NavController, viewModel: ChatViewModel) {
    val action = viewModel.action
    val adapter = UserInteractionsListAdapter(
        R.layout.item_user_interactions,
        when(viewModel.action){
            UserAction.E_TER-> viewModel.ets
            UserAction.COMMENTS-> viewModel.comments
            UserAction.THUMBS_UP-> viewModel.likes
        }
    )
    BaseScreen(
        title = action.action,
        backgroundColor = Color.White,
        navController = navController
    ) {
        RecyclerList(20, adapter)
    }
}