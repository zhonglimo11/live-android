package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ContinueButton(
    backColor: Color,
    roundedCorner: Int = 23,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier
            .background(backColor, RoundedCornerShape(roundedCorner.dp)),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}