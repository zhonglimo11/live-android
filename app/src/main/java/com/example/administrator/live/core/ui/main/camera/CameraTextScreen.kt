package com.example.administrator.live.core.ui.main.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.administrator.live.R
import com.example.administrator.live.bean.Image
import com.example.administrator.live.core.ui.customui.CameraIcons
import com.example.administrator.live.core.ui.customui.ContinueButton
import com.example.administrator.live.core.ui.customui.IconWithText
import com.example.administrator.live.core.ui.customui.ImagePreview
import com.example.administrator.live.core.ui.customui.Margin
import com.example.administrator.live.core.ui.customui.MusicSelect
import com.example.administrator.live.core.ui.customui.SimpleImage
import com.example.administrator.live.core.ui.customui.SimpleText
import com.example.administrator.live.core.ui.customui.TextInputBox
import com.example.administrator.live.core.ui.navhost.Screen
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.ui.theme.LineColor
import com.example.administrator.live.core.ui.theme.Red
import com.example.administrator.live.core.viewmodels.CameraViewModel

data class CameraBg(
    val name: String,
    val image: Int,
    val icon: Int,
)

@Composable
fun CameraTextScreen(navController: NavController, viewModel: CameraViewModel) {
    val siftCameraBgList = remember {
        listOf(
            CameraBg("雅黑", R.drawable.camera_bg_01, R.drawable.camera_bg_icon_01),
            CameraBg("灰砂", R.drawable.camera_bg_02, R.drawable.camera_bg_icon_02),
            CameraBg("烟霏灰", R.drawable.camera_bg_03, R.drawable.camera_bg_icon_03),
            CameraBg("夜色", R.drawable.camera_bg_04, R.drawable.camera_bg_icon_04),
            CameraBg("浅水蓝", R.drawable.camera_bg_05, R.drawable.camera_bg_icon_05),
            CameraBg("青春绿", R.drawable.camera_bg_06, R.drawable.camera_bg_icon_06),
            CameraBg("静密紫", R.drawable.camera_bg_07, R.drawable.camera_bg_icon_07),
            CameraBg("倾心粉", R.drawable.camera_bg_08, R.drawable.camera_bg_icon_08),
            CameraBg("梦境", R.drawable.camera_bg_09, R.drawable.camera_bg_icon_09),
            CameraBg("光影", R.drawable.camera_bg_10, R.drawable.camera_bg_icon_10),
            CameraBg("梦幻星空", R.drawable.camera_bg_11, R.drawable.camera_bg_icon_11),
            CameraBg("海浪", R.drawable.camera_bg_12, R.drawable.camera_bg_icon_12),
            CameraBg("极光", R.drawable.camera_bg_13, R.drawable.camera_bg_icon_13),
            CameraBg("云间物语", R.drawable.camera_bg_14, R.drawable.camera_bg_icon_14),
            CameraBg("都市", R.drawable.camera_bg_15, R.drawable.camera_bg_icon_15),
            CameraBg("晚秋", R.drawable.camera_bg_16, R.drawable.camera_bg_icon_16),
            CameraBg("日落", R.drawable.camera_bg_17, R.drawable.camera_bg_icon_17),
            CameraBg("休闲", R.drawable.camera_bg_18, R.drawable.camera_bg_icon_18),
            CameraBg("旅途", R.drawable.camera_bg_19, R.drawable.camera_bg_icon_19),
            CameraBg("意境", R.drawable.camera_bg_20, R.drawable.camera_bg_icon_20),
        )
    }
    var showMusicSelect by remember { mutableStateOf(false) }
    var showSiftList by remember { mutableStateOf(false) }
    val baseIcons = listOf(
        IconWithText(R.drawable.icon_setting, "更多设置", { /* 设置操作 */ })
    )
    val moreIcons = listOf(
        IconWithText(R.drawable.icon_upload_img, "上传背景", { navController.navigate(Screen.SelectBgPhoto(Screen.CameraText.route).route) }),
        IconWithText(R.drawable.camera_bg_icon_18, "精选背景", { showSiftList = !showSiftList })
    )
    val textFieldValueChange: (TextFieldValue) -> Unit = {}
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(Modifier.padding(bottom = 50.dp)) {
            ImagePreview(viewModel.imageFilePath)
        }
        Box(Modifier.align(Alignment.Center)) {
            TextInputBox(
                hint = "说点什么",
                hintStyle = TextStyle(
                    color = FontColor2,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp
                ),
                textStyle = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp
                ),
                color = Color.Transparent,
                showCountText = false
            ) { textFieldValueChange(it) }
        }
        Box(
            Modifier
                .padding(start = 20.dp, top = 14.dp)
                .clickable { navController.popBackStack() }) {
            Image(
                painterResource(R.drawable.back_white),
                null,
                Modifier.size(18.dp),
                contentScale = ContentScale.Crop
            )
        }
        MusicSelect(
            Modifier
                .align(Alignment.TopCenter)
                .clickable { showMusicSelect = !showMusicSelect })
        CameraIcons(Modifier.align(Alignment.TopEnd), baseIcons, moreIcons)
        if (showSiftList) {
            SiftCameraBgList(
                Modifier
                    .align(Alignment.BottomCenter)

                    .padding(bottom = 61.dp),
                siftCameraBgList
            ) { viewModel.imageFilePath = Image.IntImage(it) }
        }
        if (
            showMusicSelect
        ) {
            CameraMusic { showMusicSelect = !showMusicSelect }
        }
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 2.dp)
        ) {
            ContinueButton(
                LineColor,
                33,
                Modifier
                    .size(140.dp, 44.dp)
                    .clickable { navController.popBackStack()/*TODO*/ }) {
                Row {
                    SimpleImage(R.drawable.icon_save_camera, 20) {}
                    Margin(width = 4)
                    SimpleText("存草稿", 16, FontColor)
                }
            }
            Margin(width = 31)
            ContinueButton(
                Red,
                33,
                Modifier
                    .size(140.dp, 44.dp)
                    .clickable { navController.popBackStack()/*TODO*/ }) {
                Row {
                    SimpleImage(R.drawable.icon_save_camera, 20) {}
                    Margin(width = 4)
                    SimpleText("发作品", 16, Color.White)
                }
            }
        }
    }
}

@Composable
fun SiftCameraBgList(
    modifier: Modifier = Modifier,
    list: List<CameraBg>,
    onIconClick: (Int) -> Unit
) {
    Box(modifier) {
        LazyRow(
            Modifier.padding(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(list) { item ->
                Column(
                    Modifier.clickable { onIconClick(item.image) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SimpleImage(item.icon, 40) { onIconClick(item.image) }
                    Margin(4)
                    SimpleText(item.name, 12, Color.White)
                }
            }
        }
    }
}