package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.administrator.live.R
import com.example.administrator.live.bean.User
import com.example.administrator.live.core.ui.theme.FontColor3
import com.example.administrator.live.core.ui.theme.Red

@Composable
fun Avatar(
    user: User? = null,
    imgResId: Int? = null,
    name: String? = null,
    size: Dp = 50.dp,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        val nameStr = name ?: (user?.name ?: "Unknown")
        if (user != null) {
            CircularImage(imageUrl = user.avatar, size = size)
        }
        if (imgResId != null) {
            CircularImage(imgResId = imgResId, size = size)
        }
        Text(
            text = nameStr,
            maxLines = 1,
            fontSize = 10.sp,
            color = FontColor3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun AvatarWithNickname(avatar: String, nickname: String, marginHorizontal: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularImage(avatar)
        Margin(0, marginHorizontal)
        SimpleText(nickname)
    }
}

@Composable
fun AvatarWithIcon(user: User) {
    Box(
        Modifier.size(50.dp,58.dp)
    ) {
        AsyncImage(
            model = user.avatar,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )
        Box(
            Modifier
                .size(30.dp, 18.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Red)
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.mipmap.add_icon),
                null,
                Modifier.size(10.dp),
                colorFilter = ColorFilter.tint(Color.White),
            )
        }
    }
}