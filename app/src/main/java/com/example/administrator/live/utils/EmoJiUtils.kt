package com.example.administrator.live.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import androidx.core.content.ContextCompat
import com.example.administrator.live.R
import com.example.administrator.live.utils.AppUtils.dpToPx


object EmoJiUtils {
    private val emoJiMap: MutableMap<String, Int> = HashMap()
    private var allMap: MutableMap<String, Int> = HashMap()

    init {
        emoJiMap["[emoji_00]"] = R.drawable.emoji_00
        emoJiMap["[emoji_01]"] = R.drawable.emoji_01
        emoJiMap["[emoji_02]"] = R.drawable.emoji_02
        emoJiMap["[emoji_03]"] = R.drawable.emoji_03
        emoJiMap["[emoji_04]"] = R.drawable.emoji_04
        emoJiMap["[emoji_05]"] = R.drawable.emoji_05
        emoJiMap["[emoji_06]"] = R.drawable.emoji_06
        emoJiMap["[emoji_07]"] = R.drawable.emoji_07
        emoJiMap["[emoji_08]"] = R.drawable.emoji_08
        emoJiMap["[emoji_09]"] = R.drawable.emoji_09
        emoJiMap["[emoji_10]"] = R.drawable.emoji_10
        emoJiMap["[emoji_11]"] = R.drawable.emoji_11
        emoJiMap["[emoji_12]"] = R.drawable.emoji_12
        emoJiMap["[emoji_13]"] = R.drawable.emoji_13
        emoJiMap["[emoji_14]"] = R.drawable.emoji_14
        emoJiMap["[emoji_15]"] = R.drawable.emoji_15
        emoJiMap["[emoji_16]"] = R.drawable.emoji_16
        emoJiMap["[emoji_17]"] = R.drawable.emoji_17
        emoJiMap["[emoji_18]"] = R.drawable.emoji_18
        emoJiMap["[emoji_19]"] = R.drawable.emoji_19
        emoJiMap["[emoji_20]"] = R.drawable.emoji_20
        emoJiMap["[emoji_21]"] = R.drawable.emoji_21
        emoJiMap["[emoji_22]"] = R.drawable.emoji_22
        emoJiMap["[emoji_23]"] = R.drawable.emoji_23
        emoJiMap["[emoji_24]"] = R.drawable.emoji_24
        emoJiMap["[emoji_25]"] = R.drawable.emoji_25
        emoJiMap["[emoji_26]"] = R.drawable.emoji_26
        emoJiMap["[emoji_27]"] = R.drawable.emoji_27
        emoJiMap["[emoji_28]"] = R.drawable.emoji_28
        emoJiMap["[emoji_29]"] = R.drawable.emoji_29
        emoJiMap["[emoji_30]"] = R.drawable.emoji_30
        emoJiMap["[emoji_31]"] = R.drawable.emoji_31
        emoJiMap["[emoji_32]"] = R.drawable.emoji_32
        emoJiMap["[emoji_33]"] = R.drawable.emoji_33
        emoJiMap["[emoji_34]"] = R.drawable.emoji_34
        emoJiMap["[emoji_35]"] = R.drawable.emoji_35
        emoJiMap["[emoji_36]"] = R.drawable.emoji_36
        emoJiMap["[emoji_37]"] = R.drawable.emoji_37
        emoJiMap["[emoji_38]"] = R.drawable.emoji_38
        emoJiMap["[emoji_39]"] = R.drawable.emoji_39
        emoJiMap["[emoji_40]"] = R.drawable.emoji_40
        emoJiMap["[emoji_41]"] = R.drawable.emoji_41
        emoJiMap["[emoji_42]"] = R.drawable.emoji_42
        emoJiMap["[emoji_43]"] = R.drawable.emoji_43
        emoJiMap["[emoji_44]"] = R.drawable.emoji_44
        emoJiMap["[emoji_45]"] = R.drawable.emoji_45
        emoJiMap["[emoji_46]"] = R.drawable.emoji_46
        emoJiMap["[emoji_47]"] = R.drawable.emoji_47
        emoJiMap["[emoji_48]"] = R.drawable.emoji_48
        emoJiMap["[emoji_49]"] = R.drawable.emoji_49
        emoJiMap["[emoji_50]"] = R.drawable.emoji_50
        emoJiMap["[emoji_51]"] = R.drawable.emoji_51
        emoJiMap["[emoji_52]"] = R.drawable.emoji_52
        emoJiMap["[emoji_53]"] = R.drawable.emoji_53
        emoJiMap["[emoji_54]"] = R.drawable.emoji_54
        emoJiMap["[emoji_55]"] = R.drawable.emoji_55
        emoJiMap["[emoji_56]"] = R.drawable.emoji_56
        emoJiMap["[emoji_57]"] = R.drawable.emoji_57
        emoJiMap["[emoji_58]"] = R.drawable.emoji_58
        emoJiMap["[emoji_59]"] = R.drawable.emoji_59
        allMap.putAll(emoJiMap)
    }

    fun getEmoJiMap(): Map<String, Int> = emoJiMap

    /**
     * 解析文本中的表情符号，并将其转换为图像。
     *
     * 该函数通过正则表达式匹配文本中的表情符号，然后将匹配到的表情符号替换为相应的图像资源。
     * 它用于处理包含特定格式表情符号的字符串，并将其转换为带有图像的可显示文本。
     *
     * @param context 上下文对象，用于访问资源。
     * @param content 包含表情符号的文本内容。
     * @return 返回一个 SpannableString，其中的表情符号已被图像替换。
     */
    fun parseEmoJi(context: Context, content: String): SpannableString {
        // 创建一个可变的字符串，用于后续添加表情图像
        val spannable = SpannableString(content)
        // 定义一个正则表达式，用于匹配文本中的表情符号
        val reg = "\\[[a-zA-Z0-9_\\u4e00-\\u9fa5]+]".toRegex()
        // 使用正则表达式在文本中查找匹配的表情符号
        var matcher = reg.find(content)
        // 遍历所有匹配的表情符号
        while (matcher != null) {
            // 获取当前匹配到的表情符号文本
            val regEmoJi = matcher.value
            // 获取当前匹配表情符号在文本中的起始位置
            val start = matcher.range.first
            // 获取当前匹配表情符号在文本中的结束位置
            val end = matcher.range.last + 1
            // 从预定义的地图中获取与表情符号对应的图像资源ID
            val resId = allMap[regEmoJi]
            // 如果找到了对应的图像资源ID
            resId?.let {
                // 根据资源ID获取表情图像资源
                val drawable = ContextCompat.getDrawable(context, it)!!
                // 设置图像的边界
                val imgSize = 20.dpToPx(context)
                drawable.setBounds(0, 0, imgSize, imgSize)
                // 创建一个ImageSpan，用于在文本中插入图像
                val imageSpan = ImageSpan(drawable)
                // 在spannable字符串中替换表情符号为图像
                spannable.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            // 在文本中继续查找下一个表情符号
            matcher = reg.find(content, end)
        }
        // 返回已经处理过的文本，其中的表情符号已被图像替换
        return spannable
    }
}
