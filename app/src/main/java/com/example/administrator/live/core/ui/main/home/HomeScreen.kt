package com.example.administrator.live.core.ui.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.administrator.live.R
import com.example.administrator.live.bean.Video
import com.example.administrator.live.core.ui.customui.CustomTitleBar
import com.example.administrator.live.core.ui.customui.VideoListScreen
import com.example.administrator.live.core.ui.theme.LineColor
import com.example.administrator.live.core.viewmodels.HomeViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel) {
    val tabs = listOf("商城", "关注", "推荐", "同城", "直播")
    val pagerState = rememberPagerState(initialPage = 2) { tabs.size }
    val selectedIndex = pagerState.currentPage
    val scrollState = rememberScrollState()
    val tabWidth = 55
    LaunchedEffect(selectedIndex) {
        val scrollOffset = (selectedIndex - 2) * tabWidth * 3
        scrollState.animateScrollTo(scrollOffset)
    }
    val coroutineScope = rememberCoroutineScope()
    val onHomeSearch = {}
    val onRefresh = {}
    Box {
        // 主内容页
        HorizontalPager(state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(onDoubleClick = { onRefresh() }) {}) { page ->
            when (page) {
                0 -> ShopContent()
                1 -> FollowContent(viewModel.recommendVideoList)
                2 -> RecommendContent(viewModel.recommendVideoList)
                3 -> CityContent()
                4 -> LiveContent()
            }
        }
        CustomTitleBar(
            customBackRes = R.drawable.back_white,
            hasLine = false,
            hasTitle = false,
            centerContent = {
                Box(
                    modifier = Modifier.width(300.dp).padding(end = 20.dp).horizontalScroll(scrollState)
                ) {
                    val tabRowWidth = (tabs.size * tabWidth).dp
                    TabRow(
                        selectedTabIndex = selectedIndex,
                        containerColor = Color.Transparent,
                        modifier = Modifier.width(tabRowWidth),
                                indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex])
                                    .padding(horizontal = 15.dp),
                                color = Color.White
                            )
                        },
                        divider = {}
                    ) {
                        tabs.forEachIndexed { index, title ->
                            val selected = selectedIndex == index
                            Tab(
                                modifier = Modifier.padding(vertical = 8.dp).height(25.dp),
                                selected = selected,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.scrollToPage(index)
                                    }
                                }
                            ) {
                                Text(
                                    title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (selected) Color.White else LineColor
                                )
                            }
                        }
                    }
                }
            }
        ) {
            Image(painter = painterResource(id = R.mipmap.search_icon),
                contentDescription = null,
                modifier = Modifier
                    .clickable { onHomeSearch() }
                    .size(20.dp))
        }
    }
}

@Composable
fun ShopContent() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.Center
    ) {
        Text("商城", color = Color.White)
    }
}

@Composable
fun FollowContent(followVideoList: StateFlow<List<Video>>) {
    val recommendVideos by followVideoList.collectAsState()
    Box(
        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        VideoListScreen(recommendVideos)
    }
}

@Composable
fun RecommendContent(recommendVideoList: StateFlow<List<Video>>) {
    val recommendVideos by recommendVideoList.collectAsState()
    Box(
        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        VideoListScreen(recommendVideos)
    }
}

@Composable
fun CityContent() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.Center
    ) {
        Text("同城", color = Color.White)
    }
}

@Composable
fun LiveContent() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.Center
    ) {
        Text("直播", color = Color.White)
    }
}