package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import com.example.administrator.live.R
import com.example.administrator.live.bean.Music
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.ui.theme.LineColor
import com.example.administrator.live.utils.AppUtils
import com.example.administrator.live.utils.ExoPlayerManager

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun MusicItem(
    music: Music,
    isBig: Boolean,
    musicPlayer: ExoPlayerManager,
    currentPlayingIndex: String,
    onMusicPlay: (String) -> Unit
) {
    val coverSize = if (isBig) 80.dp else 68.dp
    val imageHeight = if (isBig) 70.dp else 60.dp
    val nameSize = if (isBig) 20 else 18
    val authorSize = if (isBig) 14 else 12
    val usedCountSize = if (isBig) 12 else 10
    val playImageSize = if (isBig) 24.dp else 20.dp
    val coverBoxWidth = if (isBig) 98.dp else 83.dp
    val timeWidth = if (isBig) 35.dp else 32.dp
    val timeHeight = if (isBig) 17.dp else 14.dp
    val timeSize = if (isBig) 12 else 10
    val playImageRes = if (currentPlayingIndex == music.id) // 假设 music.id 是音乐的唯一标识
        painterResource(R.drawable.music_pause)
    else
        painterResource(R.drawable.music_play)
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(coverBoxWidth, coverSize)
        ) {
            Box(
                modifier = Modifier
                    .size(coverSize)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        val index = music.id
                        if (currentPlayingIndex == index) {
                            musicPlayer.stopMusic()
                            onMusicPlay("0") // 更新当前播放索引
                        } else {
                            musicPlayer.playMusic(music.url, context)
                            onMusicPlay(index) // 更新当前播放索引
                        }
                    }
            ) {
                AsyncImage(
                    model = music.cover,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.image_empty),
                )
                Image(
                    playImageRes, null,
                    Modifier
                        .size(playImageSize)
                        .align(Alignment.Center)
                )
                Box(
                    Modifier
                        .background(Color(0x4D000000), RoundedCornerShape(topStart = 8.dp))
                        .size(timeWidth, timeHeight)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    SimpleText(AppUtils.formatDuration(music.duration), timeSize, Color.White)
                }
            }
            Image(
                painterResource(R.drawable.half_record),
                null,
                Modifier
                    .height(imageHeight)
                    .align(Alignment.CenterEnd),
                contentScale = ContentScale.FillHeight
            )
        }
        Margin(0, 14)
        Column {
            SimpleText(music.name, nameSize)
            Margin(4)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .background(LineColor, RoundedCornerShape(2))
                        .padding(horizontal = 3.dp)
                ) {
                    SimpleText("制作人", 8)
                }
                Margin(0, 8)
                SimpleText(music.author, authorSize, FontColor2)
            }
            Margin(4)
            SimpleText(AppUtils.formatNumber(music.usedCount) + "人使用", usedCountSize, FontColor2)
        }
        Spacer(Modifier.weight(1f))
        if (!isBig) {
            Column(
                Modifier
                    .size(20.dp)
                    .padding(3.dp, 5.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                HorizontalDivider(color = FontColor)
                HorizontalDivider(Modifier.padding(start = 2.dp), color = FontColor)
                HorizontalDivider(Modifier.padding(start = 4.dp), color = FontColor)
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val text = if (music.isLike) "已收藏" else "收藏"
                if (music.isLike) {
                    SimpleImage(R.drawable.icon_star_yellow, 32, 32)
                } else {
                    Image(
                        painterResource(R.drawable.icon_star),
                        null,
                        Modifier.size(32.dp),
                        colorFilter = ColorFilter.tint(Color(0xFF999999))
                    )
                }
                Margin(8)
                SimpleText(text, 14, FontColor2)
            }
        }
    }
}