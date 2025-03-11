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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import com.stfalcon.chatkit.R;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.ViewHolder;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Adapter for {@link MessagesList}.
 */
@SuppressWarnings("WeakerAccess")
public class MessagesListAdapter<MESSAGE extends IMessage>
        extends RecyclerView.Adapter<ViewHolder>
        implements RecyclerScrollMoreListener.OnLoadMoreListener {

    protected static boolean isSelectionModeEnabled;

    protected List<Wrapper> items;
    private MessageHolders holders;
    private String senderId;

    private int selectedItemsCount;
    private SelectionListener selectionListener;

    private OnLoadMoreListener loadMoreListener;
    private OnMessageClickListener<MESSAGE> onMessageClickListener;
    private OnMessageViewClickListener<MESSAGE> onMessageViewClickListener;
    private OnMessageLongClickListener<MESSAGE> onMessageLongClickListener;
    private OnMessageViewLongClickListener<MESSAGE> onMessageViewLongClickListener;
    private ImageLoader imageLoader;
    private RecyclerView.LayoutManager layoutManager;
    private MessagesListStyle messagesListStyle;
    private DateFormatter.Formatter dateHeadersFormatter;
    private SparseArray<OnMessageViewClickListener> viewClickListenersArray = new SparseArray<>();

    /**
     * 构造函数，用于创建 MessagesListAdapter 的实例，使用默认的列表项布局和视图保持器。
     *
     * @param senderId    发送者ID。
     * @param imageLoader 图片加载方法。
     */
    public MessagesListAdapter(String senderId, ImageLoader imageLoader) {
        this(senderId, new MessageHolders(), imageLoader);
    }

    /**
     * 构造函数，用于创建 MessagesListAdapter 的实例，允许自定义列表项布局和视图保持器。
     *
     * @param senderId    发送者ID。
     * @param holders     自定义列表项布局和视图保持器。详见 {@link MessageHolders} 文档
     * @param imageLoader 图片加载方法。
     */
    public MessagesListAdapter(String senderId, MessageHolders holders,
                               ImageLoader imageLoader) {
        this.senderId = senderId;
        this.holders = holders;
        this.imageLoader = imageLoader;
        this.items = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return holders.getHolder(parent, viewType, messagesListStyle);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Wrapper wrapper = items.get(position);
        holders.bind(holder, wrapper.item, wrapper.isSelected, imageLoader,
                getMessageClickListener(wrapper),
                getMessageLongClickListener(wrapper),
                dateHeadersFormatter,
                viewClickListenersArray);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return holders.getViewType(items.get(position).item, senderId);
    }

    @Override
    public void onLoadMore(int page, int total) {
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMore(page, total);
        }
    }

    @Override
    public int getMessagesCount() {
        int count = 0;
        for (Wrapper item : items) {
            if (item.item instanceof IMessage) {
                count++;
            }
        }
        return count;
    }

    // * 公共方法
    // *

    /**
     * 将消息添加到列表顶部并根据需要滚动。
     *
     * @param message 要添加的消息对象。
     * @param scroll  如果需要在添加消息后滚动列表到底部，则为true。
     */
    public void addToStart(MESSAGE message, boolean scroll) {
        boolean isNewMessageToday = !isPreviousSameDate(0, message.getCreatedAt());
        if (isNewMessageToday) {
            items.add(0, new Wrapper<>(message.getCreatedAt()));
        }
        Wrapper<MESSAGE> element = new Wrapper<>(message);
        items.add(0, element);
        notifyItemRangeInserted(0, isNewMessageToday ? 2 : 1);
        if (layoutManager != null && scroll) {
            layoutManager.scrollToPosition(0);
        }
    }

    /**
     * 按时间顺序添加消息列表。用于添加历史记录。
     *
     * @param messages 历史记录中的消息列表。
     * @param reverse  如果需要在添加前反转消息列表，则为true。
     */
    public void addToEnd(List<MESSAGE> messages, boolean reverse) {
        if (messages.isEmpty()) return;

        if (reverse) Collections.reverse(messages);

        if (!items.isEmpty()) {
            int lastItemPosition = items.size() - 1;
            Date lastItem = (Date) items.get(lastItemPosition).item;
            if (DateFormatter.isSameDay(messages.get(0).getCreatedAt(), lastItem)) {
                items.remove(lastItemPosition);
                notifyItemRemoved(lastItemPosition);
            }
        }

        int oldSize = items.size();
        generateDateHeaders(messages);
        notifyItemRangeInserted(oldSize, items.size() - oldSize);
    }

    /**
     * 根据消息ID更新消息。
     *
     * @param message 更新后的消息对象。
     * @return 如果成功更新则返回true，否则返回false。
     */
    public boolean update(MESSAGE message) {
        return update(message.getId(), message);
    }

    /**
     * 根据旧的标识符更新消息（如果ID已更改，请使用此方法）。否则使用 {@link #update(IMessage)} 方法。
     *
     * @param oldId      要更新的消息的旧标识符。
     * @param newMessage 新的消息对象。
     * @return 如果成功更新则返回true，否则返回false。
     */
    public boolean update(String oldId, MESSAGE newMessage) {
        int position = getMessagePositionById(oldId);
        if (position >= 0) {
            Wrapper<MESSAGE> element = new Wrapper<>(newMessage);
            items.set(position, element);
            notifyItemChanged(position);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将元素从当前位置移动到列表顶部。
     *
     * @param newMessage 新的消息对象。
     */
    public void updateAndMoveToStart(MESSAGE newMessage) {
        int position = getMessagePositionById(newMessage.getId());
        if (position >= 0) {
            Wrapper<MESSAGE> element = new Wrapper<>(newMessage);
            items.remove(position);
            items.add(0, element);
            notifyItemMoved(position, 0);
            notifyItemChanged(0);
        }
    }

    /**
     * 如果消息存在则更新，否则添加到列表顶部。
     *
     * @param message 要插入或更新的消息对象。
     */
    public void upsert(MESSAGE message) {
        if (!update(message)) {
            addToStart(message, false);
        }
    }

    /**
     * 如果消息存在则更新并根据指定是否移动到列表顶部；如果不存在则插入。
     *
     * @param message             要插入或更新的消息对象。
     * @param moveToStartIfUpdate 如果更新时是否将消息移动到列表顶部。
     */
    public void upsert(MESSAGE message, boolean moveToStartIfUpdate) {
        if (moveToStartIfUpdate) {
            if (getMessagePositionById(message.getId()) > 0) {
                updateAndMoveToStart(message);
            } else {
                upsert(message);
            }
        } else {
            upsert(message);
        }
    }


        /**
     * 删除单条消息。
     *
     * @param message 要删除的消息对象。
     */
    public void delete(MESSAGE message) {
        deleteById(message.getId());
    }

    /**
     * 批量删除消息列表。
     *
     * @param messages 要删除的消息列表。
     */
    public void delete(List<MESSAGE> messages) {
        boolean result = false;
        for (MESSAGE message : messages) {
            int index = getMessagePositionById(message.getId());
            if (index >= 0) {
                items.remove(index);
                notifyItemRemoved(index);
                result = true;
            }
        }
        if (result) {
            recountDateHeaders();
        }
    }

    /**
     * 根据消息ID删除消息。
     *
     * @param id 消息的唯一标识符。
     */
    public void deleteById(String id) {
        int index = getMessagePositionById(id);
        if (index >= 0) {
            items.remove(index);
            notifyItemRemoved(index);
            recountDateHeaders();
        }
    }

    /**
     * 根据多个消息ID批量删除消息。
     *
     * @param ids 要删除的消息ID数组。
     */
    public void deleteByIds(String[] ids) {
        boolean result = false;
        for (String id : ids) {
            int index = getMessagePositionById(id);
            if (index >= 0) {
                items.remove(index);
                notifyItemRemoved(index);
                result = true;
            }
        }
        if (result) {
            recountDateHeaders();
        }
    }

    /**
     * 判断消息列表是否为空。
     *
     * @return 如果消息数量为零则返回true，否则返回false。
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * 清空消息列表，并通知数据变化。
     */
    public void clear() {
        clear(true);
    }

    /**
     * 清空消息列表。
     *
     * @param notifyDataSetChanged 是否通知数据集变化。
     */
    public void clear(boolean notifyDataSetChanged) {
        if (items != null) {
            items.clear();
            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 启用选择模式。
     *
     * @param selectionListener 选择项变化监听器。
     */
    public void enableSelectionMode(SelectionListener selectionListener) {
        if (selectionListener == null) {
            throw new IllegalArgumentException("SelectionListener must not be null. Use `disableSelectionMode()` if you want to disable selection mode");
        } else {
            this.selectionListener = selectionListener;
        }
    }

    /**
     * 禁用选择模式并移除选择项监听器。
     */
    public void disableSelectionMode() {
        this.selectionListener = null;
        unselectAllItems();
    }


    /**
     * 返回已选择的消息列表。
     *
     * @return 已选择的消息列表。如果没有选择任何消息或选择模式已禁用，则返回空列表。
     */
    @SuppressWarnings("unchecked")
    public ArrayList<MESSAGE> getSelectedMessages() {
        ArrayList<MESSAGE> selectedMessages = new ArrayList<>();
        for (Wrapper wrapper : items) {
            if (wrapper.item instanceof IMessage && wrapper.isSelected) {
                selectedMessages.add((MESSAGE) wrapper.item);
            }
        }
        return selectedMessages;
    }

    /**
     * 返回已选择消息的文本，并为您调用 {@link #unselectAllItems()}。
     *
     * @param formatter 格式化器，允许您在复制时格式化消息模型。
     * @param reverse   复制消息时更改顺序。
     * @return 通过 {@link Formatter} 格式化的文本。如果为 {@code null}，则使用 {@code MESSAGE#toString()}。
     */
    public String getSelectedMessagesText(Formatter<MESSAGE> formatter, boolean reverse) {
        String copiedText = getSelectedText(formatter, reverse);
        unselectAllItems();
        return copiedText;
    }

    /**
     * 将文本复制到设备的剪贴板，并返回已选择消息的文本。同时为您调用 {@link #unselectAllItems()}。
     *
     * @param context   上下文。
     * @param formatter 格式化器，允许您在复制时格式化消息模型。
     * @param reverse   复制消息时更改顺序。
     * @return 通过 {@link Formatter} 格式化的文本。如果为 {@code null}，则使用 {@code MESSAGE#toString()}。
     */
    public String copySelectedMessagesText(Context context, Formatter<MESSAGE> formatter, boolean reverse) {
        String copiedText = getSelectedText(formatter, reverse);
        copyToClipboard(context, copiedText);
        unselectAllItems();
        return copiedText;
    }

    /**
     * 取消选择所有已选择的消息。使用零计数通知 {@link SelectionListener}。
     */
    public void unselectAllItems() {
        for (int i = 0; i < items.size(); i++) {
            Wrapper wrapper = items.get(i);
            if (wrapper.isSelected) {
                wrapper.isSelected = false;
                notifyItemChanged(i);
            }
        }
        isSelectionModeEnabled = false;
        selectedItemsCount = 0;
        notifySelectionChanged();
    }

    /**
     * 删除所有已选择的消息并禁用选择模式。
     * 在调用此方法之前，请先调用 {@link #getSelectedMessages()} 从您的数据源中删除消息。
     */
    public void deleteSelectedMessages() {
        List<MESSAGE> selectedMessages = getSelectedMessages();
        delete(selectedMessages);
        unselectAllItems();
    }


    /**
     * 设置项的点击监听器。仅在列表不在选择模式下时触发。
     *
     * @param onMessageClickListener 点击监听器。
     */
    public void setOnMessageClickListener(OnMessageClickListener<MESSAGE> onMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener;
    }

    /**
     * 设置消息视图的点击监听器。仅在列表不在选择模式下时触发。
     *
     * @param onMessageViewClickListener 点击监听器。
     */
    public void setOnMessageViewClickListener(OnMessageViewClickListener<MESSAGE> onMessageViewClickListener) {
        this.onMessageViewClickListener = onMessageViewClickListener;
    }

    /**
     * 按视图ID注册点击监听器。
     *
     * @param viewId                     视图ID。
     * @param onMessageViewClickListener 点击监听器。
     */
    public void registerViewClickListener(int viewId, OnMessageViewClickListener<MESSAGE> onMessageViewClickListener) {
        this.viewClickListenersArray.append(viewId, onMessageViewClickListener);
    }

    /**
     * 设置项的长按监听器。仅在选择模式禁用时触发。
     *
     * @param onMessageLongClickListener 长按监听器。
     */
    public void setOnMessageLongClickListener(OnMessageLongClickListener<MESSAGE> onMessageLongClickListener) {
        this.onMessageLongClickListener = onMessageLongClickListener;
    }

    /**
     * 设置消息视图的长按监听器。仅在选择模式禁用时触发。
     *
     * @param onMessageViewLongClickListener 长按监听器。
     */
    public void setOnMessageViewLongClickListener(OnMessageViewLongClickListener<MESSAGE> onMessageViewLongClickListener) {
        this.onMessageViewLongClickListener = onMessageViewLongClickListener;
    }

    /**
     * 设置回调，当列表滚动到顶部时调用。
     *
     * @param loadMoreListener 监听器。
     */
    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    /**
     * 设置自定义的日期头格式化器，用于日期头的文字表示。
     */
    public void setDateHeadersFormatter(DateFormatter.Formatter dateHeadersFormatter) {
        this.dateHeadersFormatter = dateHeadersFormatter;
    }


    /*
     * PRIVATE METHODS
     * */
    private void recountDateHeaders() {
        List<Integer> indicesToDelete = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            Wrapper wrapper = items.get(i);
            if (wrapper.item instanceof Date) {
                if (i == 0) {
                    indicesToDelete.add(i);
                } else {
                    if (items.get(i - 1).item instanceof Date) {
                        indicesToDelete.add(i);
                    }
                }
            }
        }

        Collections.reverse(indicesToDelete);
        for (int i : indicesToDelete) {
            items.remove(i);
            notifyItemRemoved(i);
        }
    }

    protected void generateDateHeaders(List<MESSAGE> messages) {
        for (int i = 0; i < messages.size(); i++) {
            MESSAGE message = messages.get(i);
            this.items.add(new Wrapper<>(message));
            if (messages.size() > i + 1) {
                MESSAGE nextMessage = messages.get(i + 1);
                if (!DateFormatter.isSameDay(message.getCreatedAt(), nextMessage.getCreatedAt())) {
                    this.items.add(new Wrapper<>(message.getCreatedAt()));
                }
            } else {
                this.items.add(new Wrapper<>(message.getCreatedAt()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private int getMessagePositionById(String id) {
        for (int i = 0; i < items.size(); i++) {
            Wrapper wrapper = items.get(i);
            if (wrapper.item instanceof IMessage) {
                MESSAGE message = (MESSAGE) wrapper.item;
                if (message.getId().contentEquals(id)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    private boolean isPreviousSameDate(int position, Date dateToCompare) {
        if (items.size() <= position) return false;
        if (items.get(position).item instanceof IMessage) {
            Date previousPositionDate = ((MESSAGE) items.get(position).item).getCreatedAt();
            return DateFormatter.isSameDay(dateToCompare, previousPositionDate);
        } else return false;
    }

    @SuppressWarnings("unchecked")
    private boolean isPreviousSameAuthor(String id, int position) {
        int prevPosition = position + 1;
        if (items.size() <= prevPosition) return false;
        else return items.get(prevPosition).item instanceof IMessage
                && ((MESSAGE) items.get(prevPosition).item).getUser().getId().contentEquals(id);
    }

    private void incrementSelectedItemsCount() {
        selectedItemsCount++;
        notifySelectionChanged();
    }

    private void decrementSelectedItemsCount() {
        selectedItemsCount--;
        isSelectionModeEnabled = selectedItemsCount > 0;

        notifySelectionChanged();
    }

    private void notifySelectionChanged() {
        if (selectionListener != null) {
            selectionListener.onSelectionChanged(selectedItemsCount);
        }
    }

    private void notifyMessageClicked(MESSAGE message) {
        if (onMessageClickListener != null) {
            onMessageClickListener.onMessageClick(message);
        }
    }

    private void notifyMessageViewClicked(View view, MESSAGE message) {
        if (onMessageViewClickListener != null) {
            onMessageViewClickListener.onMessageViewClick(view, message);
        }
    }

    private void notifyMessageLongClicked(MESSAGE message) {
        if (onMessageLongClickListener != null) {
            onMessageLongClickListener.onMessageLongClick(message);
        }
    }

    private void notifyMessageViewLongClicked(View view, MESSAGE message) {
        if (onMessageViewLongClickListener != null) {
            onMessageViewLongClickListener.onMessageViewLongClick(view, message);
        }
    }

    private View.OnClickListener getMessageClickListener(final Wrapper<MESSAGE> wrapper) {
        return view -> {
            if (selectionListener != null && isSelectionModeEnabled) {
                wrapper.isSelected = !wrapper.isSelected;

                if (wrapper.isSelected) incrementSelectedItemsCount();
                else decrementSelectedItemsCount();

                MESSAGE message = (wrapper.item);
                notifyItemChanged(getMessagePositionById(message.getId()));
            } else {
                notifyMessageClicked(wrapper.item);
                notifyMessageViewClicked(view, wrapper.item);
            }
        };
    }

    private View.OnLongClickListener getMessageLongClickListener(final Wrapper<MESSAGE> wrapper) {
        return view -> {
            if (selectionListener == null) {
                notifyMessageLongClicked(wrapper.item);
                notifyMessageViewLongClicked(view, wrapper.item);
            } else {
                isSelectionModeEnabled = true;
                view.performClick();
            }
            return true;
        };
    }

    private String getSelectedText(Formatter<MESSAGE> formatter, boolean reverse) {
        StringBuilder builder = new StringBuilder();

        ArrayList<MESSAGE> selectedMessages = getSelectedMessages();
        if (reverse) Collections.reverse(selectedMessages);

        for (MESSAGE message : selectedMessages) {
            builder.append(formatter == null
                    ? message.toString()
                    : formatter.format(message));
            builder.append("\n\n");
        }
        builder.replace(builder.length() - 2, builder.length(), "");

        return builder.toString();
    }

    private void copyToClipboard(Context context, String copiedText) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(copiedText, copiedText);
        clipboard.setPrimaryClip(clip);
    }

    void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    void setStyle(MessagesListStyle style) {
        this.messagesListStyle = style;
    }

    /*
     * WRAPPER
     * */
    public static class Wrapper<DATA> {
        public DATA item;
        public boolean isSelected;

        Wrapper(DATA item) {
            this.item = item;
        }
    }

    /*
     * LISTENERS
     * */

    /**
     * 加载更多监听器接口定义，当需要加载更多消息时会调用此回调。
     */
    public interface OnLoadMoreListener {

        /**
         * 加载更多数据的回调方法。
         *
         * @param page 当前页码，用于指示需要加载的下一部分数据。
         * @param totalItemsCount 到目前为止加载的总项数，帮助决定是否还有更多数据需要加载。
         */
        void onLoadMore(int page, int totalItemsCount);
    }

    /**
     * 选择变化监听器接口定义，当选择的项目数量发生变化时会调用此回调。
     */
    public interface SelectionListener {

        /**
         * 选择变化回调方法。
         *
         * @param count 当前选择的项目数量。
         */
        void onSelectionChanged(int count);
    }


    /**
     * 定义了一个回调接口，当消息项被点击时调用。
     */
    public interface OnMessageClickListener<MESSAGE extends IMessage> {

        /**
         * 当消息项被点击时触发。
         *
         * @param message 被点击的消息。
         */
        void onMessageClick(MESSAGE message);
    }

    /**
     * 定义了一个回调接口，当消息视图被点击时调用。
     */
    public interface OnMessageViewClickListener<MESSAGE extends IMessage> {

        /**
         * 当消息视图被点击时触发。
         *
         * @param view 被点击的视图。
         * @param message 被点击的消息。
         */
        void onMessageViewClick(View view, MESSAGE message);
    }

    /**
     * 定义了一个回调接口，当消息项被长按点击时调用。
     */
    public interface OnMessageLongClickListener<MESSAGE extends IMessage> {

        /**
         * 当消息项被长按时触发。
         *
         * @param message 被长按点击的消息。
         */
        void onMessageLongClick(MESSAGE message);
    }

    /**
     * 定义了一个回调接口，当消息视图被长按点击时调用。
     */
    public interface OnMessageViewLongClickListener<MESSAGE extends IMessage> {

        /**
         * 当消息视图被长按时触发。
         *
         * @param view 被长按点击的视图。
         * @param message 被长按点击的消息。
         */
        void onMessageViewLongClick(View view, MESSAGE message);
    }

    /**
     * 用于在复制时格式化消息模型的接口。
     */
    public interface Formatter<MESSAGE> {

        /**
         * 将消息对象格式化为字符串表示形式。
         *
         * @param message 需要格式化的对象。
         * @return 格式化后的文本。
         */
        String format(MESSAGE message);
    }


    /**
     * This class is deprecated. Use {@link MessageHolders} instead.
     */
    @Deprecated
    public static class HoldersConfig extends MessageHolders {

        /**
         * This method is deprecated. Use {@link MessageHolders#setIncomingTextConfig(Class, int)} instead.
         *
         * @param holder holder class.
         * @param layout layout resource.
         */
        @Deprecated
        public void setIncoming(Class<? extends BaseMessageViewHolder<? extends IMessage>> holder, @LayoutRes int layout) {
            super.setIncomingTextConfig(holder, layout);
        }

        /**
         * This method is deprecated. Use {@link MessageHolders#setIncomingTextHolder(Class)} instead.
         *
         * @param holder holder class.
         */
        @Deprecated
        public void setIncomingHolder(Class<? extends BaseMessageViewHolder<? extends IMessage>> holder) {
            super.setIncomingTextHolder(holder);
        }

        /**
         * This method is deprecated. Use {@link MessageHolders#setIncomingTextLayout(int)} instead.
         *
         * @param layout layout resource.
         */
        @Deprecated
        public void setIncomingLayout(@LayoutRes int layout) {
            super.setIncomingTextLayout(layout);
        }

        /**
         * This method is deprecated. Use {@link MessageHolders#setOutcomingTextConfig(Class, int)} instead.
         *
         * @param holder holder class.
         * @param layout layout resource.
         */
        @Deprecated
        public void setOutcoming(Class<? extends BaseMessageViewHolder<? extends IMessage>> holder, @LayoutRes int layout) {
            super.setOutcomingTextConfig(holder, layout);
        }

        /**
         * This method is deprecated. Use {@link MessageHolders#setOutcomingTextHolder(Class)} instead.
         *
         * @param holder holder class.
         */
        @Deprecated
        public void setOutcomingHolder(Class<? extends BaseMessageViewHolder<? extends IMessage>> holder) {
            super.setOutcomingTextHolder(holder);
        }

        /**
         * This method is deprecated. Use {@link MessageHolders#setOutcomingTextLayout(int)} instead.
         *
         * @param layout layout resource.
         */
        @Deprecated
        public void setOutcomingLayout(@LayoutRes int layout) {
            this.setOutcomingTextLayout(layout);
        }

        /**
         * This method is deprecated. Use {@link MessageHolders#setDateHeaderConfig(Class, int)} instead.
         *
         * @param holder holder class.
         * @param layout layout resource.
         */
        @Deprecated
        public void setDateHeader(Class<? extends ViewHolder<Date>> holder, @LayoutRes int layout) {
            super.setDateHeaderConfig(holder, layout);
        }
    }

    /**
     * This class is deprecated. Use {@link MessageHolders.BaseMessageViewHolder} instead.
     */
    @Deprecated
    public static abstract class BaseMessageViewHolder<MESSAGE extends IMessage>
            extends MessageHolders.BaseMessageViewHolder<MESSAGE> {

        private boolean isSelected;

        /**
         * Callback for implementing images loading in message list
         */
        protected ImageLoader imageLoader;

        public BaseMessageViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * Returns whether is item selected
         *
         * @return weather is item selected.
         */
        public boolean isSelected() {
            return isSelected;
        }

        /**
         * Returns weather is selection mode enabled
         *
         * @return weather is selection mode enabled.
         */
        public boolean isSelectionModeEnabled() {
            return isSelectionModeEnabled;
        }

        /**
         * Getter for {@link #imageLoader}
         *
         * @return image loader interface.
         */
        public ImageLoader getImageLoader() {
            return imageLoader;
        }

        protected void configureLinksBehavior(final TextView text) {
            text.setLinksClickable(false);
            text.setMovementMethod(new LinkMovementMethod() {
                @Override
                public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
                    boolean result = false;
                    if (!isSelectionModeEnabled) {
                        result = super.onTouchEvent(widget, buffer, event);
                    }
                    itemView.onTouchEvent(event);
                    return result;
                }
            });
        }

    }

    /**
     * This class is deprecated. Use {@link MessageHolders.DefaultDateHeaderViewHolder} instead.
     */
    @Deprecated
    public static class DefaultDateHeaderViewHolder extends ViewHolder<Date>
            implements MessageHolders.DefaultMessageViewHolder {

        protected TextView text;
        protected String dateFormat;
        protected DateFormatter.Formatter dateHeadersFormatter;

        public DefaultDateHeaderViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.messageText);
        }

        @Override
        public void onBind(Date date) {
            if (text != null) {
                String formattedDate = null;
                if (dateHeadersFormatter != null) formattedDate = dateHeadersFormatter.format(date);
                text.setText(formattedDate == null ? DateFormatter.format(date, dateFormat) : formattedDate);
            }
        }

        @Override
        public void applyStyle(MessagesListStyle style) {
            if (text != null) {
                text.setTextColor(style.getDateHeaderTextColor());
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getDateHeaderTextSize());
                text.setTypeface(text.getTypeface(), style.getDateHeaderTextStyle());
                text.setPadding(style.getDateHeaderPadding(), style.getDateHeaderPadding(),
                        style.getDateHeaderPadding(), style.getDateHeaderPadding());
            }
            dateFormat = style.getDateHeaderFormat();
            dateFormat = dateFormat == null ? DateFormatter.Template.STRING_DAY_MONTH_YEAR.get() : dateFormat;
        }
    }

    /**
     * This class is deprecated. Use {@link MessageHolders.IncomingTextMessageViewHolder} instead.
     */
    @Deprecated
    public static class IncomingMessageViewHolder<MESSAGE extends IMessage>
            extends MessageHolders.IncomingTextMessageViewHolder<MESSAGE>
            implements MessageHolders.DefaultMessageViewHolder {

        public IncomingMessageViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * This class is deprecated. Use {@link MessageHolders.OutcomingTextMessageViewHolder} instead.
     */
    @Deprecated
    public static class OutcomingMessageViewHolder<MESSAGE extends IMessage>
            extends MessageHolders.OutcomingTextMessageViewHolder<MESSAGE> {

        public OutcomingMessageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
