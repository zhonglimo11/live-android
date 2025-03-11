package com.example.administrator.live.core.ui.main.mine

import com.example.administrator.live.utils.ExoPlayerManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.administrator.live.R
import com.example.administrator.live.bean.Commodity
import com.example.administrator.live.bean.Image
import com.example.administrator.live.bean.Music
import com.example.administrator.live.core.viewmodels.MineViewModel
import com.example.administrator.live.core.ui.customui.CircularImage
import com.example.administrator.live.core.ui.customui.CustomTabRow
import com.example.administrator.live.core.ui.customui.EmptyList
import com.example.administrator.live.core.ui.customui.Margin
import com.example.administrator.live.core.ui.customui.MusicItem
import com.example.administrator.live.core.ui.customui.NumberWithText
import com.example.administrator.live.core.ui.customui.SimpleImage
import com.example.administrator.live.core.ui.customui.SimpleText
import com.example.administrator.live.core.ui.customui.VideoList
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.ui.theme.HalfTransparent
import com.example.administrator.live.core.ui.theme.PurpleGrey
import com.example.administrator.live.core.ui.theme.Red
import com.example.administrator.live.utils.AppUtils.formatNumber
import com.example.administrator.live.utils.fixtures.FixturesData.Companion.getRandomImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MineScreen(navController: NavHostController, viewModel: MineViewModel) {
    val userAvatar = getRandomImage()
    val tabs = listOf("作品", "私密", "收藏", "喜欢")
    val pagerState = rememberPagerState { tabs.size }
    val user = viewModel.user
    val userInfo = if (user.info == "") "点击添加介绍，让大家了解你" else user.info
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp)
            .navigationBarsPadding()
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(325.dp)
        ) {
            when (val bg = viewModel.bgImg) {
                is Image.IntImage -> Image(
                    painterResource(bg.value),
                    null,
                    Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                is Image.StringImage -> AsyncImage(
                    model = bg.value,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
            Image(
                painterResource(R.drawable.bg_gradient),
                null,
                Modifier
                    .fillMaxWidth()
                    .height(161.dp)
                    .align(Alignment.BottomCenter),
                contentScale = ContentScale.FillBounds
            )
            Column {
                Margin(40)
                Row(Modifier.padding(horizontal = 20.dp)) {
                    Spacer(Modifier.weight(1f))
                    Image(
                        painterResource(R.drawable.img_button_setting), null, Modifier.size(20.dp)
                    )
                }
                Margin(4)
                Row(Modifier.padding(start = 20.dp)) {
                    CircularImage(userAvatar, 68.dp)
                    Margin(0, 16)
                    Column {
                        Text(
                            user.name,
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Margin(4)
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SimpleText("逛逛号: ${user.id}", 12, Color.White)
                            Margin(0, 2)
                            SimpleImage(R.drawable.img_qr, 8, 8)
                        }
                        Row {
                            Text(
                                userInfo,
                                Modifier
                                    .widthIn(max = 160.dp)
                                    .padding(end = 4.dp),
                                Color.White,
                                12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            SimpleImage(R.drawable.img_edit, 16, 16)
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .padding(6.dp, 5.dp)
                            .size(60.dp, 28.dp)
                            .background(HalfTransparent, shape = RoundedCornerShape(17.dp))
                            .border(0.5.dp, Color.White, RoundedCornerShape(17.dp))
                            .align(Alignment.Bottom), contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SimpleImage(R.drawable.img_shop, 20, 20)
                            Text(
                                text = "商城",
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
                Margin(20)
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 20.dp),
                    color = HalfTransparent,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        NumberWithText(viewModel.favoritesNum.toString(), 16, "收藏")
                        VerticalDivider(
                            color = Color.White, modifier = Modifier.padding(vertical = 12.dp)
                        )
                        NumberWithText(viewModel.consumptionRedPacketNum.toString(), 16, "消费红包")
                        VerticalDivider(
                            color = Color.White, modifier = Modifier.padding(vertical = 12.dp)
                        )
                        NumberWithText(viewModel.cashRedPacketNum.toString(), 16, "现金红包")
                    }
                }
                Margin(16)
                Row(
                    Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    with(viewModel) {
                        NumberWithText(fansList.size.toString(), 20, "粉丝")
                        NumberWithText(followList.size.toString(), 20, "关注")
                        NumberWithText(friendList.size.toString(), 20, "朋友")
                        NumberWithText(likesReceivedNum.toString(), 20, "获赞")
                    }
                }
            }
        }
        Surface(
            Modifier
                .fillMaxSize()
                .padding(top = 283.dp),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        ) {
            Column {
                CustomTabRow(
                    Modifier.padding(horizontal = 40.dp),
                    pagerState,
                    tabs = tabs,
                    tabSize = 16,
                    FontWeight.Normal
                )
                Margin(8)
                HorizontalPager(
                    state = pagerState, modifier = Modifier.fillMaxSize()
                ) { page ->
                    val videos = when (page) {
                        0 -> viewModel.worksVideoList
                        1 -> viewModel.privateVideoList
                        3 -> viewModel.likedVideoList
                        else -> emptyList()
                    }
                    when (page) {
                        0, 1, 3 -> VideoList(videos)
                        2 -> FavoriteList(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteList(viewModel: MineViewModel) {
    val selectedIndex = remember { mutableIntStateOf(0) }
    val videoCount = viewModel.favoriteVideoList.size
    val musicCount = viewModel.favoriteMusicList.size
    val commCount = viewModel.favorCommodityList.size
    if (videoCount == 0) {
        selectedIndex.intValue = 1
        if (musicCount == 0) {
            selectedIndex.intValue = 2
        } else {
            selectedIndex.intValue = 3
        }
    }
    // 标签列表
    val items = listOf(
        "视频 · $videoCount" to videoCount,
        "音乐 · $musicCount" to musicCount,
        "商品 · $commCount" to commCount
    )
    Column {
        HorizontalDivider()
        Row(
            Modifier.padding(16.dp, 8.dp)
        ) {
            items.forEachIndexed { index, item ->
                val (label, count) = item
                if (count > 0) {
                    FavoriteTabItem(
                        label = label,
                        isSelected = selectedIndex.intValue == index,
                        onClick = { selectedIndex.intValue = index }
                    )
                    if (index < items.size - 1) {
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }
        }
        HorizontalDivider()
        when (selectedIndex.intValue) {
            0 -> VideoList(viewModel.favoriteVideoList)
            1 -> FavoriteMusicList(viewModel.favoriteMusicList)
            2 -> FavoriteCommodityList(viewModel.favorCommodityList)
            else -> EmptyList()
        }
    }
}

@Composable
fun FavoriteTabItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val textColor = if (isSelected) FontColor else FontColor2
    val backgroundColor =
        if (isSelected) Modifier.background(PurpleGrey, RoundedCornerShape(12.dp)) else Modifier
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(backgroundColor)
            .padding(12.dp, 4.dp)
    ) {
        SimpleText(label, 14, color = textColor)
    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun FavoriteMusicList(musics: List<Music>) {
    val context = LocalContext.current
    val musicPlayer = remember { ExoPlayerManager.getInstance(context) }
    var currentPlayingIndex by remember { mutableStateOf("0") }
    LazyColumn(
        Modifier.padding(16.dp, 8.dp, 16.dp, 0.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(musics) { music ->
            MusicItem(
                music,
                false,
                musicPlayer,
                currentPlayingIndex
            ) { index ->
                currentPlayingIndex = index // 更新当前播放索引
            }
        }
    }
    LaunchedEffect(Unit) {
        musicPlayer
    }
    DisposableEffect(Unit) {
        onDispose {
            musicPlayer.stopMusic()
        }
    }
}

@Composable
fun FavoriteCommodityList(commodityList: List<Commodity>) {
    LazyColumn(
        Modifier.padding(16.dp, 8.dp, 16.dp, 0.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(commodityList) {
            Row(
                Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = it.cover,
                    contentDescription = null,
                    modifier = Modifier
                        .size(66.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.image_empty),
                )
                Margin(0, 14)
                Column {
                    SimpleText(it.name, 16)
                    Margin(2)
                    SimpleText(formatNumber(it.likeCount) + "人想要", 14, FontColor2)
                    Margin(2)
                    SimpleText("￥ ${it.price}", 12, Red)
                }
            }
        }
    }
}