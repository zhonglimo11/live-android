package com.example.administrator.live.core.ui.customui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.administrator.live.views.FullWindowVideoView
import timber.log.Timber

@Composable
fun VideoPreview(videoPath: String) {
    val videoViewRef = remember { mutableStateOf<FullWindowVideoView?>(null) }
    AndroidView(
        factory = { ctx ->
            FullWindowVideoView(ctx).apply {
                setVideoPath(videoPath)
                setOnErrorListener { _, what, extra ->
                    Timber.e("Error: $what, Extra: $extra")
                    return@setOnErrorListener true
                }
                videoViewRef.value = this
                start()
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                videoViewRef.value?.let { videoView ->
                    if (videoView.isPlaying) {
                        videoView.pause()
                    } else {
                        videoView.start()
                    }
                }
            }
    )
}