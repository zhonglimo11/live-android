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

package com.stfalcon.chatkit.dialogs;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.stfalcon.chatkit.commons.models.IDialog;

/**
 * 用于显示对话框列表的组件
 */
public class DialogsList extends RecyclerView {

    private DialogListStyle dialogStyle;

    public DialogsList(Context context) {
        super(context);
    }

    public DialogsList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parseStyle(context, attrs);
    }

    public DialogsList(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseStyle(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        SimpleItemAnimator animator = new DefaultItemAnimator();

        setLayoutManager(layout);
        setItemAnimator(animator);
    }

    /**
     * 禁止使用此方法设置适配器，否则会抛出异常。
     * 请改用 {@link #setAdapter(DialogsListAdapter)} 方法。
     */
    @Override
    public void setAdapter(Adapter adapter) {
        throw new IllegalArgumentException("You can't set adapter to DialogsList. Use #setAdapter(DialogsListAdapter) instead.");
    }

    /**
     * 为 DialogsList 设置适配器
     *
     * @param adapter 适配器。必须扩展 DialogsListAdapter
     * @param <DIALOG> 对话模型类
     */
    public <DIALOG extends IDialog<?>>
    void setAdapter(DialogsListAdapter<DIALOG> adapter) {
        setAdapter(adapter, false);
    }

    /**
     * 为 DialogsList 设置适配器
     *
     * @param adapter       适配器。必须扩展 DialogsListAdapter
     * @param reverseLayout 是否使用反向布局用于布局管理器。
     * @param <DIALOG>      对话模型类
     */
    public <DIALOG extends IDialog<?>>
    void setAdapter(DialogsListAdapter<DIALOG> adapter, boolean reverseLayout) {
        // 禁用动画以提高性能
        SimpleItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);

        // 使用 LinearLayoutManager 来管理对话列表的布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, reverseLayout);

        // 设置列表的 ItemAnimator 和 LayoutManager
        setItemAnimator(itemAnimator);
        setLayoutManager(layoutManager);

        // 应用对话样式
        adapter.setStyle(dialogStyle);

        // 调用父类的 setAdapter 方法来设置适配器
        super.setAdapter(adapter);
    }


    @SuppressWarnings("ResourceType")
    private void parseStyle(Context context, AttributeSet attrs) {
        dialogStyle = DialogListStyle.parse(context, attrs);
    }
}
