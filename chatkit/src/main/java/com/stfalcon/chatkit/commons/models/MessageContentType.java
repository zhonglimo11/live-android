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

package com.stfalcon.chatkit.commons.models;

import androidx.annotation.Nullable;

import com.stfalcon.chatkit.messages.MessageHolders;

/*
 * Created by troy379 on 28.03.17.
 */

/**
 * 用于将消息标记为自定义内容类型的接口。其表示形式请参见{@link MessageHolders}
 */
public interface MessageContentType extends IMessage {

    /**
     * 图片消息的默认媒体类型。
     */
    interface Image extends IMessage {
        /**
         * 获取图片的URL地址。
         *
         * @return 图片的URL地址，可能为null。
         */
        @Nullable
        String getImageUrl();
    }

    // 其他默认类型将在此处定义
}

