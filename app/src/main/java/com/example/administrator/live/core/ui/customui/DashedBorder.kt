package com.example.administrator.live.core.ui.customui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.administrator.live.core.ui.theme.FontColor2

/**
 * dashedBorder
 *
 * @param color 边框颜色
 * @param strokeWidth 边框宽度
 * @param dashLength 虚线长度
 * @param gapLength 虚线间隔
 * @param cornerRadius 圆角半径
 */
fun Modifier.dashedBorder(
    color: Color = FontColor2,
    strokeWidth: Dp = 1.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
    cornerRadius: Dp = 8.dp
): Modifier = this.then(
    Modifier.drawBehind {
        // 使用 dp 到像素的转换
        val strokePx = strokeWidth.toPx()
        val dashPx = dashLength.toPx()
        val gapPx = gapLength.toPx()
        val cornerRadiusPx = cornerRadius.toPx()

        // 设置路径效果为虚线
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashPx, gapPx), 0f)

        // 绘制带圆角的虚线边框
        drawRoundRect(
            color = color,
            topLeft = Offset(0f, 0f),
            size = size,
            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
            style = Stroke(
                width = strokePx,
                pathEffect = pathEffect
            )
        )
    }.zIndex(1f)
)