package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.administrator.live.R

@Composable
fun CircularImage(
    imgResId: Int? = null,
    size: Dp = 50.dp
) {
    if (imgResId != null) {
        Image(
            painter = painterResource(imgResId),
            contentDescription = null,
            modifier = Modifier
                .size(size)
                .clip(CircleShape),
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun CircularImage(
    imageUrl: String? = null,
    size: Dp = 50.dp
) {
    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(size)
                .clip(CircleShape),
            contentScale = ContentScale.FillBounds,
            placeholder = painterResource(id = R.drawable.header_empty)
        )
    }
}