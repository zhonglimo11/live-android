package com.example.administrator.live.core.ui.customui

import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.KeyEvent
import android.widget.EditText
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.administrator.live.R
import com.example.administrator.live.bean.MoreInputItem
import com.example.administrator.live.core.ui.theme.PurpleGrey
import com.example.administrator.live.core.ui.theme.Red
import com.example.administrator.live.utils.EmoJiUtils
import java.util.Locale

enum class InputMode {
    VOICE, TEXT, EMOJI, MORE, NONE
}

@Composable
fun ChatInputBox(
    modifier: Modifier = Modifier,
    onSendClick: (CharSequence) -> Unit = {},
    isSystem: Boolean = false
) {
    var message by remember { mutableStateOf<CharSequence>("") }
    val showSendButton = message.isNotEmpty()
    var inputMode by remember { mutableStateOf(InputMode.NONE) }
    var isKeyboardVisible by remember { mutableStateOf(false) }
    val voiceIcon =
        painterResource(if (inputMode == InputMode.VOICE) R.drawable.icon_key else R.drawable.icon_voice)
    val emojiIcon =
        painterResource(if (inputMode == InputMode.EMOJI) R.drawable.icon_key else R.drawable.icon_emoji)
    val keyboardController = LocalSoftwareKeyboardController.current
    var editTextReference: EditText? by remember { mutableStateOf(null) }
    val density = LocalDensity.current
    // 监听软键盘状态
    isKeyboardVisible = WindowInsets.ime.getBottom(density) > 0

    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(20.dp, 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isSystem) {
                Image(
                    voiceIcon,
                    contentDescription = "Voice",
                    Modifier
                        .size(24.dp)
                        .clickable {
                            inputMode =
                                if (inputMode == InputMode.VOICE) InputMode.NONE else InputMode.VOICE
                            keyboardController?.hide()  // 收起软键盘
                        }
                )
                Margin(0, 13)
            }
            if (inputMode == InputMode.VOICE) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, RoundedCornerShape(17.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(Modifier.padding(12.dp, 7.dp)) {
                        SimpleText("按住说话", 14)
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, RoundedCornerShape(17.dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(Modifier.padding(12.dp, 7.dp)) {
                        AndroidView(
                            factory = { context ->
                                EditText(context).apply {
                                    this.width = maxWidth
                                    editTextReference = this
                                    setText(message)
                                    hint = "发消息..."
                                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f) // 设置字体大小为14sp
                                    setTextColor(resources.getColor(R.color.fontDefaultColor))
                                    setHintTextColor(resources.getColor(R.color.fontColor_2))
                                    setBackgroundColor(resources.getColor(R.color.transparent))
                                    setPadding(0, 0, 0, 0)
                                    maxLines = 6
                                    isSingleLine = false
                                    isCursorVisible = true
                                    setOnClickListener {
                                        inputMode = InputMode.TEXT
                                    }
                                    addTextChangedListener(object : TextWatcher {
                                        override fun beforeTextChanged(
                                            s: CharSequence?, start: Int, count: Int, after: Int
                                        ) {
                                        }

                                        override fun onTextChanged(
                                            s: CharSequence?, start: Int, before: Int, count: Int
                                        ) {
                                        }

                                        override fun afterTextChanged(s: Editable?) {
                                            message = s.toString()
                                        }
                                    })
                                }
                            },
                            update = { editText ->
                                if (editText.text.toString() != message) {
                                    editText.setText(message)
                                    editText.setSelection(message.length)
                                }
                            }
                        )
                    }
                }
            }
            Margin(0, 12)
            Image(
                emojiIcon,
                contentDescription = "Emoji",
                Modifier
                    .size(24.dp)
                    .clickable {
                        keyboardController?.hide()
                        inputMode =
                            if (inputMode == InputMode.EMOJI) InputMode.NONE else InputMode.EMOJI
                    },
                contentScale = ContentScale.FillBounds
            )
            Margin(0, 12)
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterVertically)
            ) {
                if (showSendButton || isSystem) {
                    Box(
                        Modifier
                            .clickable {
                                onSendClick(message)
                                message = ""
                            }
                            .size(40.dp, 24.dp)
                            .background(Red, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        SimpleText("发送", 14, Color.White)
                    }
                } else {
                    Image(
                        painterResource(id = R.drawable.icon_more_black),
                        "More",
                        Modifier
                            .size(24.dp)
                            .clickable {
                                keyboardController?.hide()
                                inputMode =
                                    if (inputMode == InputMode.MORE) InputMode.NONE else InputMode.MORE
                            },
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
        if (!isKeyboardVisible) {
            if (inputMode == InputMode.MORE) {
                InputMore()
            }
            val context = LocalContext.current
            if (inputMode == InputMode.EMOJI) {
                InputEmoji(onEmojiClick = {
                    val stringBuilder = StringBuilder(message)
                    stringBuilder.append(it)
                    message = EmoJiUtils.parseEmoJi(context, stringBuilder.toString())
                }, onDeleteClick = {
                    val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)
                    editTextReference?.dispatchKeyEvent(event)
                })
            }
        }
    }
}

@Composable
fun InputMore(modifier: Modifier = Modifier) {
    val moreInputItems = listOf(
        MoreInputItem("相册", R.drawable.img_album),
        MoreInputItem("拍照", R.drawable.img_camera),
        MoreInputItem("视频通话", R.drawable.img_video_call),
        MoreInputItem("分享作品", R.drawable.img_share_work),
        MoreInputItem("分享群聊", R.drawable.img_share_group)
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .fillMaxWidth()
            .background(PurpleGrey)
            .padding(24.dp, 20.dp, 24.dp, 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(moreInputItems) { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SimpleImage(item.imageResourceId, 66, 66)
                Margin(8)
                SimpleText(item.title, 12)
            }
        }
    }
}

@Composable
fun InputEmoji(
    modifier: Modifier = Modifier,
    onEmojiClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    // 使用 remember 追踪最近使用的表情
    val recentEmojis = remember { mutableStateListOf<String>() }
    val allEmojis = remember {
        (0..59).map { index -> String.format(Locale.getDefault(), "[emoji_%02d]", index) }
    }

    fun updateRecentEmojis(emoji: String) {
        // 如果最近表情列表已经包含该表情，则移除旧的
        recentEmojis.remove(emoji)
        // 添加新表情到列表开头
        recentEmojis.add(0, emoji)
        // 限制最近表情的大小为3
        if (recentEmojis.size > 7) {
            recentEmojis.removeLast()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(PurpleGrey)
            .padding(16.dp),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 最近使用表情
            if (recentEmojis.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SimpleText("最近使用", 12)
                }
                items(recentEmojis) { item ->
                    EmoJiUtils.getEmoJiMap()[item]?.let {
                        SimpleImage(
                            it,
                            30,
                            30,
                            onClick = {
                                updateRecentEmojis(item)
                                onEmojiClick(item)
                            }
                        )
                    }
                }
            }

            // 所有表情
            item(span = { GridItemSpan(maxLineSpan) }) {
                SimpleText("所有表情", 12)
            }
            items(allEmojis) { item ->
                EmoJiUtils.getEmoJiMap()[item]?.let {
                    SimpleImage(
                        it,
                        30,
                        30,
                        onClick = {
                            updateRecentEmojis(item)
                            onEmojiClick(item)
                        }
                    )
                }
            }
        }

        // 删除按钮
        Box(Modifier.align(Alignment.BottomEnd)) {
            SimpleImage(R.drawable.img_delete, 40, 30, onClick = onDeleteClick)
        }
    }
}
