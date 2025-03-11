package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.theme.LineColor

@Composable
fun FunctionMenu(
    name: String,
    nameColorId: Int = R.color.fontDefaultColor,
    switch: @Composable () -> Unit = {},
    imageSecond: @Composable () -> Unit = {},
    info: @Composable () -> Unit = {},
    infoSecond: @Composable () -> Unit = {},
    hasDivider: Boolean = true,
    hasDefImg: Boolean = true,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(12.dp, 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SimpleText(name, 16, colorResource(id = nameColorId))
            Spacer(modifier = Modifier.weight(1f))
            info()
            imageSecond()
            if (hasDefImg) {
                Margin(0, 6)
                SimpleImage(R.drawable.img_group_more, 12, 12)
            }
            switch()
        }
        Margin(4)
        infoSecond()
    }
    if (hasDivider) {
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = LineColor
        )
    }
}