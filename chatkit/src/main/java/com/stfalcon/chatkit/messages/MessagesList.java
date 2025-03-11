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

package com.stfalcon.chatkit.messages;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.stfalcon.chatkit.commons.models.IMessage;

/**
 * 消息列表的显示组件
 */
public class MessagesList extends RecyclerView {
    // 消息列表的样式
    private MessagesListStyle messagesListStyle;

    /**
     * 构造函数，仅使用上下文初始化MessagesList
     *
     * @param context 应用程序上下文
     */
    public MessagesList(Context context) {
        super(context);
    }

    /**
     * 构造函数，使用上下文和属性集初始化MessagesList，并解析样式
     *
     * @param context 应用程序上下文
     * @param attrs   属性集
     */
    public MessagesList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 解析样式
        parseStyle(context, attrs);
    }

    /**
     * 构造函数，使用上下文、属性集和默认样式初始化MessagesList，并解析样式
     *
     * @param context  应用程序上下文
     * @param attrs    属性集
     * @param defStyle 默认样式
     */
    public MessagesList(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 解析样式
        parseStyle(context, attrs);
    }

    /**
     * 重写setAdapter方法，防止不正确的适配器设置
     *
     * @param adapter 适配器
     * @throws IllegalArgumentException 尝试设置适配器时抛出异常，提示使用正确的方法
     */
    @Override
    public void setAdapter(Adapter adapter) {
        throw new IllegalArgumentException("You can't set adapter to MessagesList. Use #setAdapter(MessagesListAdapter) instead.");
    }

    /**
     * 为MessagesList设置适配器
     *
     * @param adapter   适配器，必须扩展MessagesListAdapter
     * @param <MESSAGE> 消息模型类
     */
    public <MESSAGE extends IMessage>
    void setAdapter(MessagesListAdapter<MESSAGE> adapter) {
        // 调用重载的方法，设置适配器并使用默认的布局方向
        setAdapter(adapter, true);
    }

    /**
     * 为MessagesList设置适配器，并允许指定布局管理器的布局方向
     *
     * @param adapter       适配器，必须扩展MessagesListAdapter
     * @param reverseLayout 是否使用反向布局
     * @param <MESSAGE>     消息模型类
     */
    public <MESSAGE extends IMessage>
    void setAdapter(MessagesListAdapter<MESSAGE> adapter, boolean reverseLayout) {
        // 禁用动画效果
        SimpleItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);

        // 根据reverseLayout参数设置布局方向
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, reverseLayout);

        // 设置动画器、布局管理器和适配器的样式
        setItemAnimator(itemAnimator);
        setLayoutManager(layoutManager);
        adapter.setLayoutManager(layoutManager);
        adapter.setStyle(messagesListStyle);

        // 添加滚动监听器
        addOnScrollListener(new RecyclerScrollMoreListener(layoutManager, adapter));
        super.setAdapter(adapter);
    }

    /**
     * 解析自定义样式
     *
     * @param context 上下文
     * @param attrs   属性集合
     */
    @SuppressWarnings("ResourceType")
    private void parseStyle(Context context, AttributeSet attrs) {
        // 解析并应用自定义样式
        messagesListStyle = MessagesListStyle.parse(context, attrs);
    }
}

