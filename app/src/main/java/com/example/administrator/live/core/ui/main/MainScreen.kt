package com.example.administrator.live.core.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.administrator.live.core.intent.MainIntent
import com.example.administrator.live.core.state.MainState
import com.example.administrator.live.core.ui.navhost.MainNavHost
import com.example.administrator.live.core.ui.navhost.Screen
import com.example.administrator.live.core.ui.theme.LineColor2
import com.example.administrator.live.core.viewmodels.MainViewModel
import kotlinx.coroutines.channels.Channel

@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {
    val navController = rememberNavController()
    val uiState by viewModel.state.collectAsState()
    val intent = viewModel.intent
    val items = listOf(Screen.Home.route,Screen.Friend.route, Screen.Camera.route,Screen.Message.route, Screen.Mine.route)
    var currentRoute by remember { mutableStateOf<String?>(items[0]) }
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            currentRoute = backStackEntry.destination.route
            intent.trySend(MainIntent.Navigation(currentRoute?:items[0]))
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MainNavHost(navController)
        if (currentRoute in items) {
            if (currentRoute == Screen.Camera.route){
                return
            }
            BottomNavigationBar(
                items,
                currentRoute?:items[0],
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(
                        colorResource(
                            uiState.backGroundColor
                        )
                    )
                    .navigationBarsPadding(),
                navController = navController,
                uiState = uiState,
                intent = intent
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<String>,
    currentTab: String,
    modifier: Modifier,
    navController: NavHostController,
    uiState: MainState,
    intent: Channel<MainIntent>
) {
    BottomAppBar(
        modifier = modifier.height(56.dp),
        containerColor = Color.Transparent,
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            if (uiState == MainState.Black) {
                HorizontalDivider(color = LineColor2)
            }
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    BottomNavItem(
                        uiState,
                        label = when (item) {
                            Screen.Home.route -> "首页"
                            Screen.Friend.route -> "好友"
                            Screen.Camera.route -> ""
                            Screen.Message.route -> "消息"
                            Screen.Mine.route -> "我的"
                            else -> ""
                        },
                        icon = if (item == Screen.Camera.route) painterResource(id = uiState.addIcon) else null,
                        selected = currentTab == item,
                        onClick = {
                            navController.navigate(item)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavItem(
    uiState: MainState,
    icon: Painter? = null,
    label: String? = null,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }) {
        icon?.let {
            Image(
                modifier = Modifier.size(32.dp), painter = it, contentDescription = label
            )
        }
        label?.let {
            Text(
                text = it,
                color = colorResource(if (selected) uiState.selectedTabColor else uiState.unselectedTabColor),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}