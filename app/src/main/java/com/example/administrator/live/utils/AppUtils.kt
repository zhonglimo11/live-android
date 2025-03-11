package com.example.administrator.live.utils

import android.content.Context
import android.graphics.Rect
import android.os.Environment
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.camera.core.ImageProxy
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.administrator.live.core.ui.theme.Red
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import timber.log.Timber
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * 工具类，提供与应用相关的辅助方法
 */
object AppUtils {

    /**
     * 显示Toast消息，使用字符串资源
     * @param context 上下文环境，用于获取资源和创建Toast
     * @param text 字符串资源ID，Toast要显示的文本内容
     * @param isLong 指示Toast持续时间的布尔值，如果为true则为长Toast，否则为短Toast
     */
    fun showToast(context: Context, @StringRes text: Int, isLong: Boolean) {
        showToast(context, context.getString(text), isLong)
    }

    /**
     * 拷贝资源文件到本地文件夹
     */
     fun copyAssetToFile(context: Context, assetName: String, outputName: String): File {
        val outputFile = File(context.filesDir, outputName)
        if (outputFile.exists() && outputFile.length() > 0) {
            Timber.d("文件已存在，跳过拷贝: ${outputFile.absolutePath}")
            return outputFile
        }

        try {
            context.assets.open(assetName).use { inputStream ->
                outputFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Timber.d("成功拷贝文件: $assetName 到 ${outputFile.absolutePath}")
        } catch (e: Exception) {
            Timber.e("拷贝资源文件失败: $assetName -> ${outputFile.absolutePath}, 错误: $e")
        }
        return outputFile
    }


    /**
     * 删除文件
     * @param filePath 文件路径
     * @return 删除成功返回true，否则返回false
     */
    fun deleteFile(filePath: String): Boolean {
        val file = File(filePath)
        return if (file.exists()) {
            val deleted = file.delete()
            if (!deleted) {
                Timber.e("删除文件失败：$filePath")
            }
            Timber.d("文件已删除：$filePath")
            deleted
        } else {
            Timber.e("文件不存在：$filePath")
            false
        }
    }

    /**
     * 显示Toast消息，使用字符串
     * @param context 上下文环境，用于创建Toast
     * @param text Toast要显示的文本内容
     * @param isLong 指示Toast持续时间的布尔值，如果为true则为长Toast，否则为短Toast
     */
    fun showToast(context: Context, text: String, isLong: Boolean) {
        Toast.makeText(context, text, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }

    /**
     * 调整输入框的 marginBottom
     * @param rootView 根布局视图
     * @param inputView 输入框视图
     */
    fun adjustInputMargin(rootView: View, inputView: View) {
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom
            val layoutParams = inputView.layoutParams as? RelativeLayout.LayoutParams
            if (keypadHeight > (screenHeight * 0.15)) {
                layoutParams?.bottomMargin = keypadHeight
            } else {
                layoutParams?.bottomMargin = 0
            }
            layoutParams?.let {
                inputView.layoutParams = it
                inputView.requestLayout()
            }
        }
    }

    /**
     * 扩展函数，将 dp 单位转换为像素单位
     * @param context 上下文环境，用于获取屏幕密度
     * @return 转换后的像素值
     */
    fun Int.dpToPx(context: Context): Int =
        (this * context.resources.displayMetrics.density).toInt()

    fun Context.dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    private val outputFormat: HanyuPinyinOutputFormat by lazy {
        val format = HanyuPinyinOutputFormat()
        format.caseType = HanyuPinyinCaseType.UPPERCASE
        format.toneType = HanyuPinyinToneType.WITHOUT_TONE
        format
    }

    /**
     * 计算所有首字母
     * @param name 用户名称
     * @return 首字母字符串
     */
    fun getInitialsAll(name: String): String {
        try {
            val initials = StringBuilder()
            for (char in name.toCharArray()) {
                val pinyinArray = PinyinHelper.toHanyuPinyinStringArray(char, outputFormat)
                if (pinyinArray != null && pinyinArray.isNotEmpty()) {
                    initials.append(pinyinArray[0].first())
                } else {
                    initials.append(char.toString().first())
                }
            }
            return initials.toString().uppercase(Locale.ROOT)
        } catch (e: BadHanyuPinyinOutputFormatCombination) {
            return name.first().toString().uppercase(Locale.ROOT)
        }
    }

    /**
     * 计算首字母
     * @param name 用户名称
     * @return 首字母字符串
     */
    fun getInitials(name: String): String {
        if (name.isEmpty()) return ""

        val firstChar = name.first()
        return when {
            firstChar.isLetter() -> {
                try {
                    val pinyinArray = PinyinHelper.toHanyuPinyinStringArray(firstChar, outputFormat)
                    if (pinyinArray != null && pinyinArray.isNotEmpty()) {
                        pinyinArray[0].first().uppercase(Locale.ROOT)
                    } else {
                        firstChar.uppercase(Locale.ROOT)
                    }
                } catch (e: BadHanyuPinyinOutputFormatCombination) {
                    firstChar.uppercase(Locale.ROOT)
                }
            }

            else -> firstChar.uppercase(Locale.ROOT)
        }
    }

    /**
     * 格式化日期，显示为时间或星期几
     */
    fun formatDate(date: Date): String {
        // 获取当前日期
        val currentDate = Calendar.getInstance()

        // 获取传入的日期
        val inputDate = Calendar.getInstance()
        inputDate.time = date

        // 判断是否为今天
        if (currentDate.get(Calendar.YEAR) == inputDate.get(Calendar.YEAR) &&
            currentDate.get(Calendar.DAY_OF_YEAR) == inputDate.get(Calendar.DAY_OF_YEAR)
        ) {
            // 如果是当天，则显示时间
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            return timeFormat.format(date)
        }

        // 判断是否为昨天
        currentDate.add(Calendar.DAY_OF_YEAR, -1)
        if (currentDate.get(Calendar.YEAR) == inputDate.get(Calendar.YEAR) &&
            currentDate.get(Calendar.DAY_OF_YEAR) == inputDate.get(Calendar.DAY_OF_YEAR)
        ) {
            return "昨天"
        }

        // 判断是否在本周内
        val startOfWeek = getStartOfWeek(currentDate)
        val endOfWeek = getEndOfWeek(currentDate)
        if (inputDate.get(Calendar.YEAR) == startOfWeek.get(Calendar.YEAR) &&
            inputDate.get(Calendar.DAY_OF_YEAR) >= startOfWeek.get(Calendar.DAY_OF_YEAR) &&
            inputDate.get(Calendar.DAY_OF_YEAR) <= endOfWeek.get(Calendar.DAY_OF_YEAR)
        ) {
            // 如果是在本周内，则返回星期几
            val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.getDefault())
            return dayOfWeekFormat.format(date)
        }

        // 更早的日期，则返回几月几日
        val monthDayFormat = SimpleDateFormat("MM月dd日", Locale.getDefault())
        return monthDayFormat.format(date)
    }

    private fun getStartOfWeek(calendar: Calendar): Calendar {
        val startOfWeek = Calendar.getInstance()
        startOfWeek.time = calendar.time
        startOfWeek.firstDayOfWeek = Calendar.MONDAY
        startOfWeek.set(Calendar.DAY_OF_WEEK, startOfWeek.firstDayOfWeek)
        return startOfWeek
    }

    private fun getEndOfWeek(calendar: Calendar): Calendar {
        val endOfWeek = Calendar.getInstance()
        endOfWeek.time = calendar.time
        endOfWeek.firstDayOfWeek = Calendar.MONDAY
        endOfWeek.set(Calendar.DAY_OF_WEEK, endOfWeek.firstDayOfWeek + 6)
        return endOfWeek
    }

    /**
     * 高亮显示搜索结果
     */
    fun highlightText(message: String, searchInput: String): AnnotatedString {
        val highlightedText = buildAnnotatedString {
            val parts = message.split(searchInput, ignoreCase = true)
            for (i in parts.indices) {
                append(parts[i])
                if (i < parts.size - 1) {
                    withStyle(style = SpanStyle(color = Red)) {
                        append(searchInput)
                    }
                }
            }
        }
        return highlightedText
    }

    fun getImageFoldersWithImages(): List<Pair<String, List<String>>> {
        val foldersWithImages = mutableListOf<Pair<String, List<String>>>()
        // 获取外部存储状态，确保可以访问
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val mediaDirs = arrayOf(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Environment.getExternalStorageDirectory()
            )
            for (mediaDir in mediaDirs) {
                mediaDir.listFiles { file ->
                    // 只处理目录
                    file.isDirectory && file.canRead()
                }?.forEach { directory ->
                    val images = directory.listFiles { file ->
                        // 确保文件是图片类型
                        file.isFile && file.extension.lowercase(Locale.ROOT) in listOf(
                            "jpg",
                            "jpeg",
                            "png"
                        )
                    }?.map { it.absolutePath.toString() } ?: emptyList()

                    if (images.isNotEmpty()) {
                        foldersWithImages.add(Pair(directory.name, images))
                    }
                }
            }
            // 创建一个“所有图片”的虚拟文件夹
            val allImages = foldersWithImages.flatMap { it.second }
            foldersWithImages.add(0, Pair("所有图片", allImages))
        }
        return foldersWithImages
    }

    fun getEffectIconsWithMetaFiles(context: Context): List<Triple<String, String, String>> {
        val effectDir = File("${context.cacheDir.path}/effect")
        val iconDir = File(effectDir, "icon")
        val dataDir = File(effectDir, "data")

        val effects = mutableListOf<Triple<String, String, String>>()

        if (iconDir.exists() && iconDir.isDirectory && dataDir.exists() && dataDir.isDirectory) {
            // 遍历 icon 目录下的文件
            iconDir.listFiles { file ->
                file.isFile && file.extension.equals("png", ignoreCase = true)
            }?.forEach { iconFile ->
                // 获取对应的 meta 文件路径
                val effectName = iconFile.nameWithoutExtension
                val metaFile = File(dataDir, "$effectName/meta.json")

                // 检查 meta 文件是否存在
                if (metaFile.exists() && metaFile.isFile) {
                    // 添加名称、图标文件路径和对应的 meta 文件路径到列表中
                    effects.add(Triple(effectName, iconFile.absolutePath, metaFile.absolutePath))
                }
            }
        }
        return effects
    }

    // 从资源文件中读取原始JSON数据
    fun getRawJson(context: Context, resId: Int): String {
        return context.resources.openRawResource(resId).bufferedReader().use { it.readText() }
    }

    /**
     * 将时长（秒）转换为时：分：秒
     */
    fun formatDuration(totalSeconds: Int): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return if (hours > 0) {
            String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
    }

    /**
     * 格式化数字，将数字转换为带单位
     */
    fun formatNumber(number: Int): String {
        return when {
            number >= 1_0000_0000 -> { // 大于等于1亿
                val billion = number / 1_0000_0000f
                DecimalFormat("#,##0.##").format(billion) + "亿"
            }

            number >= 10000 -> { // 大于等于1万
                val million = number / 10000f
                DecimalFormat("#,##0.##").format(million) + "万"
            }

            else -> {
                number.toString()
            }
        }
    }

    /**
     * 将ImageProxy转换为NV21格式的ByteArray
     */
    private fun imageProxyToNV21(image: ImageProxy): ByteArray {
        val yPlane = image.planes[0]
        val uPlane = image.planes[1]
        val vPlane = image.planes[2]

        val ySize = yPlane.buffer.remaining()
        val uvSize = uPlane.buffer.remaining() + vPlane.buffer.remaining()

        val nv21 = ByteArray(ySize + uvSize)

        yPlane.buffer.get(nv21, 0, ySize)

        val uBuffer = ByteArray(uPlane.buffer.remaining())
        uPlane.buffer.get(uBuffer)

        val vBuffer = ByteArray(vPlane.buffer.remaining())
        vPlane.buffer.get(vBuffer)

        for (i in vBuffer.indices) {
            nv21[ySize + i * 2] = vBuffer[i]
            nv21[ySize + i * 2 + 1] = uBuffer[i]
        }

        return nv21
    }

    /**
     * 将NV21格式的ByteArray转换为OpenCV的Mat对象
     */
    private fun nv21ToMat(nv21: ByteArray, width: Int, height: Int): Mat {
        // 创建 YUV 数据的 Mat
        val yuv = Mat(height + height / 2, width, CvType.CV_8UC1)
        yuv.put(0, 0, nv21)

        // 转换为 BGR 格式
        val bgr = Mat()
        Imgproc.cvtColor(yuv, bgr, Imgproc.COLOR_YUV2BGR_NV21)
        return bgr
    }


    /**
     * 将ImageProxy转换为OpenCV的Mat对象
     */
    fun imageProxyToMat(image: ImageProxy): Mat {
        val nv21 = imageProxyToNV21(image)
        val mat = nv21ToMat(nv21, image.width, image.height)
        image.close() // 确保关闭 ImageProxy
        return mat
    }


}
