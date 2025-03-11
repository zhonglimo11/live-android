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

package com.stfalcon.chatkit.commons;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.stfalcon.chatkit.R;

/**
 * 聊天组件样式的基类
 */
public abstract class Style {

    // 当前应用或组件的上下文，用于访问应用资源和类
    protected Context context;
    // 资源对象，用于获取各种资源
    protected Resources resources;
    // 属性集，通常用于从XML中读取属性
    protected AttributeSet attrs;

    /**
     * 构造方法初始化Style对象
     *
     * @param context 应用程序上下文
     * @param attrs   XML文件中的属性集合
     */
    protected Style(Context context, AttributeSet attrs) {
        this.context = context;
        this.resources = context.getResources();
        this.attrs = attrs;
    }

    /**
     * 获取系统强调色
     *
     * @return 颜色值
     */
    protected final int getSystemAccentColor() {
        return getSystemColor(R.attr.colorAccent);
    }

    /**
     * 获取系统主色调
     *
     * @return 颜色值
     */
    protected final int getSystemPrimaryColor() {
        return getSystemColor(R.attr.colorPrimary);
    }

    /**
     * 获取系统的深色主色调
     *
     * @return 颜色值
     */
    protected final int getSystemPrimaryDarkColor() {
        return getSystemColor(R.attr.colorPrimaryDark);
    }

    /**
     * 获取系统的主要文本颜色
     *
     * @return 颜色值
     */
    protected final int getSystemPrimaryTextColor() {
        return getSystemColor(android.R.attr.textColorPrimary);
    }

    /**
     * 获取系统的提示文本颜色
     *
     * @return 颜色值
     */
    protected final int getSystemHintColor() {
        return getSystemColor(android.R.attr.textColorHint);
    }

    /**
     * 根据给定的属性ID获取系统颜色
     *
     * @param attr 属性资源ID
     * @return 颜色值
     */
    protected final int getSystemColor(@AttrRes int attr) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{attr});
        int color = a.getColor(0, 0);
        a.recycle();

        return color;
    }

    /**
     * 根据给定的尺寸资源ID获取尺寸值
     *
     * @param dimen 尺寸资源ID
     * @return 尺寸值
     */
    protected final int getDimension(@DimenRes int dimen) {
        return resources.getDimensionPixelSize(dimen);
    }

    /**
     * 根据给定的颜色资源ID获取颜色值
     *
     * @param color 颜色资源ID
     * @return 颜色值
     */
    protected final int getColor(@ColorRes int color) {
        return ContextCompat.getColor(context, color);
    }

    /**
     * 根据给定的图片资源ID获取Drawable对象
     *
     * @param drawable 图片资源ID
     * @return Drawable对象
     */
    protected final Drawable getDrawable(@DrawableRes int drawable) {
        return ContextCompat.getDrawable(context, drawable);
    }

    /**
     * 根据给定的矢量图片资源ID获取Drawable对象
     *
     * @param drawable 矢量图片资源ID
     * @return Drawable对象
     */
    protected final Drawable getVectorDrawable(@DrawableRes int drawable) {
        return ContextCompat.getDrawable(context, drawable);
    }

}
