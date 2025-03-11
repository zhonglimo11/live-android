package com.example.administrator.live.core.ui.customui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.theme.LineColor

@Composable
fun CameraIcons(modifier: Modifier = Modifier, baseIcons:List<IconWithText>, moreIcons:List<IconWithText> = listOf()) {
    // 使用预设项布局
    val items = getIconWithTextList(baseIcons, 28, 28, 4, 14)
    val moreItems = getIconWithTextList(moreIcons, 28, 28, 4, 14)
    var isMoreExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    // 更多按钮的旋转动画
    val rotationAngle by animateFloatAsState(
        targetValue = if (isMoreExpanded) 180f else 0f,
        label = ""
    )
    // 展开或收起时的滚动效果
    LaunchedEffect(isMoreExpanded) {
        scrollState.animateScrollTo(if (isMoreExpanded) scrollState.maxValue else 0)
    }
    // 布局
    Column(
        modifier.padding(top = 20.dp, end = 23.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEach { IconWithText(it) }
        if (moreIcons.isNotEmpty()) {
            HorizontalDivider(color = LineColor, modifier = Modifier.width(28.dp))
        }
        Column(
            Modifier
                .height(204.dp)
                .verticalScroll(scrollState, false),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            moreItems.forEach { IconWithText(it) }
        }
        if(baseIcons.size+moreIcons.size > 5){
            // 展开/收起按钮
            Image(
                painterResource(R.drawable.icon_up),
                null,
                Modifier
                    .size(36.dp)
                    .rotate(rotationAngle)
                    .clickable { isMoreExpanded = !isMoreExpanded },
                contentScale = ContentScale.FillBounds
            )
        }
    }
}