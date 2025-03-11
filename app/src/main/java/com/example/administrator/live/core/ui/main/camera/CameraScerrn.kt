package com.example.administrator.live.core.ui.main.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.administrator.live.R
import com.example.administrator.live.bean.Image
import com.example.administrator.live.core.ui.customui.BottomDialogView
import com.example.administrator.live.core.ui.customui.BottomNavigationMenu
import com.example.administrator.live.core.ui.customui.ContinueButton
import com.example.administrator.live.core.ui.customui.Margin
import com.example.administrator.live.core.ui.customui.SimpleIconWithText
import com.example.administrator.live.core.ui.customui.SimpleText
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.LineColor
import com.example.administrator.live.core.ui.theme.Red
import com.example.administrator.live.core.viewmodels.BottomDialog
import com.example.administrator.live.core.viewmodels.CameraViewModel
import com.example.administrator.live.core.viewmodels.RecordingState


@Composable
fun CameraScreen(navController: NavController, viewModel: CameraViewModel) {
    val tabs = listOf("图文", "多段拍", "随手拍", "模板", "直播")

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp)
        ) {
            when (viewModel.selectedIndex) {
                0 -> ImageTextShooting()
                1, 2 -> CameraView(navController, viewModel)
                3 -> TemplateShooting()
                4 -> LiveShooting()
            }
        }
        if (viewModel.recordingState == RecordingState.FINISHED) {
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 2.dp)
            ) {
                ContinueButton(LineColor, 33, Modifier.size(140.dp, 44.dp)) {
                    SimpleText("日常", 16, FontColor)
                }
                Margin(width = 31)
                ContinueButton(Red, 33, Modifier.size(140.dp, 44.dp)) {
                    SimpleText("下一步", 16, Color.White)
                }
            }
        }
        when (viewModel.bottomDialog) {
            BottomDialog.FILTER -> CameraFilter(viewModel)
            BottomDialog.EFFECT -> CameraEffect(viewModel)
            BottomDialog.BEAUTY -> CameraBeauty(viewModel)
            BottomDialog.MUSIC -> CameraMusic { viewModel.backDefaultState() }
            BottomDialog.MENU -> BottomNavigationMenu(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(Alignment.BottomStart), tabs, viewModel.selectedIndex
            ) { viewModel.selectedIndex = it }

            else -> {}
        }
    }
}

@Composable
fun CameraFilter(viewModel: CameraViewModel) {
    //使用view Model传入的列表 TODO
    val tabs =
        listOf("所有", "精选", "人像", "日常", "复古", "美食", "风景", "示例", "示例", "示例")
    val items = listOf(
        SimpleIconWithText(),
        SimpleIconWithText(
            text = "滤镜1",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "滤镜2",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "滤镜3",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "滤镜4",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "滤镜5",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "滤镜6",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "滤镜7",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "滤镜8",
            icon = Image.IntImage(R.drawable.example),
        ),
    )
    val selectedIndex = remember { mutableIntStateOf(0) }
    val selectedItemIndex = remember { mutableIntStateOf(0) }
    BottomDialogView(
        selectedIndex.intValue,
        selectedItemIndex.intValue,
        tabs, items, { viewModel.backDefaultState() },
        { selectedIndex.intValue = it }, { selectedItemIndex.intValue = it }
    )
}

@Composable
fun CameraEffect(viewModel: CameraViewModel) {
    //使用view Model传入的列表 TODO
    val tabs =
        listOf("特效")
    val items = listOf(
        SimpleIconWithText(),
        SimpleIconWithText(
            text = "特效1",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "特效2",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "特效3",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "特效4",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "特效5",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "特效6",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "特效7",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "特效8",
            icon = Image.IntImage(R.drawable.example),
        ),
    )
    val selectedIndex = remember { mutableIntStateOf(0) }
    val selectedItemIndex = remember { mutableIntStateOf(0) }
    BottomDialogView(
        selectedIndex.intValue,
        selectedItemIndex.intValue,
        tabs, items, { viewModel.backDefaultState() },
        { selectedIndex.intValue = it }, { selectedItemIndex.intValue = it }
    )
}

@Composable
fun CameraBeauty(viewModel: CameraViewModel) {
    //使用view Model传入的列表 TODO
    val tabs =
        listOf("美颜", "美妆", "美体")
    val items = listOf(
        SimpleIconWithText(),
        SimpleIconWithText(
            text = "磨皮",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "瘦脸",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "大眼",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "美白",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "一键瘦身",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "长腿",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "瘦腰",
            icon = Image.IntImage(R.drawable.example),
        ),
        SimpleIconWithText(
            text = "丰胸",
            icon = Image.IntImage(R.drawable.example),
        ),
    )
    val selectedIndex = remember { mutableIntStateOf(0) }
    val selectedItemIndex = remember { mutableIntStateOf(0) }
    BottomDialogView(
        selectedIndex.intValue,
        selectedItemIndex.intValue,
        tabs, items, { viewModel.backDefaultState() },
        { selectedIndex.intValue = it }, { selectedItemIndex.intValue = it }, 41, 100
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraMusic(onDismissRequest: () -> Unit) {
    ModalBottomSheet(
        dragHandle = {},
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        scrimColor = Color.Transparent,
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            color = Color.White,
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(459.dp),
                contentAlignment = Alignment.Center
            ) {
                SimpleText("音乐抽屉")
            }
        }
    }
}

@Composable
fun ImageTextShooting(modifier: Modifier = Modifier) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.Center
    ) {
        Text("图文", color = Color.White)
    }
}

@Composable
fun TemplateShooting(modifier: Modifier = Modifier) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.Center
    ) {
        Text("模板", color = Color.White)
    }
}

@Composable
fun LiveShooting(modifier: Modifier = Modifier) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.Center
    ) {
        Text("直播", color = Color.White)
    }
}