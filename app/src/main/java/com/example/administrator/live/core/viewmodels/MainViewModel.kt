package com.example.administrator.live.core.viewmodels

import com.example.administrator.live.core.intent.MainIntent
import com.example.administrator.live.core.state.MainState
import com.example.administrator.live.core.ui.navhost.Screen

class MainViewModel : BaseViewModel<MainIntent, MainState>(MainState.Black) {

    override suspend fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.Navigation -> {
                val tabPosition = intent.route
                when (tabPosition) {
                    Screen.Home.route,Screen.Friend.route,Screen.Camera.route
                    -> state.value = MainState.Black
                    else -> state.value = MainState.White
                }
            }
        }
    }
}