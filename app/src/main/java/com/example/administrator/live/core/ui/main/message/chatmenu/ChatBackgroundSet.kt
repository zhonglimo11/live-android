package com.example.administrator.live.core.ui.main.message.chatmenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.administrator.live.R
import com.example.administrator.live.bean.Image
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.DefSurfaceColumn
import com.example.administrator.live.core.ui.customui.FunctionMenu
import com.example.administrator.live.core.ui.customui.Margin
import com.example.administrator.live.core.ui.customui.SimpleImage
import com.example.administrator.live.core.ui.customui.SimpleText
import com.example.administrator.live.core.ui.navhost.Screen
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.viewmodels.ChatViewModel

@Composable
fun ChatBackgroundSet(navController: NavController, viewModel: ChatViewModel) {
    val defBG = remember { listOf(
        R.drawable.image_empty,
        R.drawable.camera_bg_03,
        R.drawable.camera_bg_05,
        R.drawable.camera_bg_06,
        R.drawable.camera_bg_07,
        R.drawable.camera_bg_08,
        R.drawable.camera_bg_09,
        R.drawable.camera_bg_10,
        R.drawable.camera_bg_11,
        R.drawable.camera_bg_12,
        R.drawable.camera_bg_13,
        R.drawable.camera_bg_14,
        R.drawable.camera_bg_15,
        R.drawable.camera_bg_16,
        R.drawable.camera_bg_17,
        R.drawable.camera_bg_18,
        R.drawable.camera_bg_19,
        R.drawable.camera_bg_20,
    ) }
    BaseScreen(
        title = "设置聊天背景",
        navController = navController
    ) {
        Column(
            Modifier.padding(20.dp, 0.dp, 20.dp, 20.dp)
        ) {
            Margin(12)
            DefSurfaceColumn {
                FunctionMenu("从相册中选择",
                    hasDivider = false,
                    hasDefImg = false,
                    imageSecond = {
                        SimpleImage(R.drawable.icon_photo, 20, 20)
                    },
                    onClick = { navController.navigate(Screen.SelectBgPhoto(Screen.ChatBackgroundSet.route).route) })
            }
            Margin(20)
            SimpleText("精选背景", color = FontColor2)
            Margin(12)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),// 水平间隔
                verticalArrangement = Arrangement.spacedBy(5.dp)// 垂直间隔
            ) {
                itemsIndexed(defBG) { index, resId ->
                    Box(
                        Modifier.clickable {
                            viewModel.bgChecked = index
                            viewModel.chatBg = Image.IntImage(resId)
                        }
                    ) {
                        Image(
                            rememberAsyncImagePainter(resId), null,
                            Modifier
                                .fillMaxWidth()
                                .height(204.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                        if (index == 0) {
                            Text("默认", Modifier.align(Alignment.Center), FontColor2)
                        }
                        if (index == viewModel.bgChecked) {
                            Box(
                                Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 16.dp)
                            ) {
                                Image(
                                    painterResource(R.drawable.icon_checked), null,
                                    Modifier.size(24.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}