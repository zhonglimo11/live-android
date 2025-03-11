package com.example.administrator.live.core.ui.customui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.LineColor

@Composable
fun CustomTitleBar(
    title: String = "",
    hasTitle: Boolean = true,
    hasLine: Boolean = true,
    centerContent: @Composable () -> Unit = {},
    navController: NavController? = null,
    @DrawableRes customBackRes: Int = R.drawable.back_black,
    backGroundColor: Color = Color.Transparent,
    content: @Composable () -> Unit = {}
) {
    Column(
        Modifier
            .statusBarsPadding()
            .background(backGroundColor),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(id = customBackRes), null,
                    Modifier
                        .size(18.dp)
                        .clickable { navController?.popBackStack() }
                )
                centerContent()
            }
            if (hasTitle) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FontColor,
                )
            }
            Box(Modifier.align(Alignment.CenterEnd)) {
                content()
            }
        }
        if (hasLine) {
            HorizontalDivider(color = LineColor)
        }
    }
}