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

/**
 * 用户模型接口，用于实现具体的用户模型
 */
public interface IUser {

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    String getId();

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    String getName();

    /**
     * 获取用户头像的图片URL
     *
     * @return 用户头像的图片URL
     */
    String getAvatar();
}
