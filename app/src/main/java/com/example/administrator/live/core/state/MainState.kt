package com.example.administrator.live.core.state

import com.example.administrator.live.R

interface MainState {
    val backGroundColor: Int
    val selectedTabColor: Int
    val unselectedTabColor: Int
    val addIcon: Int

    data object Black : MainState {
        override val backGroundColor = R.color.black
        override val selectedTabColor = R.color.white
        override val unselectedTabColor = R.color.fontColor_2
        override val addIcon = R.drawable.icon_add
    }

    data object White : MainState {
        override val backGroundColor = R.color.white
        override val selectedTabColor = R.color.black
        override val unselectedTabColor = R.color.fontColor_2
        override val addIcon = R.drawable.icon_add_black
    }
}

