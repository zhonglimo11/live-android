package com.example.administrator.live.core.ui.customui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.administrator.live.core.ui.theme.HalfTransparent2
import com.example.administrator.live.core.ui.theme.Red
import com.example.administrator.live.utils.AppUtils
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Preview(showBackground = true, backgroundColor = 0x00000000)
@Composable
private fun P() {
    var isRecording by remember { mutableStateOf(false) }
    val pauseMarks = remember { mutableListOf<Float>() }
    var isPause by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    val onToggleRecording: () -> Unit = {
        if (!isRecording) {
            isRecording = true
        } else {
            isPause = !isPause
            if (isPause) {
                pauseMarks.add(progress)
            }
        }
    }
    LaunchedEffect(isRecording, isPause) {
        if (isRecording) {
            while (isRecording && !isPause && progress < 1f) {
                delay(100L)
                progress += 0.1f / 60
                if (progress > 1f) {
                    isRecording = false
                    progress = 0f
                    pauseMarks.clear()
                }
            }
        }
    }
    CustomRecordButton(
        Modifier,
        isRecording,
        progress,
        isPause,
        pauseMarks,
        onToggleRecording
    )
}

@Composable
fun CustomRecordButton(
    modifier: Modifier = Modifier,
    isRecording: Boolean,
    progress: Float,
    isPause: Boolean,
    pauseMarks: List<Float>,
    onToggleRecording: () -> Unit,
) {
    val halfBgSize by animateDpAsState(if (!isRecording) 0.dp else 136.dp, label = "")
    val redBorderSize by animateDpAsState(if (!isRecording) 0.dp else 141.dp, label = "")
    val whiteSize by animateDpAsState(if (!isRecording) 81.dp else 50.dp, label = "")
    val whiteBorderSize by animateDpAsState(if (!isRecording) 4.dp else 30.dp, label = "")
    val innerRedSize by animateDpAsState(if (!isRecording) 65.dp else 20.dp, label = "")
    val innerRedShape by animateDpAsState(if (!isRecording) 65.dp else 6.dp, label = "")
    val sweepAngle by animateFloatAsState(if (isRecording) progress * 360 else 0f, label = "")

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (isRecording) {
            SimpleText(AppUtils.formatDuration((progress * 60).toInt()), color = Color.White)
            Margin(5)
        }
        Box(
            Modifier.clickable { onToggleRecording() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .size(halfBgSize)
                    .background(HalfTransparent2, shape = CircleShape)
            )
            Box(Modifier.size(redBorderSize)) {
                val strokeWidth = with(LocalDensity.current) { 10.dp.toPx() }
                Canvas(modifier = Modifier.size(redBorderSize)) {
                    val diameter = size.minDimension - strokeWidth
                    // 绘制进度环
                    drawArc(
                        color = Red,
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                        size = Size(diameter, diameter),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    // 绘制暂停时刻的刻度
                    pauseMarks.forEach { pauseMark ->
                        val pauseAngle = pauseMark * 360f - 90f // 转换为对应角度
                        val radius = diameter / 2 // 半径是进度环的一半
                        val x =
                            (radius * cos(Math.toRadians(pauseAngle.toDouble()))).toFloat() + diameter / 2 + strokeWidth / 2
                        val y =
                            (radius * sin(Math.toRadians(pauseAngle.toDouble()))).toFloat() + diameter / 2 + strokeWidth / 2
                        drawCircle(
                            color = Color.White,
                            radius = strokeWidth / 2, // 小圆的半径
                            center = Offset(x, y)
                        )
                    }
                }
            }
            Box(
                Modifier
                    .size(whiteSize)
                    .border(whiteBorderSize, Color.White, shape = CircleShape)
            )
            if (isPause) {
                PauseIcon()
            } else {
                Box(
                    Modifier
                        .size(innerRedSize)
                        .background(Red, RoundedCornerShape(innerRedShape))
                )
            }
        }
    }
}

@Composable
fun PauseIcon(modifier: Modifier = Modifier, height: Int = 20) {
    val width = 0.3 * height
    val padding = 0.4 * height
    Row(
        modifier = modifier.height(height.dp),
    ) {
        VerticalDivider(Modifier.clip(RoundedCornerShape(width.dp)), width.dp, Red)
        Margin(width = padding.toInt())
        VerticalDivider(Modifier.clip(RoundedCornerShape(width.dp)), width.dp, Red)
    }
}