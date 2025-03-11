package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.administrator.live.R
import com.example.administrator.live.bean.Video

@Composable
fun VideoList(videos: List<Video>) {
    if (videos.isEmpty()) {
        EmptyList()
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(3.dp),// 水平间隔
            verticalArrangement = Arrangement.spacedBy(3.dp)// 垂直间隔
        ) {
            items(videos) {
                CustomImageBox(it.cover ?: "") {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(6.dp, 8.dp)
                    ) {
                        val isLiked = it.isLike
                        val likeImg = if (isLiked) R.drawable.icon_like else R.drawable.icon_unlike
                        if (it.createTime == null) {
                            Text(
                                "草稿 ${it.title}",
                                Modifier.align(Alignment.BottomEnd),
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        } else {
                            Row(Modifier.align(Alignment.BottomEnd)) {
                                SimpleImage(likeImg, 12, 12)
                                Margin(0, 4)
                                SimpleText(it.likeCount.toString(), 12, Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}