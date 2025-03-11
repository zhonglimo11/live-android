package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.administrator.live.bean.Image
import com.example.administrator.live.bean.modelValue

// 图片预览
@Composable
fun ImagePreview(imagePath: Image,modifier: Modifier = Modifier) {
    AsyncImage(
        imagePath.modelValue,
        null,
        modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}