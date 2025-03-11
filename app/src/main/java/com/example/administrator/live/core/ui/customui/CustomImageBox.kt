package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.administrator.live.core.ui.theme.PurpleGrey

@Composable
fun CustomImageBox(
    imageUrl: String, content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .background(PurpleGrey)
            .aspectRatio(3f / 4f)  // 设置宽高比为3:4
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),  // 填充整个Box
            contentScale = ContentScale.FillBounds,
        )
        content()
    }
}