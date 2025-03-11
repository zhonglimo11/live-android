package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.administrator.live.R
import com.example.administrator.live.bean.User
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.ui.theme.PurpleGrey

@Composable
fun SearchView(
    searchInput: String,
    updateSearchInput: (String) -> Unit,
    hint: String = "搜索",
    height: Dp = 32.dp,
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp),
    color: Color = PurpleGrey,
    shape: Shape = RoundedCornerShape(16.dp),
    selectedFriends: List<User> = emptyList(),
) {
    Row(
        modifier = Modifier
            .clip(shape)
            .height(height)
            .background(color)
            .padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            modifier = Modifier.weight(1f).padding(end = 10.dp),
            value = searchInput,
            onValueChange = { updateSearchInput(it) },
            cursorBrush = SolidColor(Color.Red),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (selectedFriends.isEmpty()) {
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.search_gray),
                            contentDescription = null,
                        )
                    } else {
                        val listState = rememberLazyListState()
                        LaunchedEffect(selectedFriends.size) {
                            if (selectedFriends.isNotEmpty()) {
                                listState.scrollToItem(selectedFriends.size - 1)
                            }
                        }
                        LazyRow(
                            Modifier.widthIn(max = 200.dp),
                            listState,
                        ) {
                            itemsIndexed(selectedFriends) { index, user ->
                                CircularImage(user.avatar, size = 28.dp)
                                if (index < selectedFriends.size - 1) {
                                    Margin(0, 4)
                                }
                            }
                        }
                    }
                    Margin(0, 4)
                    Box{
                        if (searchInput.isEmpty()) {
                            Text(
                                text = hint,
                                color = FontColor2,
                                fontSize = 14.sp
                            )
                        }
                        innerTextField() // 显示输入框内容
                    }
                }
            }
        )
        if (searchInput.isNotEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.icon_delete),
                contentDescription = "delete",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { updateSearchInput("") })
        }
    }
}