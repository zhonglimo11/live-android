package com.example.administrator.live.core.ui.customui

import android.content.Context
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.administrator.live.core.viewmodels.CameraMode
import com.example.administrator.live.core.viewmodels.RecordingState
import com.example.administrator.live.widgets.camerax.GLCameraView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener(
                {
                    continuation.resume(cameraProvider.get())
                }, ContextCompat.getMainExecutor(this)
            )
        }
    }

@Composable
fun CameraPreview(
    lensFacing: Int,
    recordingState: RecordingState,
    cameraMode: CameraMode,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val glCameraPreview = remember {
        GLCameraView(context, 9 / 16F)
    }
    val scope = remember {
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }
//    LaunchedEffect(recordingState) {
//        when (cameraMode) {
//            CameraMode.VIDEO -> {
//                val file = VideoUtils.createVideoFile(false, context)
//                glCameraPreview.takeVideo(file.absolutePath)
//            }
//
//            else -> {
//                val file = VideoUtils.createPictureFile(context)
//                glCameraPreview.takePicture(file.absolutePath)
//            }
//        }
//    }

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()

        val preview = Preview.Builder().apply {
            setTargetAspectRatio(AspectRatio.RATIO_16_9)
        }.build()
        glCameraPreview.switchCameraLensFacing(true, lensFacing)
        preview.surfaceProvider = glCameraPreview

        // 设置前置、后置摄像头切换
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
        kotlin.runCatching {
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview
            )
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        DisposableEffect(Unit) {
            onDispose {
                scope.launch {
                    glCameraPreview.switchCameraLensFacing(false, lensFacing)
                    val cameraProvider = context.getCameraProvider()
                    cameraProvider.unbindAll()
                }
            }
        }
        AndroidView(
            factory = { glCameraPreview },
            modifier = Modifier.fillMaxSize()
        )
    }
}