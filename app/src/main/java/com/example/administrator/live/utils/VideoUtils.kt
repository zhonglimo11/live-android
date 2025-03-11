package com.example.administrator.live.utils

import android.app.Application
import android.content.Context
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFprobeKit
import com.arthenica.ffmpegkit.ReturnCode
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object VideoUtils {

    fun getAudioCodec(inputFile: File): String? {
        val ffprobeCommand =
            arrayOf("-v", "quiet", "-print_format", "json", "-show_streams", inputFile.absolutePath)
        val session = FFprobeKit.execute(ffprobeCommand.joinToString(" "))
        when (session.returnCode.value) {
            ReturnCode.SUCCESS -> {
                val output = session.output
                val json = output.substringAfter("{").substringBefore("}")
                val audioStreams = json.split("},")
                for (stream in audioStreams) {
                    if (stream.contains("\"codec_type\":\"audio\"")) {
                        return stream.substringAfter("\"codec_name\":\"").substringBefore("\"")
                    }
                }
            }

            ReturnCode.CANCEL -> {
                Timber.w("FFprobe 被取消: ${inputFile.name}")
            }

            else -> {
                Timber.e("FFprobe 失败: ${inputFile.name}")
                Timber.e("FFprobe 输出: ${session.output}")
            }
        }
        return null
    }


    fun convertAudioToAAC(inputFile: File, outputFile: File) {
        val ffmpegCommand = arrayOf(
            "-i", inputFile.absolutePath,
            "-c:v", "copy",
            "-c:a", "aac",
            outputFile.absolutePath
        )
        Timber.d("FFmpeg 命令: ${ffmpegCommand.joinToString(" ")}")
        val session = FFmpegKit.execute(ffmpegCommand.joinToString(" "))
        when (session.returnCode.value) {
            ReturnCode.SUCCESS -> {
                Timber.i("音频转换成功: ${inputFile.name} -> ${outputFile.name}")
            }

            ReturnCode.CANCEL -> {
                Timber.w("音频转换被取消: ${inputFile.name}")
            }

            else -> {
                Timber.e("音频转换失败: ${inputFile.name} -> ${outputFile.name}")
                Timber.e("FFmpeg 输出: ${session.output}")
            }
        }
    }

    // 创建视频文件，命名为当前日期
    fun createVideoFile(isFinal: Boolean = false, context: Context): File {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDateTime = dateFormat.format(Date())
        var fileName = "video_$currentDateTime.mp4"
        if (isFinal) {
            fileName = "final_$fileName"
        }
        return File(context.filesDir, fileName)
    }


    // 创建图片文件，命名为当前日期
    fun createPictureFile(context: Context): File {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDateTime = dateFormat.format(Date())
        val fileName = "picture_$currentDateTime.jpg"
        return File(context.filesDir, fileName)
    }

    fun mergeVideos(videoFiles: List<File>, outputFile: File, application: Application) {
        Timber.d("开始视频合并处理")

        // 转换音频编码
        val convertedVideoFiles = mutableListOf<File>()
        videoFiles.forEach { inputFile ->
            val outputFile =
                File(application.filesDir, "${inputFile.nameWithoutExtension}_converted.mp4")
            val audioCodec = getAudioCodec(inputFile)
            if (audioCodec != "aac") {
                convertAudioToAAC(inputFile, outputFile)
                if (outputFile.exists()) {
                    convertedVideoFiles.add(outputFile)
                }
            } else {
                convertedVideoFiles.add(inputFile)
            }

        }

        // 创建 FFmpeg 命令来合并视频
        val fileList = convertedVideoFiles.joinToString("\n") { "file '${it.absolutePath}'" }
        Timber.d("生成的文件列表: $fileList")

        // 使用内部存储目录来创建 fileList.txt
        val listFile = File(application.filesDir, "fileList.txt")  // 使用应用内部存储
        listFile.writeText(fileList)
        Timber.d("文件列表已写入: ${listFile.absolutePath}")

        // FFmpeg 合并命令
        val ffmpegCommand = arrayOf(
            "-f", "concat",
            "-safe", "0",
            "-i", listFile.absolutePath,
            "-c", "copy",
            outputFile.absolutePath
        )
        Timber.d("FFmpeg 命令: ${ffmpegCommand.joinToString(" ")}")

        // 执行 FFmpeg 命令
        val session = FFmpegKit.execute(ffmpegCommand.joinToString(" "))
        when (session.returnCode.value) {
            ReturnCode.SUCCESS -> {
                Timber.i("视频合并成功")
            }

            ReturnCode.CANCEL -> {
                Timber.w("视频合并被取消")
            }

            else -> {
                Timber.e("视频合并失败: ${session.failStackTrace}")
                Timber.e("FFmpeg 输出: ${session.output}")
            }
        }
        // 清理临时文件和输入文件
        listFile.delete()
        convertedVideoFiles.forEach {
            it.delete()
        }
        videoFiles.forEach {
            it.delete()
        }
        Timber.d("临时文件和输入文件已删除")
    }

    /**
     * 视频文件变速
     */
    fun speedVideo(videoFile: File, speed: Float, outputFile: File) {
        Timber.d("开始视频变速处理: ${videoFile.name} -> 速度: $speed")
        // 构建 FFmpeg 滤镜，根据速度设置不同的音频滤镜
        val filterComplex = when (speed) {
            0.25f -> "[0:v]setpts=4*PTS[v];[0:a]atempo=0.5,atempo=0.5[a]"
            0.5f -> "[0:v]setpts=2*PTS[v];[0:a]atempo=0.5[a]"
            1.25f -> "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2[a]"
            1.5f -> "[0:v]setpts=0.25*PTS[v];[0:a]atempo=2,atempo=2[a]"
            else -> throw IllegalArgumentException("不支持的速度值: $speed")
        }
        // 构建 FFmpeg 命令
        val ffmpegCommand = mutableListOf<String>().apply {
            addAll(arrayOf("-i", videoFile.absolutePath))
            addAll(arrayOf("-filter_complex", filterComplex, "-map", "[v]", "-map", "[a]"))
            addAll(
                arrayOf(
                    "-c:v", "h264",
                    outputFile.absolutePath
                )
            )
        }

        Timber.d("FFmpeg 命令: ${ffmpegCommand.joinToString(" ")}")

        // 执行命令
        val session = FFmpegKit.execute(ffmpegCommand.joinToString(" "))
        when (session.returnCode.value) {
            ReturnCode.SUCCESS -> {
                Timber.i("视频变速成功: ${videoFile.name} -> ${outputFile.name}")
            }

            ReturnCode.CANCEL -> {
                Timber.w("视频变速被取消: ${videoFile.name}")
            }

            else -> {
                Timber.e("视频变速失败: ${session.output}")
            }
        }

        // 清理临时文件
        videoFile.delete()
        Timber.d("临时文件已删除")
    }
}