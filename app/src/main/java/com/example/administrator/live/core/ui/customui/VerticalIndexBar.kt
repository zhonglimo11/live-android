package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.ui.theme.FontColor3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun VerticalIndexBar(
    positions: Map<String, Int>, // 每个字母组的开头位置
    listState: LazyListState,
    coroutineScope: CoroutineScope
) {
    Column(
        Modifier.padding(top = 84.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ('A'..'Z').forEach { letter ->
            val key = letter.toString()
            if (key == "A") {
                positions.forEach {
                    println("${it.key} ${it.value}")
                }
            }
            val isAvailable = positions.containsKey(key) // 检查该字母是否有对应的用户组
            Text(
                text = key,
                modifier = Modifier
                    .clickable(enabled = isAvailable) { // 只有有用户组的字母可以点击
                        if (isAvailable) {
                            val position = positions[key]
                            if (position != null) {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(position) // 滚动到该字母组的开头
                                }
                            }
                        }
                    }
                    .padding(vertical = 1.dp),
                fontSize = 12.sp,
                color = if (isAvailable) FontColor3 else FontColor2 // 有效字母与无效字母显示不同颜色
            )
        }
    }
}