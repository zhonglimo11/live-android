package com.example.administrator.live.core.intent


interface MainIntent {
    data class Navigation(val route: String) : MainIntent
}