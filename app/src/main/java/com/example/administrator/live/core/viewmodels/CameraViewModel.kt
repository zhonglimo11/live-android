package com.example.administrator.live.core.viewmodels

import android.app.Application
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.administrator.live.R
import com.example.administrator.live.bean.Image
import com.example.administrator.live.core.repository.MediaRepository
import com.example.administrator.live.core.repository.MediaType
import com.example.administrator.live.core.viewmodels.CameraMode.IMAGE
import com.example.administrator.live.core.viewmodels.CameraMode.TEXT
import com.example.administrator.live.core.viewmodels.CameraMode.VIDEO
import com.example.administrator.live.utils.AppUtils.deleteFile
import com.example.administrator.live.utils.FaceLandmarkerHelper
import com.example.administrator.live.widgets.camerax.utils.GLCameraxUtils
import timber.log.Timber
import java.io.File

enum class RecordingState {
    NOT_STARTED,
    RECORDING,
    FINISHED
}

enum class CameraMode(val displayName: String) {
    IMAGE("拍照"),
    VIDEO("视频"),
    TEXT("文字")
}

enum class BottomDialog {
    NULL, FILTER, EFFECT, BEAUTY, MUSIC, MENU
}

enum class SpeedLevel(val value: Float, val title: String) {
    LOW_LOW(0.25f, "极慢"),
    LOW(0.5f, "慢"),
    NORMAL(1f, "标准"),
    FAST(1.25f, "快"),
    FAST_FAST(1.5f, "极快")
}

class CameraViewModel(private val application: Application) : AndroidViewModel(application) {
    var selectedIndex by mutableIntStateOf(2)
    var cameraMode by mutableStateOf(VIDEO)
    var isMirror by mutableStateOf(false)
    var isSpeedOn by mutableStateOf(false)
    var bottomDialog by mutableStateOf(BottomDialog.MENU)
    var imageCaptureFlashMode by mutableIntStateOf(ImageCapture.FLASH_MODE_AUTO)
    var lensFacing by mutableIntStateOf(CameraSelector.LENS_FACING_FRONT)
    var faceResultBundle by mutableStateOf<FaceLandmarkerHelper.ResultBundle?>(null)
    var isEmptyFace by mutableStateOf(false)
    var isRecordingPause by mutableStateOf(false)
    var recordingState by mutableStateOf(RecordingState.NOT_STARTED)
    var elapsedTime by mutableIntStateOf(0)
    var pauseMarks = mutableListOf<Float>()
    private val maxTime = 600 // 最大录制时间（单位为 1/10 秒）
    private val mediaRepository: MediaRepository = MediaRepository(application)
    var imageFilePath by mutableStateOf<Image>(Image.IntImage(R.drawable.camera_bg_01))
    var videoFilePath by mutableStateOf("")
    // 设置不同的速度挡位
    var currentSpeedLevel by mutableStateOf(SpeedLevel.NORMAL)

    fun toggleSpeedLevel(level: SpeedLevel) {
        Timber.d("切换速度为: ${level.value}")
        currentSpeedLevel = level
    }

    init {
        GLCameraxUtils.init(application)
    }

    fun switchMode() {
        when (cameraMode) {
            IMAGE -> {
                cameraMode = VIDEO
            }

            VIDEO -> {
                cameraMode = IMAGE
            }

            else -> {}
        }
    }

    fun toggleRecording() {
        if (selectedIndex == 1) {
            if (recordingState == RecordingState.RECORDING) {
//                if (isRecordingPause) {
//                } else {
//                    pauseMarks.add(elapsedTime.toFloat() / maxTime)
//                }
                recordingState = RecordingState.FINISHED
            } else {
                initRecording()
                if (recordingState == RecordingState.NOT_STARTED) {
                    bottomDialog = BottomDialog.NULL
                }
            }
        } else {
            when {
                recordingState == RecordingState.RECORDING -> stopRecording()
                cameraMode == IMAGE -> takePicture()
                else -> initRecording()
            }
            if (recordingState == RecordingState.NOT_STARTED) {
                bottomDialog = BottomDialog.NULL
            }
        }
    }

    private fun takePicture() {

    }

    private fun initRecording() {
        recordingState = RecordingState.RECORDING
    }

    fun cancelRecording() {
        recordingState = RecordingState.NOT_STARTED
    }

    fun stopRecording() {
        recordingState = RecordingState.NOT_STARTED
    }
    // 切换摄像头
    fun toggleCamera() {
        lensFacing = when (lensFacing) {
            CameraSelector.LENS_FACING_FRONT -> CameraSelector.LENS_FACING_BACK
            else -> CameraSelector.LENS_FACING_FRONT
        }
    }

    //保存到本地媒体库
    private fun saveToMediaStore(file: File, type: MediaType) {
        mediaRepository.saveFileToMediaStore(file, type)
    }


    //美颜
    fun toggleBeauty() {
        bottomDialog =
            if (bottomDialog != BottomDialog.BEAUTY) BottomDialog.BEAUTY else BottomDialog.MENU
    }

    //滤镜
    fun toggleFilter() {
        bottomDialog =
            if (bottomDialog != BottomDialog.FILTER) BottomDialog.FILTER else BottomDialog.MENU
    }

    //特效
    fun toggleEffect() {
        bottomDialog =
            if (bottomDialog != BottomDialog.EFFECT) BottomDialog.EFFECT else BottomDialog.MENU
    }

    //音乐
    fun toggleMusic() {
        bottomDialog =
            if (bottomDialog != BottomDialog.MUSIC) BottomDialog.MUSIC else BottomDialog.MENU
    }

    //倒计时
    fun toggleCountDown() {
    }

    //变速
    fun toggleSpeed() {
        isSpeedOn = !isSpeedOn
    }

    //镜像
    fun toggleMirror() {
        isMirror = !isMirror
    }

    //清空内容
    fun clearCamera() {
        val file = when (cameraMode) {
            IMAGE -> (imageFilePath as Image.StringImage).value
            VIDEO -> videoFilePath
            TEXT -> TODO()
        }
        isRecordingPause = false
        deleteFile(file)
        backDefaultState()
    }

    //存草稿箱
    fun saveToDraft() {
        val file = when (cameraMode) {
            IMAGE -> (imageFilePath as Image.StringImage).value
            VIDEO -> videoFilePath
            TEXT -> TODO()
        }
        val type = when (cameraMode) {
            IMAGE -> MediaType.IMAGE
            VIDEO -> MediaType.VIDEO
            TEXT -> TODO()
        }
        file.let {
            saveToMediaStore(File(it), type)
            deleteFile(it)
        }
        backDefaultState()
    }

    //回到默认状态
    fun backDefaultState() {
        recordingState = RecordingState.NOT_STARTED
        bottomDialog = BottomDialog.MENU
    }

    // 切换闪光灯
    fun toggleFlash() {
        imageCaptureFlashMode = when (imageCaptureFlashMode) {
            ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_ON
            ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_AUTO
            ImageCapture.FLASH_MODE_AUTO -> ImageCapture.FLASH_MODE_OFF
            else -> throw IllegalArgumentException("Invalid flash mode")
        }
    }

}