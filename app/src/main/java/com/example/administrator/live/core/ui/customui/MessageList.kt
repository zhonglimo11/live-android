package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.administrator.live.R
import com.example.administrator.live.bean.Message
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.utils.AppUtils
import com.stfalcon.chatkit.utils.DateFormatter


@Composable
fun MessageList(
    messageList: List<Message>,
    searchInput: String = "",
    isOwnerSpeak: Boolean = false
) {
    LazyColumn(
        Modifier.fillMaxSize(),
    ) {
        itemsIndexed(messageList) { index, message ->
            MessageItem(message, searchInput, isOwnerSpeak)
            if (index != messageList.size - 1) Margin(20)
        }
    }
}

@Composable
fun MessageItem(message: Message, searchInput: String = "", isOwnerSpeak: Boolean = false) {
    var nameColor = FontColor
    var nameSize = 16
    var textColor = FontColor2
    var textSize = 14
    if (isOwnerSpeak) {
        nameColor = FontColor2
        nameSize = 14
        textColor = FontColor
        textSize = 16
    }
    Row {
        CircularImage(message.user?.avatar, size = 60.dp)
        Margin(0, 12)
        Column(
            Modifier
                .weight(1f)
                .padding(end = 10.dp)
        ) {
            Margin(7)
            SimpleText(message.user?.name ?: "", color = nameColor, size = nameSize)
            Margin(4)
            if (message.text != "") {
                Text(
                    text = AppUtils.highlightText(message.text, searchInput),
                    color = textColor,
                    fontSize = textSize.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (message.imageUrl != null && isOwnerSpeak) {
                AsyncImage(
                    message.imageUrl, "",
                    Modifier
                        .heightIn(max = 104.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .widthIn(max = 80.dp),
                    placeholder = painterResource(id = R.drawable.image_empty)
                )
            }
        }
        val timeStr = AppUtils.formatDate(message.createdAt)
        Text(
            timeStr, Modifier.padding(top = 8.dp), color = FontColor2, fontSize = 12.sp
        )
    }
}

@Composable
fun ImageMessageList(imgMessageList: List<Message>) {
    // 用于跟踪已显示的日期
    val shownDates = remember { mutableSetOf<String>() }
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(3.dp),// 水平间隔
        verticalArrangement = Arrangement.spacedBy(3.dp)// 垂直间隔
    ) {
        items(imgMessageList) { message ->
            val imgUrl = message.imageUrl
            val timeStr = DateFormatter.format(message.createdAt, "MM月dd日")
            // 判断当前日期是否已经显示
            val isDateShown = shownDates.contains(timeStr)
            if (!isDateShown) {
                shownDates.add(timeStr)  // 添加到已显示的日期集合
            }
            CustomImageBox (imgUrl?: ""){
                // 仅在日期未显示时才显示日期文本
                if (!isDateShown) {
                    Text(timeStr, Modifier.padding(8.dp), Color.White, 14.sp)
                }
            }
        }
    }
}