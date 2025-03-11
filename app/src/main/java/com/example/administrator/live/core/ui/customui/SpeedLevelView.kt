package com.example.administrator.live.core.ui.customui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.HalfTransparent3
import com.example.administrator.live.core.viewmodels.SpeedLevel

@Composable
fun SpeedLevelView(
    modifier: Modifier = Modifier,
    currentSpeed: SpeedLevel,
    onClick: (SpeedLevel) -> Unit
) {
    val speedLevels = remember { SpeedLevel.entries.toTypedArray() }
    val selectedIndex = speedLevels.indexOf(currentSpeed)
    val boxWidth = 61.dp
    val boxHeight = 44.dp
    val offset by animateDpAsState(
        targetValue = selectedIndex * (boxWidth),
        animationSpec = tween(durationMillis = 300), label = ""
    )
    Box(
        modifier = modifier
            .background(HalfTransparent3, RoundedCornerShape(22.dp))
    ) {
        Box(
            Modifier
                .offset { IntOffset(offset.roundToPx(), 0) }
                .size(boxWidth, boxHeight)
                .background(Color.White, RoundedCornerShape(22.dp))
        )
        LazyRow {
            itemsIndexed(speedLevels) { index, speedLevel ->
                val textColor by animateColorAsState(
                    targetValue = if (index == selectedIndex) FontColor else Color.White,
                    animationSpec = tween(durationMillis = 300), label = ""
                )
                Box(
                    Modifier
                        .size(boxWidth, boxHeight)
                        .clickable { onClick(speedLevel) },
                    contentAlignment = Alignment.Center
                ) {
                    SimpleText(speedLevel.title, color = textColor)
                }
            }
        }
    }
}