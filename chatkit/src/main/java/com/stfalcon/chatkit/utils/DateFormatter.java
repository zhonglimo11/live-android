/*******************************************************************************
 * Copyright 2016 stfalcon.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.stfalcon.chatkit.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * DateFormatter 类提供了日期格式化以及日期比较的功能。
 * 该类为最终类，不可被继承。
 */
public final class DateFormatter {

    /**
     * 私有构造方法，防止外部实例化此类。
     */
    private DateFormatter() {
        throw new AssertionError();
    }

    /**
     * 根据给定的日期和模板格式化日期。
     *
     * @param date     日期对象
     * @param template 模板枚举
     * @return 格式化的日期字符串
     */
    public static String format(Date date, Template template) {
        return format(date, template.get());
    }

    /**
     * 根据给定的日期和格式字符串格式化日期。
     *
     * @param date   日期对象
     * @param format 日期格式字符串
     * @return 格式化的日期字符串
     */
    public static String format(Date date, String format) {
        if (date == null) return "";
        return new SimpleDateFormat(format, Locale.getDefault())
                .format(date);
    }

    /**
     * 判断两个日期是否在同一天。
     *
     * @param date1 第一个日期对象
     * @param date2 第二个日期对象
     * @return 如果两个日期都在同一天则返回 true，否则返回 false
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("Dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * 判断两个日历对象是否在同一天。
     *
     * @param cal1 第一个日历对象
     * @param cal2 第二个日历对象
     * @return 如果两个日历都在同一天则返回 true，否则返回 false
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("Dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 判断两个日期是否在同一年。
     *
     * @param date1 第一个日期对象
     * @param date2 第二个日期对象
     * @return 如果两个日期都在同一年则返回 true，否则返回 false
     */
    public static boolean isSameYear(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("Dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameYear(cal1, cal2);
    }

    /**
     * 判断两个日历对象是否在同一年。
     *
     * @param cal1 第一个日历对象
     * @param cal2 第二个日历对象
     * @return 如果两个日历都在同一年则返回 true，否则返回 false
     */
    public static boolean isSameYear(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("Dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR));
    }

    /**
     * 判断给定的日历对象是否表示今天。
     *
     * @param calendar 日历对象
     * @return 如果是今天则返回 true，否则返回 false
     */
    public static boolean isToday(Calendar calendar) {
        return isSameDay(calendar, Calendar.getInstance());
    }

    /**
     * 判断给定的日期对象是否表示今天。
     *
     * @param date 日期对象
     * @return 如果是今天则返回 true，否则返回 false
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    /**
     * 判断给定的日历对象是否表示昨天。
     *
     * @param calendar 日历对象
     * @return 如果是昨天则返回 true，否则返回 false
     */
    public static boolean isYesterday(Calendar calendar) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        return isSameDay(calendar, yesterday);
    }

    /**
     * 判断给定的日期对象是否表示昨天。
     *
     * @param date 日期对象
     * @return 如果是昨天则返回 true，否则返回 false
     */
    public static boolean isYesterday(Date date) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        return isSameDay(date, yesterday.getTime());
    }

    /**
     * 判断给定的日期对象是否表示当前年份。
     *
     * @param date 日期对象
     * @return 如果是在当前年份则返回 true，否则返回 false
     */
    public static boolean isCurrentYear(Date date) {
        return isSameYear(date, Calendar.getInstance().getTime());
    }

    /**
     * 判断给定的日历对象是否表示当前年份。
     *
     * @param calendar 日历对象
     * @return 如果是在当前年份则返回 true，否则返回 false
     */
    public static boolean isCurrentYear(Calendar calendar) {
        return isSameYear(calendar, Calendar.getInstance());
    }


    /**
     * 用于格式化日期的接口，以便在显示之前对日期进行格式化。
     * 例如，在对话框中显示的时间、消息的日期头等。
     */
    public interface Formatter {

        /**
         * 将日期对象格式化为字符串表示形式。
         *
         * @param date 需要被格式化的日期。
         * @return 格式化后的文本。
         */
        String format(Date date);
    }


    /**
     * 枚举类模板提供了几个预定义的日期和时间格式模板。
     * 这个类的目的是维护共同的日期和时间格式字符串，方便他们在整个程序中重用。
     */
    public enum Template {
        // 日期格式模板，包括日、月和年
        STRING_DAY_MONTH_YEAR("d MMMM yyyy"),
        // 日期格式模板，包括日期和月份
        STRING_DAY_MONTH("d MMMM"),
        // 时间格式模板，包括小时和分钟
        TIME("HH:mm");

        /**
         * 用于存储特定格式字符串的私有字段
         */
        private String template;

        /**
         * 枚举类的构造函数，用于初始化格式字符串。
         *
         * @param template 枚举常量要使用的格式字符串，指定应如何显示日期或时间。
         */
        Template(String template) {
            this.template = template;
        }

        /**
         * 获取格式字符串的方法。
         *
         * @return 返回枚举常量的格式字符串，可用于设置日期或时间的格式。
         */
        public String get() {
            return template;
        }
    }
}
