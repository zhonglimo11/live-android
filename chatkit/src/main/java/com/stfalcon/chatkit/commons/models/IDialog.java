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

import java.util.List;

/**
 * 对话模型接口，用于被实际的对话模型实现
 * 该接口定义了获取和设置对话相关的信息的方法，包括对话的唯一标识，
 * 对话头像，对话名称，参与者列表，最后一条消息以及未读消息数量
 *
 * @param <MESSAGE> 扩展了IMessage接口的消息类型，允许不同的对话模型使用特定的消息类型
 */
public interface IDialog<MESSAGE extends IMessage> {

    /**
     * 获取对话的唯一标识符
     *
     * @return 对话的唯一标识符
     */
    String getId();

    /**
     * 获取对话的头像URL
     *
     * @return 对话头像的URL
     */
    String getDialogPhoto();

    /**
     * 获取对话的名称
     *
     * @return 对话名称
     */
    String getDialogName();

    /**
     * 获取对话中的所有用户
     *
     * @return 用户列表，列表中的用户类型为实现了IUser接口的类
     */
    List<? extends IUser> getUsers();

    /**
     * 获取对话的最后一条消息
     *
     * @return 最后一条消息，消息类型为MESSAGE
     */
    MESSAGE getLastMessage();

    /**
     * 设置对话的最后一条消息
     *
     * @param message 要设置为最后一条消息的对象
     */
    void setLastMessage(MESSAGE message);

    /**
     * 获取对话中的未读消息数量
     *
     * @return 未读消息数量
     */
    int getUnreadCount();
}
