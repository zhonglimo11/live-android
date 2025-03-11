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

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 基础的ViewHolder类，用于RecyclerView中的数据绑定
 * 该类是一个抽象类，用于被具体的ViewHolder子类继承
 * 它定义了一个泛型DATA，用于表示绑定的数据类型
 */
public abstract class ViewHolder<DATA> extends RecyclerView.ViewHolder {

    /**
     * 将数据绑定到RecyclerView的项视图上
     * 子类需要实现这个方法以具体定义如何进行数据绑定
     *
     * @param data 要绑定的数据对象
     */
    public abstract void onBind(DATA data);

    /**
     * ViewHolder的构造方法
     * 初始化RecyclerView的项视图
     *
     * @param itemView RecyclerView的项视图
     */
    public ViewHolder(View itemView) {
        super(itemView);
    }

}

