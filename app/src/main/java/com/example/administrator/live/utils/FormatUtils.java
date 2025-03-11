package com.example.administrator.live.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 提供格式化相关功能的工具类。
 */
public final class FormatUtils {
    /**
     * 构造方法，避免实例化该工具类
     */
    private FormatUtils() {
        // 抛出异常，防止外部创建实例
        throw new AssertionError();
    }

    /**
     * 根据给定的秒数，返回格式化后的时间字符串。
     *
     * @param seconds 总秒数。
     * @return 格式化后的时间字符串，格式为 "HH:mm:ss"（如果总秒数大于等于3600）或者 "mm:ss"。
     */
    public static String getDurationString(int seconds) {
        // 根据秒数创建 Date 对象
        Date date = new Date(seconds * 1000L);
        // 根据总秒数选择适当的时间格式
        SimpleDateFormat formatter = new SimpleDateFormat(seconds >= 3600 ? "HH:mm:ss" : "mm:ss", Locale.US);
        // 设置时区为 GMT
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        // 返回格式化后的时间字符串
        return formatter.format(date);
    }
}
