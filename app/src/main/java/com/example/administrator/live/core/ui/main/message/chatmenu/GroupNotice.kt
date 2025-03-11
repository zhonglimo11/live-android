package com.example.administrator.live.core.ui.main.message.chatmenu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.Margin
import com.example.administrator.live.core.ui.customui.SimpleText
import com.example.administrator.live.core.ui.theme.ButtonColors3
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.ui.theme.PurpleGrey
import com.example.administrator.live.core.viewmodels.ChatViewModel
import com.example.administrator.live.core.viewmodels.PermissionType

@Composable
fun GroupNotice(navController: NavController, viewModel: ChatViewModel) {
    with(viewModel) {
        val canExit = userPermissions != PermissionType.MEMBER
        val text = if (canExit) ",立即填写" else ""
        BaseScreen(
            navController = navController,
            backgroundColor = Color.White,
            title = "群公告"
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(20.dp,20.dp,20.dp,0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PurpleGrey
                    ) {
                        BasicTextField(
                            readOnly = !canExit,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .heightIn(min = 140.dp)
                                .verticalScroll(rememberScrollState()),
                            textStyle = TextStyle(fontSize = 16.sp, color = FontColor),
                            value = groupInfo,
                            onValueChange = { updateGroupInfo(it) },
                            cursorBrush = SolidColor(Color.Red),
                            decorationBox = { innerTextField ->
                                Box(
                                    contentAlignment = Alignment.TopStart
                                ) {
                                    if (groupInfo.isEmpty()) {
                                        Text(
                                            text = "暂无群公告${text}",
                                            color = FontColor2,
                                            fontSize = 16.sp
                                        )
                                    }
                                    innerTextField() // 显示输入框内容
                                }
                            }
                        )
                    }
                }
                if (canExit) {
                    Margin(24)
                    TextButton(
                        enabled = groupInfo.isNotEmpty(),
                        onClick = { onSubmitGroupInfoClicked() },
                        colors = ButtonColors3,
                        modifier = Modifier.size(311.dp, 46.dp),
                        contentPadding = PaddingValues()
                    ) {
                        SimpleText("去发布", 16, Color.White)
                    }
                }
            }
        }
    }
}