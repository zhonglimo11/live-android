package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.FontColor4

@Composable
fun CameraModeItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val textColor = if (isSelected) FontColor else FontColor4
    val backgroundColor =
        if (isSelected) Modifier.background(Color.White, RoundedCornerShape(35.dp)) else Modifier
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(backgroundColor)
            .padding(10.dp, 2.dp)
    ) {
        SimpleText(label, 16, color = textColor)
    }
}