package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.LineColor
import com.example.administrator.live.widgets.dialog.ShareDialog
import com.sun.resources.comment.CommendMsgDialog
import com.sun.resources.comment.CommendMsgDialog.OnListListener

@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    title: String,
    content: String? = null,
    cancel: String = "取消",
    confirm: String = "确定",
    onCancel: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) = Dialog(
    onDismissRequest = onDismissRequest
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = colorResource(id = R.color.white),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = FontColor,
                modifier = Modifier.padding(top = 28.dp)
            )
            if (content != null) {
                Text(
                    content,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = FontColor,
                    modifier = Modifier.padding(36.dp, 12.dp)
                )
                Margin(8)
            } else {
                Margin(28)
            }
            HorizontalDivider(color = LineColor)
            Row(
                Modifier.height(54.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { onCancel() },
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    SimpleText(cancel)
                }
                VerticalDivider(color = LineColor)
                TextButton(
                    onClick = { onConfirm() },
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    SimpleText(confirm)
                }
            }
        }
    }
}

@Composable
fun CommendMsgDialog(
    onListListener: OnListListener
) {
    val context = LocalContext.current
    // 使用 remember 来保持对话框实例
    val dialog = remember { CommendMsgDialog(context, onListListener) }

    // 控制对话框的显示
    LaunchedEffect(dialog) {
        dialog.show()
    }

    // 在 Composable 销毁时关闭对话框
    DisposableEffect(dialog) {
        onDispose {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
    }
}

@Composable
fun ComShareDialog() {
    val context = LocalContext.current
    // 使用 remember 来保持对话框实例
    val dialog = remember { ShareDialog(context) }

    // 控制对话框的显示
    LaunchedEffect(dialog) {
        dialog.show()
    }

    // 在 Composable 销毁时关闭对话框
    DisposableEffect(dialog) {
        onDispose {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
    }
}