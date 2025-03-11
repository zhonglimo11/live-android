package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.administrator.live.R

@Composable
fun CloseBottom(modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier
            .padding(start = 20.dp, top = 13.dp)
            .clickable { navController.popBackStack() }) {
        Image(
            painterResource(R.drawable.icon_white_close),
            null,
            Modifier.size(20.dp),
            contentScale = ContentScale.FillBounds
        )
    }
}