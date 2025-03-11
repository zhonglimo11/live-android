package com.example.administrator.live.utils

import android.content.Context
import com.example.administrator.live.utils.AppUtils.copyAssetToFile
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.FaceDetectorYN
import timber.log.Timber
import java.io.FileNotFoundException
import kotlin.math.max
import kotlin.math.min

class FaceBeautyProcessor(
    context: Context,
    private val inputSize: Size = Size(320.0, 320.0), // 可配置输入大小
    private val scoreThreshold: Float = 0.6f, // 可配置得分阈值
    private val nmsThreshold: Float = 0.3f // 可配置 NMS 阈值
) {
    private val faceDetector: FaceDetectorYN

    init {
        val onnxFile = copyAssetToFile(
            context,
            "opencv/face_detection_yunet_2023mar.onnx",
            "face_detection_yunet_2023mar.onnx"
        )

        if (!onnxFile.exists() || onnxFile.length() == 0L) {
            throw FileNotFoundException("ONNX 模型文件无效或未找到: ${onnxFile.absolutePath}")
        }

        try {
            faceDetector = FaceDetectorYN.create(
                onnxFile.absolutePath,
                "", // ONNX 不需要 config 文件
                inputSize,
                scoreThreshold,
                nmsThreshold
            )
            Timber.d(
                "人脸检测器初始化完成，模型路径: ${onnxFile.absolutePath}, 文件大小: ${onnxFile.length()} 字节"
            )
        } catch (e: Exception) {
            throw IllegalStateException("人脸检测器初始化失败: ${e.localizedMessage}", e)
        }
    }


    /**
     * 使用 FaceDetectorYN 进行人脸检测
     *
     * @param image 输入图像，类型为 OpenCV 的 Mat
     * @return 检测到的人脸区域列表，每个区域为 Rect
     */
    fun detectFaces(image: Mat): List<Rect> {
        val facesMat = Mat() // 存储检测结果的 2D Mat
        val faces = mutableListOf<Rect>()

        try {
            // 执行人脸检测
            val startTime = System.currentTimeMillis()
            val numFaces = faceDetector.detect(image, facesMat)
            val elapsedTime = System.currentTimeMillis() - startTime
            Timber.d("人脸检测耗时: $elapsedTime ms，检测到 $numFaces 张人脸")

            // 检测结果解析
            for (i in 0 until facesMat.rows()) {
                // 每一行表示一个人脸数据
                val row = facesMat.row(i)
                val data = DoubleArray(4) // 假设结果为 [x, y, width, height]
                row.get(0, 0, data)

                if (data.size == 4) {
                    val x = data[0].toInt()
                    val y = data[1].toInt()
                    val width = data[2].toInt()
                    val height = data[3].toInt()

                    // 检查人脸区域的有效性
                    if (width > 0 && height > 0) {
                        faces.add(Rect(x, y, width, height))
                    } else {
                        Timber.w("检测到无效的人脸区域: x=$x, y=$y, width=$width, height=$height")
                    }
                } else {
                    Timber.e("检测结果格式错误，数据长度不足: ${data.size}")
                }
            }

            if (faces.isEmpty()) {
                Timber.d("未检测到任何人脸")
            }
        } catch (e: Exception) {
            Timber.e("人脸检测过程中发生错误: ${e.localizedMessage}")
        } finally {
            // 确保释放 Mat 资源
            facesMat.release()
        }

        return faces
    }

    /**
     * 平滑皮肤（高斯模糊）
     *
     * @param image 输入图像
     * @param face 人脸区域的 Rect
     */
    fun smoothSkin(image: Mat, face: Rect) {
        val validFace = Rect(
            max(0, face.x),
            max(0, face.y),
            max(0, min(image.width() - face.x, face.width)),
            max(0, min(image.height() - face.y, face.height))
        )
        if (validFace.width == 0 || validFace.height == 0) {
            Timber.w("无效的人脸区域: $face")
            return
        }

        val faceRegion = image.submat(validFace)
        try {
            // 使用较小的高斯核，避免过度模糊
            Imgproc.GaussianBlur(faceRegion, faceRegion, Size(5.0, 5.0), 0.0)
            Timber.d("应用高斯模糊处理人脸区域: $validFace")
        } finally {
            faceRegion.release() // 确保释放资源
        }
    }

    /**
     * 调整亮度
     *
     * @param image 输入图像
     * @param face 人脸区域的 Rect
     */
    fun adjustBrightness(image: Mat, face: Rect) {
        val validFace = Rect(
            max(0, face.x),
            max(0, face.y),
            max(0, min(image.width() - face.x, face.width)),
            max(0, min(image.height() - face.y, face.height))
        )
        if (validFace.width == 0 || validFace.height == 0) {
            Timber.w("无效的人脸区域: $face")
            return
        }

        val faceRegion = image.submat(validFace)
        try {
            // 合理的亮度调整参数
            Core.addWeighted(faceRegion, 1.0, faceRegion, 0.0, 30.0, faceRegion)
            Timber.d("调整亮度，修改人脸区域: $validFace")
        } finally {
            faceRegion.release() // 确保释放资源
        }
    }

    /**
     * 使用双边滤波进行皮肤磨皮
     *
     * @param image 输入图像
     * @param face 人脸区域的 Rect
     */
    fun smoothSkinWithBilateralFilter(image: Mat, face: Rect) {
        val validFace = Rect(
            max(0, face.x),
            max(0, face.y),
            max(0, min(image.width() - face.x, face.width)),
            max(0, min(image.height() - face.y, face.height))
        )
        if (validFace.width == 0 || validFace.height == 0) {
            Timber.w("无效的人脸区域: $face")
            return
        }

        val faceRegion = image.submat(validFace)
        try {
            // 双边滤波处理
            Imgproc.bilateralFilter(faceRegion, faceRegion, 9, 75.0, 75.0)
            Timber.d("应用双边滤波处理人脸区域: $validFace")
        } finally {
            faceRegion.release() // 确保释放资源
        }
    }

    /**
     * 对相机帧进行美颜处理
     *
     * @param frame 输入的相机帧图像
     * @return 处理后图像
     */
    fun processFrame(frame: Mat): Mat {
        Timber.d("开始处理帧图像")
        val faces = detectFaces(frame)

        for (face in faces) {
            smoothSkin(frame, face) // 平滑皮肤
            adjustBrightness(frame, face) // 调整亮度
            smoothSkinWithBilateralFilter(frame, face) // 双边滤波处理
        }

        Timber.d("处理完毕，返回处理后的帧图像")
        return frame
    }
}