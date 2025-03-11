package com.example.administrator.live.core.ui.main.camera

import androidx.activity.compose.BackHandler
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.administrator.live.R
import com.example.administrator.live.bean.Image
import com.example.administrator.live.core.ui.customui.BackBottom
import com.example.administrator.live.core.ui.customui.CameraIcons
import com.example.administrator.live.core.ui.customui.CameraModeItem
import com.example.administrator.live.core.ui.customui.CameraPreview
import com.example.administrator.live.core.ui.customui.CloseBottom
import com.example.administrator.live.core.ui.customui.CustomRecordButton
import com.example.administrator.live.core.ui.customui.IconWithText
import com.example.administrator.live.core.ui.customui.ImagePreview
import com.example.administrator.live.core.ui.customui.Margin
import com.example.administrator.live.core.ui.customui.MusicSelect
import com.example.administrator.live.core.ui.customui.SpeedLevelView
import com.example.administrator.live.core.ui.customui.SquareIcon
import com.example.administrator.live.core.ui.customui.VideoPreview
import com.example.administrator.live.core.ui.navhost.Screen
import com.example.administrator.live.core.viewmodels.BottomDialog
import com.example.administrator.live.core.viewmodels.CameraMode
import com.example.administrator.live.core.viewmodels.CameraViewModel
import com.example.administrator.live.core.viewmodels.RecordingState

@Composable
fun CameraView(
    navController: NavController,
    viewModel: CameraViewModel,
    modifier: Modifier = Modifier
) {
    val recordingState = viewModel.recordingState
    val elapsedTime = viewModel.elapsedTime
    val progress = elapsedTime.toFloat() / 600 // 计算进度
    val isRecording = recordingState == RecordingState.RECORDING
    val imageFilePath = viewModel.imageFilePath
    val videoFilePath = viewModel.videoFilePath
    val cameraMode = viewModel.cameraMode
    val lensFacing = viewModel.lensFacing
    var takePicture by remember { mutableStateOf(false) }
    // 使用状态切换图标资源
    val flashIcon = when (viewModel.imageCaptureFlashMode) {
        ImageCapture.FLASH_MODE_OFF -> R.drawable.icon_flash_off
        ImageCapture.FLASH_MODE_ON -> R.drawable.icon_flash_on
        ImageCapture.FLASH_MODE_AUTO -> R.drawable.icon_flash_auto
        else -> R.drawable.icon_flash_off
    }
    val speedIcon = if (viewModel.isSpeedOn) R.drawable.icon_speed_on else R.drawable.icon_speed_off
    val mirrorIcon =
        if (viewModel.isMirror) R.drawable.icon_mirror_on else R.drawable.icon_mirror_off

    // 根据录制状态设置主图标和更多图标
    val (baseIcons, moreIcons) = if (viewModel.recordingState == RecordingState.FINISHED) {
        listOf(
            IconWithText(R.drawable.icon_share, "分享", { /* 分享操作 */ })
        ) to listOf(
            IconWithText(R.drawable.icon_edit_camera, "剪辑", { /* 剪辑操作 */ }),
            IconWithText(R.drawable.icon_text, "文字", { /* 文字操作 */ }),
            IconWithText(R.drawable.icon_effect, "特效", { viewModel.toggleEffect() }),
            IconWithText(R.drawable.icon_filter, "滤镜", { viewModel.toggleFilter() }),
            IconWithText(R.drawable.icon_prune, "剪裁", { /* 剪裁操作 */ }),
            IconWithText(R.drawable.icon_caption, "自动字幕", { /* 自动字幕操作 */ })
        )
    } else {
        listOf(
            IconWithText(R.drawable.icon_transfer, "翻转", { viewModel.toggleCamera() }),
            IconWithText(flashIcon, "闪光灯", { viewModel.toggleFlash() }),
            IconWithText(R.drawable.icon_setting, "设置", { /* 设置操作 */ })
        ) to listOf(
            IconWithText(R.drawable.icon_beauty, "美颜", { viewModel.toggleBeauty() }),
            IconWithText(R.drawable.icon_filter, "滤镜", { viewModel.toggleFilter() }),
            IconWithText(R.drawable.icon_count_down, "倒计时", { viewModel.toggleCountDown() }),
            IconWithText(speedIcon, "变速", { viewModel.toggleSpeed() }),
            IconWithText(mirrorIcon, "镜像", { viewModel.toggleMirror() })
        )
    }
    Box(modifier.fillMaxSize()) {
        if (recordingState == RecordingState.FINISHED) {
            BackHandler {
                viewModel.clearCamera()
            }
            when (cameraMode) {
                CameraMode.IMAGE -> ImagePreview(imageFilePath)
                CameraMode.VIDEO -> VideoPreview(videoFilePath)
                else -> {}
            }
        } else {
            if (recordingState == RecordingState.RECORDING) {
                BackHandler {
                    viewModel.cancelRecording()
                }
            }
            CameraPreview(lensFacing,recordingState,cameraMode)
            if (viewModel.selectedIndex == 1) {
                SquareIcon(
                    Modifier
                        .align(Alignment.BottomStart)
                        .clickable { viewModel.toggleEffect() }
                        .padding(start = 45.dp, bottom = 32.dp),
                    "特效", Image.IntImage(R.drawable.example)
                )
                if (viewModel.pauseMarks.isNotEmpty()) {
                    SquareIcon(
                        Modifier
                            .align(Alignment.BottomEnd)
                            .clickable { viewModel.stopRecording() }
                            .padding(end = 45.dp, bottom = 32.dp),
                        "下一步",
                        Image.IntImage(R.drawable.camera_next)
                    )
                } else {
                    SquareIcon(
                        Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 45.dp, bottom = 32.dp),
                        "相册",
                        Image.IntImage(R.drawable.image_empty)
                    )
                }
            }

        }
        if (viewModel.bottomDialog == BottomDialog.MENU) {
            if (!isRecording) {
                CloseBottom(Modifier.align(Alignment.TopStart), navController)
                MusicSelect(
                    Modifier
                        .align(Alignment.TopCenter)
                        .clickable { viewModel.toggleMusic() })
                CameraIcons(Modifier.align(Alignment.TopEnd), baseIcons, moreIcons)
                if (viewModel.isSpeedOn) {
                    SpeedLevelView(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 185.dp),
                        viewModel.currentSpeedLevel
                    ) { viewModel.toggleSpeedLevel(it) }
                }
                if (viewModel.selectedIndex != 1) {
                    CameraMode(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 145.dp),
                        viewModel, navController
                    )
                }
                SquareIcon(
                    Modifier
                        .align(Alignment.BottomStart)
                        .clickable { viewModel.toggleEffect() }
                        .padding(start = 45.dp, bottom = 32.dp),
                    "特效", Image.IntImage(R.drawable.example)
                )
                SquareIcon(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 45.dp, bottom = 32.dp),
                    "相册",
                    Image.IntImage(R.drawable.image_empty)
                )
            }
        }
        if (viewModel.bottomDialog in listOf(BottomDialog.MENU, BottomDialog.NULL)) {
            if (recordingState == RecordingState.FINISHED) {
                BackBottom(Modifier.align(Alignment.TopStart),
                    { viewModel.saveToDraft() },
                    { viewModel.clearCamera() })
                MusicSelect(Modifier.align(Alignment.TopCenter))
                CameraIcons(Modifier.align(Alignment.TopEnd), baseIcons, moreIcons)
            } else {
                if (recordingState == RecordingState.RECORDING) {
                    val recordingIcons = listOf(
                        IconWithText(
                            R.drawable.icon_transfer,
                            "翻转",
                            { viewModel.toggleCamera() }),
                    )
                    CameraIcons(Modifier.align(Alignment.TopEnd), recordingIcons)
                }
                CustomRecordButton(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp),
                    isRecording,
                    progress,
                    viewModel.isRecordingPause,
                    viewModel.pauseMarks
                ) { viewModel.toggleRecording() }
            }
        }
    }
}

@Composable
fun CameraMode(
    modifier: Modifier = Modifier,
    viewModel: CameraViewModel,
    navController: NavController
) {
    val allMode = CameraMode.entries.toTypedArray()
    Row(modifier) {
        allMode.forEachIndexed { index, mode ->
            CameraModeItem(
                mode.displayName,
                viewModel.cameraMode == mode
            ) {
                when (mode) {
                    CameraMode.TEXT -> {
                        navController.navigate(Screen.CameraText.route)
                    }

                    else -> viewModel.switchMode()
                }
            }
            if (index < allMode.size - 1) {
                Margin(width = 22)
            }
        }
    }
}