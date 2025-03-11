package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.LineColor
import com.example.administrator.live.core.ui.theme.Red

@Composable
fun BackBottom(
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit = {},
    onClearClick: () -> Unit = {}
) {
    val isShowDialog = remember { mutableStateOf(false) }
    Box(
        modifier
            .padding(start = 20.dp, top = 14.dp)
            .clickable { isShowDialog.value = !isShowDialog.value }) {
        Column {
            Image(
                painterResource(R.drawable.back_white),
                null,
                Modifier.size(18.dp),
                contentScale = ContentScale.Crop
            )
            if (isShowDialog.value) {
                Margin(16)
                Column(
                    Modifier
                        .width(101.dp)
                        .background(Color.White, RoundedCornerShape(4.dp))
                ) {
                    Row(
                        Modifier
                            .padding(12.dp, 10.dp)
                            .clickable { onClearClick() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(R.drawable.icon_clear_camera),
                            null,
                            Modifier.size(16.dp)
                        )
                        Margin(width = 5)
                        SimpleText("清空内容", 14, Red)
                    }
                    HorizontalDivider(color = LineColor)
                    Row(
                        Modifier
                            .padding(12.dp, 10.dp)
                            .clickable { onSaveClick() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(R.drawable.icon_save_camera),
                            null,
                            Modifier.size(16.dp)
                        )
                        Margin(width = 5)
                        SimpleText("存草稿箱", 14, FontColor)
                    }
                }
            }
        }
    }
}