package com.example.administrator.live.core.ui.customui

import android.os.SystemClock
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.administrator.live.R
import kotlinx.coroutines.launch
import kotlin.random.Random

@Preview
@Composable
private fun LikeAnimationPreview() {
    LikeAnimation()
}

@Composable
fun LikeAnimation() {
    var showHeart by remember { mutableStateOf(false) }
    var heartPosition by remember { mutableStateOf(Offset.Zero) }
    var parentSize by remember { mutableStateOf(IntSize.Zero) }
    val coroutineScope = rememberCoroutineScope()
    val tapTimeStamps = remember { LongArray(3) }

    val rotation = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.9f) }
    val translationY = remember { Animatable(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates -> parentSize = coordinates.size }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { tapPosition ->
                        // Shift tap timestamps for triple tap detection
                        System.arraycopy(tapTimeStamps, 1, tapTimeStamps, 0, tapTimeStamps.size - 1)
                        tapTimeStamps[tapTimeStamps.size - 1] = SystemClock.uptimeMillis()

                        // Check if it's a triple tap within 500 milliseconds
                        if (tapTimeStamps[0] >= (SystemClock.uptimeMillis() - 500)) {
                            showHeart = true
                            heartPosition = tapPosition
                            coroutineScope.launch {
                                // Set initial states for the animations
                                alpha.snapTo(1f)
                                scale.snapTo(2f)
                                translationY.snapTo(0f)
                                rotation.snapTo(Random.nextFloat() * 70 - 35)


                                // Play animations sequentially
                                launch {
                                    scale.animateTo(
                                        0.9f,
                                        animationSpec = tween(100)
                                    ) // Shrink animation
                                    scale.animateTo(
                                        1f,
                                        animationSpec = tween(50, delayMillis = 150)
                                    )
                                    scale.animateTo(
                                        3f,
                                        animationSpec = tween(700, delayMillis = 400)
                                    )
                                }
                                launch {
                                    alpha.animateTo(
                                        0f,
                                        animationSpec = tween(300, delayMillis = 400)
                                    )
                                }
                                launch {
                                    translationY.animateTo(
                                        -600f,
                                        animationSpec = tween(800, delayMillis = 400)
                                    )
                                }
                                // Reset after animation
                                launch {
                                    alpha.animateTo(
                                        0f,
                                        animationSpec = tween(300, delayMillis = 700)
                                    )
                                }
                                showHeart = false
                            }
                        }
                    }
                )
            }
    ) {
        if (showHeart) {
            Image(
                painter = painterResource(id = R.mipmap.icon_home_like_after),
                contentDescription = null,
                modifier = Modifier
                    .offset(
                        with(LocalDensity.current) { heartPosition.x.toDp() - 150.dp },
                        with(LocalDensity.current) { heartPosition.y.toDp() - 300.dp }
                    )
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value,
                        alpha = alpha.value,
                        rotationZ = rotation.value,
                        translationY = translationY.value
                    )
            )
        }
    }
}