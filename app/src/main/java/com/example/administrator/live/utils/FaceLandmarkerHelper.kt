/*
 * Copyright 2023 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.administrator.live.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.SystemClock
import androidx.annotation.VisibleForTesting
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult
import timber.log.Timber

class FaceLandmarkerHelper(
    var minFaceDetectionConfidence: Float = DEFAULT_FACE_DETECTION_CONFIDENCE,
    var minFaceTrackingConfidence: Float = DEFAULT_FACE_TRACKING_CONFIDENCE,
    var minFacePresenceConfidence: Float = DEFAULT_FACE_PRESENCE_CONFIDENCE,
    var maxNumFaces: Int = DEFAULT_NUM_FACES,
    var currentDelegate: Int = DELEGATE_CPU,
    var runningMode: RunningMode = RunningMode.IMAGE,
    val context: Context,
    // 仅在运行模式为 RunningMode.LIVE_STREAM 时使用此监听器
    val faceLandmarkerHelperListener: LandmarkerListener? = null
) {

    // 对于这个示例，这需要是一个可变变量，以便在更改时重置。
    // 如果 Face Landmarker 不会更改，则使用懒加载 val 更合适。
    private var faceLandmarker: FaceLandmarker? = null

    init {
        setupFaceLandmarker()
    }

    fun clearFaceLandmarker() {
        faceLandmarker?.close()
        faceLandmarker = null
    }

    // 返回 FaceLandmarkerHelper 的运行状态
    fun isClose(): Boolean {
        return faceLandmarker == null
    }

    // 使用当前设置在使用它的线程上初始化 Face Landmarker。
    // CPU 可以用于在主线程上创建并在后台线程上使用的 Landmarker，但
    // GPU 委托需要在初始化 Landmarker 的线程上使用
    fun setupFaceLandmarker() {
        // 设置通用的 Face Landmarker 选项
        val baseOptionBuilder = BaseOptions.builder()

        // 使用指定的硬件运行模型，默认为 CPU
        when (currentDelegate) {
            DELEGATE_CPU -> {
                baseOptionBuilder.setDelegate(Delegate.CPU)
            }

            DELEGATE_GPU -> {
                baseOptionBuilder.setDelegate(Delegate.GPU)
            }
        }

        baseOptionBuilder.setModelAssetPath(MP_FACE_LANDMARKER_TASK)

        // 检查运行模式是否与 faceLandmarkerHelperListener 一致
        when (runningMode) {
            RunningMode.LIVE_STREAM -> {
                if (faceLandmarkerHelperListener == null) {
                    throw IllegalStateException(
                        "当 runningMode 为 LIVE_STREAM 时，faceLandmarkerHelperListener 必须设置。"
                    )
                }
            }

            else -> {
                // 无操作
            }
        }

        try {
            val baseOptions = baseOptionBuilder.build()
            // 使用基础选项和特定于 Face Landmarker 的选项创建一个选项构建器
            val optionsBuilder =
                FaceLandmarker.FaceLandmarkerOptions.builder()
                    .setBaseOptions(baseOptions)
                    .setMinFaceDetectionConfidence(minFaceDetectionConfidence)
                    .setMinTrackingConfidence(minFaceTrackingConfidence)
                    .setMinFacePresenceConfidence(minFacePresenceConfidence)
                    .setNumFaces(maxNumFaces)
                    .setOutputFaceBlendshapes(true)
                    .setRunningMode(runningMode)

            // ResultListener 和 ErrorListener 仅在 LIVE_STREAM 模式下使用
            if (runningMode == RunningMode.LIVE_STREAM) {
                optionsBuilder
                    .setResultListener(this::returnLivestreamResult)
                    .setErrorListener(this::returnLivestreamError)
            }

            val options = optionsBuilder.build()
            faceLandmarker =
                FaceLandmarker.createFromOptions(context, options)
        } catch (e: IllegalStateException) {
            faceLandmarkerHelperListener?.onError(
                "Face Landmarker 初始化失败。请查看错误日志以获取详细信息"
            )
            Timber.e(
                "MediaPipe 加载任务时出错: " + e
                    .message
            )
        } catch (e: RuntimeException) {
            // 这发生在使用的模型不支持 GPU 时
            faceLandmarkerHelperListener?.onError(
                "Face Landmarker 初始化失败。请查看错误日志以获取详细信息", GPU_ERROR
            )
            Timber.e("Face Landmarker 加载模型时出错: " + e.message)
        }
    }


    // 将 ImageProxy 转换为 MP Image 并传递给 FaceLandmarkerHelper。
    fun detectLiveStream(
        imageProxy: ImageProxy,
        isFrontCamera: Boolean
    ) {
        if (runningMode != RunningMode.LIVE_STREAM) {
            throw IllegalArgumentException(
                "尝试调用 detectLiveStream 时未使用 RunningMode.LIVE_STREAM"
            )
        }
        val frameTime = SystemClock.uptimeMillis()

        // 从帧中复制 RGB 位到位图缓冲区
        val bitmapBuffer =
            Bitmap.createBitmap(
                imageProxy.width,
                imageProxy.height,
                Bitmap.Config.ARGB_8888
            )
        imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer) }
        imageProxy.close()

        val matrix = Matrix().apply {
            // 将从相机接收到的帧旋转到与显示方向相同的方向
            postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())

            // 如果用户使用前置摄像头，则翻转图像
            if (isFrontCamera) {
                postScale(
                    -1f,
                    1f,
                    imageProxy.width.toFloat(),
                    imageProxy.height.toFloat()
                )
            }
        }
        val rotatedBitmap = Bitmap.createBitmap(
            bitmapBuffer, 0, 0, bitmapBuffer.width, bitmapBuffer.height,
            matrix, true
        )

        // 将输入的 Bitmap 对象转换为 MPImage 对象以运行推理
        val mpImage = BitmapImageBuilder(rotatedBitmap).build()

        detectAsync(mpImage, frameTime)
    }

    // 使用 MediaPipe Face Landmarker API 运行人脸关键点检测
    @VisibleForTesting
    fun detectAsync(mpImage: MPImage, frameTime: Long) {
        faceLandmarker?.detectAsync(mpImage, frameTime)
        // 由于我们使用的是 LIVE_STREAM 模式，因此地标结果将在 returnLivestreamResult 函数中返回
    }

    // 接受从用户图库加载的视频文件的 URI，并尝试对视频运行人脸关键点检测。此过程将评估视频中的每一帧，并将结果附加到一个将返回的包中。
    fun detectVideoFile(
        videoUri: Uri,
        inferenceIntervalMs: Long
    ): VideoResultBundle? {
        if (runningMode != RunningMode.VIDEO) {
            throw IllegalArgumentException(
                "尝试调用 detectVideoFile 时未使用 RunningMode.VIDEO"
            )
        }

        // 推理时间是从开始到结束的系统时间差
        val startTime = SystemClock.uptimeMillis()

        var didErrorOccurred = false

        // 从视频中加载帧并运行人脸关键点检测
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, videoUri)
        val videoLengthMs =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toLong()

        // 注意：我们需要从帧中读取宽度/高度，而不是直接获取视频的宽度/高度，因为 MediaRetriever 返回的帧比实际视频文件的尺寸小。
        val firstFrame = retriever.getFrameAtTime(0)
        val width = firstFrame?.width
        val height = firstFrame?.height

        // 如果视频无效，返回 null 检测结果
        if ((videoLengthMs == null) || (width == null) || (height == null)) return null

        // 接下来，我们将每隔 frameInterval ms 获取一帧，然后在这些帧上运行检测。
        val resultList = mutableListOf<FaceLandmarkerResult>()
        val numberOfFrameToRead = videoLengthMs.div(inferenceIntervalMs)

        for (i in 0..numberOfFrameToRead) {
            val timestampMs = i * inferenceIntervalMs // 毫秒

            retriever
                .getFrameAtTime(
                    timestampMs * 1000, // 将毫秒转换为微秒
                    MediaMetadataRetriever.OPTION_CLOSEST
                )
                ?.let { frame ->
                    // 将视频帧转换为 ARGB_8888，这是 MediaPipe 所需的格式
                    val argb8888Frame =
                        if (frame.config == Bitmap.Config.ARGB_8888) frame
                        else frame.copy(Bitmap.Config.ARGB_8888, false)

                    // 将输入的 Bitmap 对象转换为 MPImage 对象以运行推理
                    val mpImage = BitmapImageBuilder(argb8888Frame).build()

                    // 使用 MediaPipe Face Landmarker API 运行人脸关键点检测
                    faceLandmarker?.detectForVideo(mpImage, timestampMs)
                        ?.let { detectionResult ->
                            resultList.add(detectionResult)
                        } ?: {
                        didErrorOccurred = true
                        faceLandmarkerHelperListener?.onError(
                            "在 detectVideoFile 中无法返回 ResultBundle"
                        )
                    }
                }
                ?: run {
                    didErrorOccurred = true
                    faceLandmarkerHelperListener?.onError(
                        "在视频中检测时无法检索指定时间的帧"
                    )
                }
        }

        retriever.release()

        val inferenceTimePerFrameMs =
            (SystemClock.uptimeMillis() - startTime).div(numberOfFrameToRead)

        return if (didErrorOccurred) {
            null
        } else {
            VideoResultBundle(resultList, inferenceTimePerFrameMs, height, width)
        }
    }

    // 接受一个 Bitmap 并在其上运行人脸关键点检测，将结果返回给调用者
    fun detectImage(image: Bitmap): ResultBundle? {
        if (runningMode != RunningMode.IMAGE) {
            throw IllegalArgumentException(
                "尝试调用 detectImage 时未使用 RunningMode.IMAGE"
            )
        }

        // 推理时间是从开始到结束的系统时间差
        val startTime = SystemClock.uptimeMillis()

        // 将输入的 Bitmap 对象转换为 MPImage 对象以运行推理
        val mpImage = BitmapImageBuilder(image).build()

        // 使用 MediaPipe Face Landmarker API 运行人脸关键点检测
        faceLandmarker?.detect(mpImage)?.also { landmarkResult ->
            val inferenceTimeMs = SystemClock.uptimeMillis() - startTime
            return ResultBundle(
                landmarkResult,
                inferenceTimeMs,
                image.height,
                image.width
            )
        }

        // 如果 faceLandmarker?.detect() 返回 null，这可能是错误。返回 null 表示这一点。
        faceLandmarkerHelperListener?.onError(
            "人脸关键点检测失败。"
        )
        return null
    }

    // 将地标结果返回给这个 FaceLandmarkerHelper 的调用者
    private fun returnLivestreamResult(
        result: FaceLandmarkerResult,
        input: MPImage
    ) {
        if (result.faceLandmarks().size > 0) {
            val finishTimeMs = SystemClock.uptimeMillis()
            val inferenceTime = finishTimeMs - result.timestampMs()

            faceLandmarkerHelperListener?.onResults(
                ResultBundle(
                    result,
                    inferenceTime,
                    input.height,
                    input.width
                )
            )
        } else {
            faceLandmarkerHelperListener?.onEmpty()
        }
    }

    // 将检测过程中抛出的错误返回给这个 FaceLandmarkerHelper 的调用者
    private fun returnLivestreamError(error: RuntimeException) {
        faceLandmarkerHelperListener?.onError(
            error.message ?: "发生未知错误"
        )
    }

    companion object {
        private const val MP_FACE_LANDMARKER_TASK = "face_landmarker.task"
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1
        const val DEFAULT_FACE_DETECTION_CONFIDENCE = 0.5F
        const val DEFAULT_FACE_TRACKING_CONFIDENCE = 0.5F
        const val DEFAULT_FACE_PRESENCE_CONFIDENCE = 0.5F
        const val DEFAULT_NUM_FACES = 2
        const val OTHER_ERROR = 0
        const val GPU_ERROR = 1
    }

    data class ResultBundle(
        val result: FaceLandmarkerResult,
        val inferenceTime: Long,
        val inputImageHeight: Int,
        val inputImageWidth: Int,
    )

    data class VideoResultBundle(
        val results: List<FaceLandmarkerResult>,
        val inferenceTime: Long,
        val inputImageHeight: Int,
        val inputImageWidth: Int,
    )

    interface LandmarkerListener {
        fun onError(error: String, errorCode: Int = OTHER_ERROR)
        fun onResults(resultBundle: ResultBundle)

        fun onEmpty() {}
    }
}