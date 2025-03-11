package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.theme.ButtonColors
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.ui.theme.Gray
import com.example.administrator.live.core.ui.theme.PurpleGrey

@Composable
fun QRCodeDialog(
    qrCode: String,
    groupName: String,
    date: String,
    onCloseClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDismissRequest: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = colorResource(id = R.color.white),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onCloseClick, modifier = Modifier.size(24.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_close),
                            null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Margin(40)
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .background(Gray)
                ) {
                    AsyncImage(
                        model = qrCode,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                    )
                }
                Margin(24)
                SimpleText("群聊：${groupName}", 18)
                Margin(8)
                SimpleText("该逛逛码将于${date}到期", 14, FontColor2)
                Margin(20)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    color = PurpleGrey,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp, 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        SimpleImage(R.drawable.img_save, 42, 40)
                        Margin(0, 4)
                        TextBox("保存图片到相册", 48)
                        Margin(0, 15)
                        SimpleImage(R.drawable.icon_arrow_right, 24, 24)
                        Margin(0, 16)
                        SimpleImage(R.drawable.icon_phone_hand, 42, 40)
                        Margin(0, 4)
                        TextBox("打开逛逛吧扫码查看", 60)
                    }
                }
                Margin(13)
                TextButton(
                    onClick = onSaveClick,
                    colors = ButtonColors,
                    modifier = Modifier.size(287.dp, 44.dp),
                    contentPadding = PaddingValues()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_save_img),
                        null,
                        modifier = Modifier.size(20.dp)
                    )
                    Margin(0, 2)
                    SimpleText("保存并分享", 16, Color.White)
                }
            }
        }
    }
}