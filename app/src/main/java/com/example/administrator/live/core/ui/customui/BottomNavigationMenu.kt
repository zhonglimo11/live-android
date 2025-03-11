package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.administrator.live.core.ui.theme.FontColor2

@Composable
fun BottomNavigationMenu(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    selectedIndex: Int,
    onClick: (Int) -> Unit
) {
    Row(
        modifier
    ) {
        tabs.forEachIndexed { index, title ->
            Box(
                Modifier
                    .fillMaxHeight()
                    .clickable {
                        onClick(index)
                    }
                    .weight(1f), contentAlignment = Alignment.Center) {
                val color = if (index == selectedIndex) Color.White else FontColor2
                Text(
                    title,
                    color = color,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}