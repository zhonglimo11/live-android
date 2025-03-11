package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.theme.HalfTransparent2

@Composable
fun MusicSelect(modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(120.dp, 42.dp)
            .padding(top = 2.dp)
            .background(HalfTransparent2, RoundedCornerShape(21.dp))
    ) {
        Row(
            Modifier.padding(16.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SimpleImage(R.drawable.icon_music, 20, 20)
            Margin(width = 4)
            Text(
                "选择音乐",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.offset(y = (-2).dp)
            )
        }
    }
}