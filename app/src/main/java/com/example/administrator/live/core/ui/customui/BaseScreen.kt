package com.example.administrator.live.core.ui.customui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.theme.PurpleGrey

@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color = PurpleGrey,
    topBarColor: Color = backgroundColor,
    title: String = "",
    hasTitle: Boolean = true,
    hasLine: Boolean = true,
    navController: NavController? = null,
    @DrawableRes customBackRes: Int = R.drawable.back_black,
    centerContent: @Composable () -> Unit = {},
    topBarMenu: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Box(
        Modifier
            .navigationBarsPadding()
            .imePadding(),
    ) {
        Box(
            modifier = modifier
                .padding(
                    top = 40.dp,
                )
                .align(Alignment.BottomStart)
                .statusBarsPadding()
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            content()
        }
        Box(Modifier.background(topBarColor)){
            CustomTitleBar(
                title,
                hasTitle,
                hasLine,
                centerContent,
                navController,
                customBackRes = customBackRes,
                backGroundColor = topBarColor
            ) {
                topBarMenu()
            }
        }
    }
}

@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color = PurpleGrey,
    paddingHorizontal: Int = 20,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = { bottomBar() },
        containerColor = backgroundColor,
    ) { innerPadding ->
        Box(
            modifier = modifier.padding(
                paddingHorizontal.dp,
                innerPadding.calculateTopPadding(),
                paddingHorizontal.dp,
                innerPadding.calculateBottomPadding()
            )
        ) {
            content()
        }
    }
}