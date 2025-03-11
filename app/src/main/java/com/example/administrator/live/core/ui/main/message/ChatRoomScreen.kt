package com.example.administrator.live.core.ui.main.message

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.administrator.live.R
import com.example.administrator.live.bean.Message
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.ChatInputBox
import com.example.administrator.live.core.ui.customui.ImagePreview
import com.example.administrator.live.core.ui.customui.Margin
import com.example.administrator.live.core.ui.customui.SimpleImage
import com.example.administrator.live.core.ui.navhost.Screen
import com.example.administrator.live.core.ui.theme.PurpleGrey
import com.example.administrator.live.core.ui.theme.Red
import com.example.administrator.live.core.viewmodels.ChatViewModel
import com.example.administrator.live.holder.DateHeaderViewHolder
import com.example.administrator.live.holder.IncomingImageMessageViewHolder
import com.example.administrator.live.holder.IncomingTextMessageViewHolder
import com.example.administrator.live.holder.OutcomingImageMessageViewHolder
import com.example.administrator.live.holder.OutcomingTextMessageViewHolder
import com.example.administrator.live.utils.fixtures.MessagesFixtures.getTextMessage
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter

@Composable
fun ChatRoomScreen(navController: NavHostController, viewModel: ChatViewModel) {
    val sendId = "0"
    val holders = remember {
        MessageHolders().apply {
            setDateHeaderConfig(DateHeaderViewHolder::class.java, R.layout.item_date_header)
            setIncomingTextConfig(
                IncomingTextMessageViewHolder::class.java,
                R.layout.item_incoming_text_message
            )
            setOutcomingTextConfig(
                OutcomingTextMessageViewHolder::class.java,
                R.layout.item_outcoming_text_message
            )
            setIncomingImageConfig(
                IncomingImageMessageViewHolder::class.java,
                R.layout.item_incoming_image_message
            )
            setOutcomingImageConfig(
                OutcomingImageMessageViewHolder::class.java,
                R.layout.item_outcoming_image_message
            )
        }
    }
    val chat = viewModel.chat
    val isSystem = chat.id == "customerService"
    val adapter: MessagesListAdapter<Message> = remember {
        MessagesListAdapter<Message>(
            sendId,
            holders,
            viewModel.imageLoader
        ).apply {
            addToEnd(chat.getMessageList(), false)
        }
    }
    val onSendClick = { message: CharSequence ->
        adapter.addToStart(
            getTextMessage(message.toString()), true
        )
    }
    BaseScreen(
        title = chat.dialogName,
        navController = navController,
        topBarMenu = {
            if (!isSystem) {
                SimpleImage(R.drawable.menu_black, 20, 20) {
                    navController.navigate(Screen.ChatMenu.route)
                }
            }
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
        ) {
            if (isSystem) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color(0xFFF7ECF0),
                                    Color(0xFFF0F0F0)
                                )
                            )
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PurpleGrey),
                ) {
                    ImagePreview(viewModel.chatBg)
                }
            }
        }
        Column(
            Modifier.fillMaxSize()
        ) {
            if (isSystem) {
                Box(Modifier.padding(16.dp)) {
                    HelpSection()
                }
            }
            MessageList(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                adapter
            )
            ChatInputBox(onSendClick = onSendClick, isSystem = isSystem)
        }
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, adapter: MessagesListAdapter<Message>) {
    AndroidView(
        factory = {
            MessagesList(it).apply {
                setAdapter(adapter)
                isNestedScrollingEnabled = true
                this.viewTreeObserver.addOnGlobalLayoutListener {
                    setPadding(0, 1, 0, 0)
                }
            }
        },
        modifier
    )
}

@Composable
fun HelpSection(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFFBF8F9),
    textColor: Color = Color.Black,
    secondaryTextColor: Color = Color.Gray,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(126.dp)
            .clip(RoundedCornerShape(14.dp)), // 设置圆角
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_ai),
                contentDescription = "AI Image",
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
                    .padding(start = 24.dp)
            ) {
                Text(
                    text = "HI~ 有什么可以帮您！",
                    style = TextStyle(
                        color = textColor,
                        fontSize = 20.sp,
                    ),
                    modifier = Modifier.padding(top = 18.dp)
                )

                Text(
                    text = "有问题可以来这里哦~",
                    style = TextStyle(
                        color = secondaryTextColor,
                        fontSize = 13.sp
                    )
                )
                Margin(15)
                Text(
                    "拨打商家电话",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier
                        .background(
                            color = Red,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 11.dp, vertical = 5.dp)
                )
            }
        }
    }
}