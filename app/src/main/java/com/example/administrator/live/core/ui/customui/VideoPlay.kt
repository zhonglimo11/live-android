package com.example.administrator.live.core.ui.customui

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.administrator.live.R
import com.example.administrator.live.bean.Music
import com.example.administrator.live.bean.Video
import com.example.administrator.live.utils.fixtures.DialogsFixtures.getOneVideo
import com.example.administrator.live.utils.fixtures.DialogsFixtures.getUser
import com.example.administrator.live.views.FullWindowVideoView
import com.sun.resources.comment.CommendMsgDialog
import timber.log.Timber

@Preview
@Composable
private fun VideoListScreenPreview() {
    FullWindowVideoPlayer(getOneVideo(), false)
}

@Composable
fun VideoListScreen(videoList: List<Video>) {
    var currentIndex by remember { mutableIntStateOf(0) }
    // 使用 VerticalPager 实现上下滑动切换
    val pagerState = rememberPagerState { videoList.size }
    // 监听滚动事件更新当前视频索引
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            currentIndex = page
        }
    }
    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { pageIndex ->
        FullWindowVideoPlayer(
            video = videoList[pageIndex],
            isPlaying = pageIndex == currentIndex // 只有当前页面播放视频
        )
    }
}

@Composable
fun FullWindowVideoPlayer(video: Video, isPlaying: Boolean) {
    val videoViewRef = remember { mutableStateOf<FullWindowVideoView?>(null) }
    var isPlayingState by remember { mutableStateOf(isPlaying) }
    var alpha by remember { mutableFloatStateOf(0f) }
    var showCommendMsgDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    val author = getUser()
    val videoInfo = "视频信息"
    val music = Music()
    val iconWithText = listOf(
        IconWithText(R.drawable.icon_like, "88.8万"),
        IconWithText(R.drawable.icon_comment, "78.8万", { showCommendMsgDialog = true }),
        IconWithText(R.drawable.icon_star, "68.8万", { showShareDialog = true }),
        IconWithText(R.drawable.icon_share, "58.8万"),
    )

    // 覆盖层放置图标和视频播放器
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                FullWindowVideoView(ctx).apply {
                    setVideoURI(Uri.parse(video.url.toString()))
                    if (isPlaying) {
                        start()
                    } else {
                        pause()
                    }
                    videoViewRef.value = this
                    setOnErrorListener { _, what, extra ->
                        Timber.e("Error: $what, Extra: $extra")
                        return@setOnErrorListener true
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    // 单击暂停或播放视频
                    videoViewRef.value?.let { videoView ->
                        if (videoView.isPlaying) {
                            videoView.pause()
                            isPlayingState = false
                        } else {
                            videoView.start()
                            isPlayingState = true
                        }
                    }
                }
        )

        // 底部的文字信息
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = author.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Margin(8)
            Text(
                text = videoInfo,
                color = Color.White,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        // 右侧的头像、点赞、评论等图标
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 头像
            AvatarWithIcon(author)
            iconWithText.forEach {
                IconWithText(it)
            }
            //歌曲
            MusicWithImage(music.cover)
        }
        // 显示分享对话框
        if (showShareDialog) {
            ComShareDialog()
        }

        // 显示评论对话框
        if (showCommendMsgDialog) {
            CommendMsgDialog(onListListener = object : CommendMsgDialog.OnListListener {
                override fun getNextPage() {}
                override fun retryFirstPage() {}
                override fun getSecondList(id: Int, page: Int, parentPosition: Int) {}
                override fun comment(
                    content: String?,
                    id: Int,
                    beReplyId: String?,
                    beReplyNickName: String?,
                    beReplyAvatar: String?,
                    parentPosition: Int
                ) {
                }

                override fun giveLikes(
                    type: Int,
                    parentPosition: Int,
                    position: Int,
                    id: Int,
                    upOrDown: String?
                ) {
                }

                override fun delComment(type: Int, position: Int, parentPosition: Int, id: Int) {}
            })
        }

        AnimatedVisibility(
            visible = !isPlayingState,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.mipmap.ic_play),
                    contentDescription = "Play Icon",
                    modifier = Modifier
                        .size(60.dp)
                        .alpha(alpha) // 应用透明度动画
                )
            }
        }
    }

    // 更新播放状态
    LaunchedEffect(isPlaying) {
        isPlayingState = isPlaying
        alpha = if (!isPlayingState) 1f else 0f
        videoViewRef.value?.let { videoView ->
            if (isPlaying) {
                videoView.start()
            } else {
                videoView.pause()
            }
        }
    }
}

@Composable
fun MusicWithImage(image: String) {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Image(painterResource(R.drawable.bg_record), null, Modifier.size(46.dp))
        CircularImage(image, 30.dp)
    }
}
