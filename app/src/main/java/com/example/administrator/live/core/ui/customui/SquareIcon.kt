package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.administrator.live.R
import com.example.administrator.live.bean.Image
import com.example.administrator.live.bean.modelValue

@Composable
fun SquareIcon(modifier: Modifier, text: String, icon: Image, size: Int = 52, margin: Int = 4) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            icon.modelValue, null,
            Modifier
                .size(size.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.image_empty),
        )
        Margin(margin)
        SimpleText(text, 14, Color.White)
    }
}