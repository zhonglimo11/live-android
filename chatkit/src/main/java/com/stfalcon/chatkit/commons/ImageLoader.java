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

import android.widget.ImageView;

import androidx.annotation.Nullable;

/**
 * 图片加载接口，用于在消息列表中实现图片的异步加载功能
 */
public interface ImageLoader {

    /**
     * 加载图片方法
     *
     * @param imageView 所需ImageView对象，用于将加载的图片显示出来
     * @param url 图片的URL地址，可能为null
     * @param payload 附加数据，可以是用于复用Bitmap等目的的对象，可能为null
     */
    void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload);

}

