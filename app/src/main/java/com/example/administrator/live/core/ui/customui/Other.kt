package com.example.administrator.live.core.ui.customui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.FontColor2

@Composable
fun DefSurfaceColumn(
    content: @Composable () -> Unit
) {
    Surface(shape = MaterialTheme.shapes.small, color = colorResource(id = R.color.white)) {
        Column {
            content()
        }
    }
}

@Composable
fun Margin(
    height: Int = 0,
    width: Int = 0
) {
    if (height > 0) {
        Spacer(modifier = Modifier.height(height.dp))
    } else {
        Spacer(modifier = Modifier.width(width.dp))
    }
}

@Composable
fun SimpleImage(@DrawableRes imageResId: Int, width: Int, height: Int = width, onClick: () -> Unit = {}) {
    val modifier = if (onClick != {}) {
        Modifier.size(width.dp, height.dp).clickable { onClick() }
    } else {
        Modifier.size(width.dp, height.dp)
    }
    Image(
        painter = painterResource(imageResId),
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
fun SimpleText(text: String, size: Int = 16, color: Color = FontColor) {
    Text(text = text, fontSize = size.sp, color = color)
}

@Composable
fun TextBox(text: String, wight: Int, textSize: Int = 12, color: Color = FontColor) {
    Text(text, Modifier.width(wight.dp), color, textSize.sp)
}

@Composable
fun SimpleImageWithTextBox(
    @DrawableRes imgResId: Int,
    text: String,
    onClick: () -> Unit = {}
) {
    Column(Modifier.clickable { onClick() }, horizontalAlignment = Alignment.CenterHorizontally) {
        SimpleImage(imgResId, 24, 24) { onClick() }
        Margin(8)
        SimpleText(text, color = FontColor2)
    }
}