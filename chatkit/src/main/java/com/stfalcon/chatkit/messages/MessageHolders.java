package com.stfalcon.chatkit.messages;

import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import com.stfalcon.chatkit.R;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.ViewHolder;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.stfalcon.chatkit.utils.RoundedImageView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@SuppressWarnings("WeakerAccess")
public class MessageHolders {

    /**
     * 视图类型：日期头部
     */
    private static final short VIEW_TYPE_DATE_HEADER = 130;
    /**
     * 视图类型：文本消息
     */
    private static final short VIEW_TYPE_TEXT_MESSAGE = 131;
    /**
     * 视图类型：图片消息
     */
    private static final short VIEW_TYPE_IMAGE_MESSAGE = 132;

    /**
     * 日期头部的视图持有者类类型
     */
    private Class<? extends ViewHolder<Date>> dateHeaderHolder;
    /**
     * 日期头部的布局资源ID
     */
    private int dateHeaderLayout;

    /**
     * 接收的文本消息配置
     */
    private HolderConfig<IMessage> incomingTextConfig;
    /**
     * 发送的文本消息配置
     */
    private HolderConfig<IMessage> outcomingTextConfig;
    /**
     * 接收的图片消息配置
     */
    private HolderConfig<MessageContentType.Image> incomingImageConfig;
    /**
     * 发送的图片消息配置
     */
    private HolderConfig<MessageContentType.Image> outcomingImageConfig;

    /**
     * 自定义内容类型配置列表
     */
    private List<ContentTypeConfig> customContentTypes = new ArrayList<>();
    /**
     * 内容检查器，用于判断消息的内容类型
     */
    private ContentChecker contentChecker;

    /**
     * 默认构造函数，初始化消息持有者的配置
     */
    public MessageHolders() {
        this.dateHeaderHolder = DefaultDateHeaderViewHolder.class;
        this.dateHeaderLayout = R.layout.item_date_header;

        this.incomingTextConfig = new HolderConfig<>(DefaultIncomingTextMessageViewHolder.class, R.layout.item_incoming_text_message);
        this.outcomingTextConfig = new HolderConfig<>(DefaultOutcomingTextMessageViewHolder.class, R.layout.item_outcoming_text_message);
        this.incomingImageConfig = new HolderConfig<>(DefaultIncomingImageMessageViewHolder.class, R.layout.item_incoming_image_message);
        this.outcomingImageConfig = new HolderConfig<>(DefaultOutcomingImageMessageViewHolder.class, R.layout.item_outcoming_image_message);
    }

    /**
     * 设置接收的文本消息的视图持有者类类型和布局资源ID
     *
     * @param holder 视图持有者类类型
     * @param layout 布局资源ID
     * @return 当前MessageHolders实例，以便进行链式调用
     */
    public MessageHolders setIncomingTextConfig(
            @NonNull Class<? extends BaseMessageViewHolder<? extends IMessage>> holder,
            @LayoutRes int layout) {
        this.incomingTextConfig.holder = holder;
        this.incomingTextConfig.layout = layout;
        return this;
    }

    /**
     * 设置接收的文本消息的视图持有者类类型、布局资源ID和附加数据
     *
     * @param holder  视图持有者类类型
     * @param layout  布局资源ID
     * @param payload 附加数据，用于ViewHolder的复用判断
     * @return 当前MessageHolders实例，以便进行链式调用
     */
    public MessageHolders setIncomingTextConfig(
            @NonNull Class<? extends BaseMessageViewHolder<? extends IMessage>> holder,
            @LayoutRes int layout,
            Object payload) {
        this.incomingTextConfig.holder = holder;
        this.incomingTextConfig.layout = layout;
        this.incomingTextConfig.payload = payload;
        return this;
    }

    /**
     * 设置接收的文本消息的视图持有者类类型
     *
     * @param holder 视图持有者类类型
     * @return 当前MessageHolders实例，以便进行链式调用
     */
    public MessageHolders setIncomingTextHolder(
            @NonNull Class<? extends BaseMessageViewHolder<? extends IMessage>> holder) {
        this.incomingTextConfig.holder = holder;
        return this;
    }

    /**
     * 设置接收的文本消息的视图持有者类类型和附加数据
     *
     * @param holder  视图持有者类类型
     * @param payload 附加数据，用于ViewHolder的复用判断
     * @return 当前MessageHolders实例，以便进行链式调用
     */
    public MessageHolders setIncomingTextHolder(
            @NonNull Class<? extends BaseMessageViewHolder<? extends IMessage>> holder,
            Object payload) {
        this.incomingTextConfig.holder = holder;
        this.incomingTextConfig.payload = payload;
        return this;
    }

    /**
     * 设置接收的文本消息的布局资源ID
     *
     * @param layout 布局资源ID
     * @return 当前MessageHolders实例，以便进行链式调用
     */
    public MessageHolders setIncomingTextLayout(@LayoutRes int layout) {
        this.incomingTextConfig.layout = layout;
        return this;
    }

    /**
     * 设置接收的文本消息的布局资源ID和附加数据
     *
     * @param layout  布局资源ID
     * @param payload 附加数据，用于ViewHolder的复用判断
     * @return 当前MessageHolders实例，以便进行链式调用
     */
    public MessageHolders setIncomingTextLayout(@LayoutRes int layout, Object payload) {
        this.incomingTextConfig.layout = layout;
        this.incomingTextConfig.payload = payload;
        return this;
    }

    /**
     * 设置发送的文本消息的视图持有者类类型和布局资源ID
     *
     * @param holder 视图持有者类类型
     * @param layout 布局资源ID
     * @return 当前MessageHolders实例，以便进行链式调用
     */
    public MessageHolders setOutcomingTextConfig(
            @NonNull Class<? extends BaseMessageViewHolder<? extends IMessage>> holder,
            @LayoutRes int layout) {
        this.outcomingTextConfig.holder = holder;
        this.outcomingTextConfig.layout = layout;
        return this;
    }


    /**
     * 为传出的文本消息同时设置自定义视图持有者类、布局资源和有效载荷。
     *
     * @param holder  视图持有者类
     * @param layout  布局资源ID
     * @param payload 自定义数据
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setOutcomingTextConfig(
            @NonNull Class<? extends BaseMessageViewHolder<? extends IMessage>> holder,
            @LayoutRes int layout,
            Object payload) {
        this.outcomingTextConfig.holder = holder;
        this.outcomingTextConfig.layout = layout;
        this.outcomingTextConfig.payload = payload;
        return this;
    }

    /**
     * 为传出的文本消息设置自定义视图持有者类。
     *
     * @param holder 视图持有者类
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setOutcomingTextHolder(
            @NonNull Class<? extends BaseMessageViewHolder<? extends IMessage>> holder) {
        this.outcomingTextConfig.holder = holder;
        return this;
    }

    /**
     * 为传出的文本消息设置自定义视图持有者类及有效载荷。
     *
     * @param holder  视图持有者类
     * @param payload 自定义数据
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setOutcomingTextHolder(
            @NonNull Class<? extends BaseMessageViewHolder<? extends IMessage>> holder,
            Object payload) {
        this.outcomingTextConfig.holder = holder;
        this.outcomingTextConfig.payload = payload;
        return this;
    }

    /**
     * 为传出的文本消息设置自定义布局资源。
     *
     * @param layout 布局资源ID
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setOutcomingTextLayout(@LayoutRes int layout) {
        this.outcomingTextConfig.layout = layout;
        return this;
    }

    /**
     * 为传出的文本消息设置自定义布局资源及有效载荷。
     *
     * @param layout  布局资源ID
     * @param payload 自定义数据
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setOutcomingTextLayout(@LayoutRes int layout, Object payload) {
        this.outcomingTextConfig.layout = layout;
        this.outcomingTextConfig.payload = payload;
        return this;
    }


    /**
     * 为传入的图像消息同时设置自定义视图持有者类和布局资源。
     *
     * @param holder 视图持有者类
     * @param layout 布局资源ID
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setIncomingImageConfig(
            @NonNull Class<? extends BaseMessageViewHolder<? extends MessageContentType.Image>> holder,
            @LayoutRes int layout) {
        this.incomingImageConfig.holder = holder;
        this.incomingImageConfig.layout = layout;
        return this;
    }

    /**
     * 为传入的图像消息同时设置自定义视图持有者类、布局资源和有效载荷。
     *
     * @param holder  视图持有者类
     * @param layout  布局资源ID
     * @param payload 自定义数据
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setIncomingImageConfig(
            @NonNull Class<? extends BaseMessageViewHolder<? extends MessageContentType.Image>> holder,
            @LayoutRes int layout,
            Object payload) {
        this.incomingImageConfig.holder = holder;
        this.incomingImageConfig.layout = layout;
        this.incomingImageConfig.payload = payload;
        return this;
    }

    /**
     * 为传入的图像消息设置自定义视图持有者类。
     *
     * @param holder 视图持有者类
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setIncomingImageHolder(
            @NonNull Class<? extends BaseMessageViewHolder<? extends MessageContentType.Image>> holder) {
        this.incomingImageConfig.holder = holder;
        return this;
    }

    /**
     * 为传入的图像消息设置自定义视图持有者类及有效载荷。
     *
     * @param holder  视图持有者类
     * @param payload 自定义数据
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setIncomingImageHolder(
            @NonNull Class<? extends BaseMessageViewHolder<? extends MessageContentType.Image>> holder,
            Object payload) {
        this.incomingImageConfig.holder = holder;
        this.incomingImageConfig.payload = payload;
        return this;
    }

    /**
     * 为传入的图像消息设置自定义布局资源。
     *
     * @param layout 布局资源ID
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setIncomingImageLayout(@LayoutRes int layout) {
        this.incomingImageConfig.layout = layout;
        return this;
    }


    /**
     * 为传入的图像消息设置自定义布局资源及有效载荷。
     *
     * @param layout  布局资源ID
     * @param payload 自定义数据
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setIncomingImageLayout(@LayoutRes int layout, Object payload) {
        this.incomingImageConfig.layout = layout;
        this.incomingImageConfig.payload = payload;
        return this;
    }

    /**
     * 为传出的图像消息同时设置自定义视图持有者类和布局资源。
     *
     * @param holder 视图持有者类
     * @param layout 布局资源ID
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setOutcomingImageConfig(
            @NonNull Class<? extends BaseMessageViewHolder<? extends MessageContentType.Image>> holder,
            @LayoutRes int layout) {
        this.outcomingImageConfig.holder = holder;
        this.outcomingImageConfig.layout = layout;
        return this;
    }

    /**
     * 为传出的图像消息同时设置自定义视图持有者类、布局资源和有效载荷。
     *
     * @param holder  视图持有者类
     * @param layout  布局资源ID
     * @param payload 自定义数据
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setOutcomingImageConfig(
            @NonNull Class<? extends BaseMessageViewHolder<? extends MessageContentType.Image>> holder,
            @LayoutRes int layout,
            Object payload) {
        this.outcomingImageConfig.holder = holder;
        this.outcomingImageConfig.layout = layout;
        this.outcomingImageConfig.payload = payload;
        return this;
    }

    /**
     * 为传出的图像消息设置自定义视图持有者类。
     *
     * @param holder 视图持有者类
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setOutcomingImageHolder(
            @NonNull Class<? extends BaseMessageViewHolder<? extends MessageContentType.Image>> holder) {
        this.outcomingImageConfig.holder = holder;
        return this;
    }

    /**
     * 为传出的图像消息设置自定义视图持有者类及有效载荷。
     *
     * @param holder  视图持有者类
     * @param payload 自定义数据
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setOutcomingImageHolder(
            @NonNull Class<? extends BaseMessageViewHolder<? extends MessageContentType.Image>> holder,
            Object payload) {
        this.outcomingImageConfig.holder = holder;
        this.outcomingImageConfig.payload = payload;
        return this;
    }


    /**
     * 为传出的图像消息设置自定义布局资源。
     *
     * @param layout 布局资源ID
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setOutcomingImageLayout(@LayoutRes int layout) {
        this.outcomingImageConfig.layout = layout;
        return this;
    }

    /**
     * 为传出的图像消息设置自定义布局资源及有效载荷。
     *
     * @param layout  布局资源ID
     * @param payload 自定义数据
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setOutcomingImageLayout(@LayoutRes int layout, Object payload) {
        this.outcomingImageConfig.layout = layout;
        this.outcomingImageConfig.payload = payload;
        return this;
    }

    /**
     * 为日期标题设置自定义视图持有者类和布局资源。
     *
     * @param holder 视图持有者类
     * @param layout 布局资源ID
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setDateHeaderConfig(
            @NonNull Class<? extends ViewHolder<Date>> holder,
            @LayoutRes int layout) {
        this.dateHeaderHolder = holder;
        this.dateHeaderLayout = layout;
        return this;
    }

    /**
     * 设置日期标题的自定义视图持有者类。
     *
     * @param holder 视图持有者类
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setDateHeaderHolder(@NonNull Class<? extends ViewHolder<Date>> holder) {
        this.dateHeaderHolder = holder;
        return this;
    }

    /**
     * 设置日期标题的自定义布局资源。
     *
     * @param layout 布局资源ID
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public MessageHolders setDateHeaderLayout(@LayoutRes int layout) {
        this.dateHeaderLayout = layout;
        return this;
    }


    /**
     * 注册自定义内容类型 (例如多媒体、事件等)
     *
     * @param type            内容类型的唯一标识
     * @param holder          传入消息和传出消息共用的消息视图持有者类
     * @param incomingLayout  传入消息的布局资源ID
     * @param outcomingLayout 传出消息的布局资源ID
     * @param contentChecker  用于验证已注册类型的消息内容检查器
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public <TYPE extends MessageContentType>
    MessageHolders registerContentType(
            byte type, @NonNull Class<? extends BaseMessageViewHolder<TYPE>> holder,
            @LayoutRes int incomingLayout,
            @LayoutRes int outcomingLayout,
            @NonNull ContentChecker contentChecker) {

        return registerContentType(type,
                holder, incomingLayout,
                holder, outcomingLayout,
                contentChecker);
    }

    /**
     * 注册自定义内容类型 (例如多媒体、事件等)
     *
     * @param type            内容类型的唯一标识
     * @param incomingHolder  传入消息的消息视图持有者类
     * @param outcomingHolder 传出消息的消息视图持有者类
     * @param incomingLayout  传入消息的布局资源ID
     * @param outcomingLayout 传出消息的布局资源ID
     * @param contentChecker  用于验证已注册类型的消息内容检查器
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public <TYPE extends MessageContentType>
    MessageHolders registerContentType(
            byte type,
            @NonNull Class<? extends BaseMessageViewHolder<TYPE>> incomingHolder, @LayoutRes int incomingLayout,
            @NonNull Class<? extends BaseMessageViewHolder<TYPE>> outcomingHolder, @LayoutRes int outcomingLayout,
            @NonNull ContentChecker contentChecker) {

        if (type == 0)
            throw new IllegalArgumentException("内容类型必须大于或小于 '0'!");

        customContentTypes.add(
                new ContentTypeConfig<>(type,
                        new HolderConfig<>(incomingHolder, incomingLayout),
                        new HolderConfig<>(outcomingHolder, outcomingLayout)));
        this.contentChecker = contentChecker;
        return this;
    }

    /**
     * 注册自定义内容类型 (例如多媒体、事件等)
     *
     * @param type             内容类型的唯一标识
     * @param incomingHolder   传入消息的消息视图持有者类
     * @param outcomingHolder  传出消息的消息视图持有者类
     * @param incomingPayload  传入消息的有效载荷
     * @param outcomingPayload 传出消息的有效载荷
     * @param incomingLayout   传入消息的布局资源ID
     * @param outcomingLayout  传出消息的布局资源ID
     * @param contentChecker   用于验证已注册类型的消息内容检查器
     * @return 返回一个 {@link MessageHolders} 实例，允许进行进一步的配置
     */
    public <TYPE extends MessageContentType>
    MessageHolders registerContentType(
            byte type,
            @NonNull Class<? extends MessageHolders.BaseMessageViewHolder<TYPE>> incomingHolder, Object incomingPayload, @LayoutRes int incomingLayout,
            @NonNull Class<? extends MessageHolders.BaseMessageViewHolder<TYPE>> outcomingHolder, Object outcomingPayload, @LayoutRes int outcomingLayout,
            @NonNull MessageHolders.ContentChecker contentChecker) {

        if (type == 0)
            throw new IllegalArgumentException("内容类型必须大于或小于 '0'!");

        customContentTypes.add(
                new MessageHolders.ContentTypeConfig<>(type,
                        new HolderConfig<>(incomingHolder, incomingLayout, incomingPayload),
                        new HolderConfig<>(outcomingHolder, outcomingLayout, outcomingPayload)));
        this.contentChecker = contentChecker;
        return this;
    }


    // * 接口
    // *

    /**
     * 接口，它包含用于检查内容可用性的逻辑。
     */
    public interface ContentChecker<MESSAGE extends IMessage> {

        /**
         * 检查内容的可用性。
         *
         * @param message 列表中的当前消息。
         * @param type    内容类型，为其确定内容可用性。
         * @return 如果内容可用，则返回 true；否则返回 false。
         */
        boolean hasContentFor(MESSAGE message, byte type);
    }


    // * 私有方法
    // *

    /**
     * 根据视图类型获取相应的ViewHolder
     *
     * @param parent            父容器，用于LayoutInflater inflate方法
     * @param viewType          视图类型，决定创建哪种类型的ViewHolder
     * @param messagesListStyle 消息列表样式配置
     * @return 根据视图类型创建的ViewHolder实例
     */
    protected ViewHolder getHolder(ViewGroup parent, int viewType, MessagesListStyle messagesListStyle) {
        switch (viewType) {
            case VIEW_TYPE_DATE_HEADER:
                // 日期头部视图类型的ViewHolder
                return getHolder(parent, dateHeaderLayout, dateHeaderHolder, messagesListStyle, null);
            case VIEW_TYPE_TEXT_MESSAGE:
                // 文本消息视图类型的ViewHolder（传入配置和样式）
                return getHolder(parent, incomingTextConfig, messagesListStyle);
            case -VIEW_TYPE_TEXT_MESSAGE:
                // 文本消息视图类型的ViewHolder（传入配置和样式）
                return getHolder(parent, outcomingTextConfig, messagesListStyle);
            case VIEW_TYPE_IMAGE_MESSAGE:
                // 图片消息视图类型的ViewHolder（传入配置和样式）
                return getHolder(parent, incomingImageConfig, messagesListStyle);
            case -VIEW_TYPE_IMAGE_MESSAGE:
                // 图片消息视图类型的ViewHolder（传入配置和样式）
                return getHolder(parent, outcomingImageConfig, messagesListStyle);
            default:
                // 遍历自定义视图类型配置
                for (ContentTypeConfig typeConfig : customContentTypes) {
                    if (Math.abs(typeConfig.type) == Math.abs(viewType)) {
                        if (viewType > 0)
                            // 根据自定义类型配置创建ViewHolder（传入配置和样式）
                            return getHolder(parent, typeConfig.incomingConfig, messagesListStyle);
                        else
                            // 根据自定义类型配置创建ViewHolder（传入配置和样式）
                            return getHolder(parent, typeConfig.outcomingConfig, messagesListStyle);
                    }
                }
        }
        // 如果没有匹配到任何视图类型，抛出异常
        throw new IllegalStateException("Wrong message view type. Please, report this issue on GitHub with full stacktrace in description.");
    }

    /**
     * 绑定ViewHolder到数据项
     *
     * @param holder                     绑定的ViewHolder实例
     * @param item                       数据项实例
     * @param isSelected                 是否选中
     * @param imageLoader                图片加载器
     * @param onMessageClickListener     消息点击监听器
     * @param onMessageLongClickListener 消息长按监听器
     * @param dateHeadersFormatter       日期头部格式化器
     * @param clickListenersArray        点击监听器数组
     */
    @SuppressWarnings("unchecked")
    protected void bind(final ViewHolder holder, final Object item, boolean isSelected,
                        final ImageLoader imageLoader,
                        final View.OnClickListener onMessageClickListener,
                        final View.OnLongClickListener onMessageLongClickListener,
                        final DateFormatter.Formatter dateHeadersFormatter,
                        final SparseArray<MessagesListAdapter.OnMessageViewClickListener> clickListenersArray) {

        if (item instanceof IMessage) {
            // 如果数据项是消息类型
            ((MessageHolders.BaseMessageViewHolder) holder).isSelected = isSelected;
            ((MessageHolders.BaseMessageViewHolder) holder).imageLoader = imageLoader;
            holder.itemView.setOnLongClickListener(onMessageLongClickListener);
            holder.itemView.setOnClickListener(onMessageClickListener);

            // 遍历点击监听器数组，为相应的视图设置点击事件
            for (int i = 0; i < clickListenersArray.size(); i++) {
                final int key = clickListenersArray.keyAt(i);
                final View view = holder.itemView.findViewById(key);
                if (view != null) {
                    view.setOnClickListener(v -> clickListenersArray.get(key).onMessageViewClick(view, (IMessage) item));
                }
            }
        } else if (item instanceof Date) {
            // 如果数据项是日期类型
            ((MessageHolders.DefaultDateHeaderViewHolder) holder).dateHeadersFormatter = dateHeadersFormatter;
        }

        // 调用ViewHolder的onBind方法进行最终绑定
        holder.onBind(item);
    }

    /**
     * 获取数据项的视图类型
     *
     * @param item     数据项实例
     * @param senderId 发送者ID，用于判断消息方向
     * @return 数据项对应的视图类型
     */
    protected int getViewType(Object item, String senderId) {
        boolean isOutcoming = false;
        int viewType;

        if (item instanceof IMessage) {
            // 如果数据项是消息类型
            IMessage message = (IMessage) item;
            isOutcoming = message.getUser().getId().contentEquals(senderId);
            viewType = getContentViewType(message);
        } else {
            // 如果数据项是日期类型
            viewType = VIEW_TYPE_DATE_HEADER;
        }

        // 根据消息方向调整视图类型
        return isOutcoming ? viewType * -1 : viewType;
    }

    /**
     * 根据给定的布局、持有者配置和样式获取 ViewHolder 实例
     * 此方法作为重载，提供了一种更简洁的方式创建 ViewHolder
     *
     * @param parent       父容器，用于 inflate 布局
     * @param holderConfig 持有者配置，包含了布局和持有者类信息
     * @param style        消息列表的样式
     * @return 创建的 ViewHolder 实例
     */
    private ViewHolder getHolder(ViewGroup parent, HolderConfig holderConfig,
                                 MessagesListStyle style) {
        return getHolder(parent, holderConfig.layout, holderConfig.holder, style, holderConfig.payload);
    }

    /**
     * 根据给定的布局、持有者类、样式和附加数据获取 ViewHolder 实例
     * 此方法负责实际的 ViewHolder 实例创建
     *
     * @param parent      父容器，用于 inflate 布局
     * @param layout      布局资源 ID
     * @param holderClass ViewHolder 的类，使用泛型指定
     * @param style       消息列表的样式
     * @param payload     附加数据，用于某些 ViewHolder 的初始化
     * @return 创建的 ViewHolder 实例
     * @throws UnsupportedOperationException 如果无法创建 ViewHolder 实例
     */
    private <HOLDER extends ViewHolder>
    ViewHolder getHolder(ViewGroup parent, @LayoutRes int layout, Class<HOLDER> holderClass,
                         MessagesListStyle style, Object payload) {

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        try {
            Constructor<HOLDER> constructor = null;
            HOLDER holder;
            try {
                // 尝试使用带附加数据的构造函数创建 ViewHolder
                constructor = holderClass.getDeclaredConstructor(View.class, Object.class);
                constructor.setAccessible(true);
                holder = constructor.newInstance(v, payload);
            } catch (NoSuchMethodException e) {
                // 如果上述构造函数不存在，尝试使用无参构造函数创建 ViewHolder
                constructor = holderClass.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                holder = constructor.newInstance(v);
            }
            // 如果创建的 ViewHolder 是 DefaultMessageViewHolder 的实例且样式不为空，则应用样式
            if (holder instanceof DefaultMessageViewHolder && style != null) {
                ((DefaultMessageViewHolder) holder).applyStyle(style);
            }
            return holder;
        } catch (Exception e) {
            // 如果创建 ViewHolder 时发生异常，抛出 UnsupportedOperationException
            throw new UnsupportedOperationException("Somehow we couldn't create the ViewHolder for message. Please, report this issue on GitHub with full stacktrace in description.", e);
        }
    }

    /**
     * 根据消息的类型确定视图类型（View Type）
     * 此方法用于将不同类型的 Message 映射到对应的视图类型
     *
     * @param message 消息对象，用于确定类型
     * @return 确定的视图类型（VIEW_TYPE_XXX）
     */
    @SuppressWarnings("unchecked")
    private short getContentViewType(IMessage message) {
        // 检查消息是否为图像类型，并且有非空的图像 URL，如果是，则返回图像消息的视图类型
        if (message instanceof MessageContentType.Image
                && ((MessageContentType.Image) message).getImageUrl() != null) {
            return VIEW_TYPE_IMAGE_MESSAGE;
        }

        // 其他默认类型的判断将放在这里

        // 如果消息是自定义内容类型，则遍历配置，尝试匹配
        if (message instanceof MessageContentType) {
            for (int i = 0; i < customContentTypes.size(); i++) {
                ContentTypeConfig config = customContentTypes.get(i);
                // 确保内容检查器不为空，否则抛出 IllegalArgumentException
                if (contentChecker == null) {
                    throw new IllegalArgumentException("ContentChecker cannot be null when using custom content types!");
                }
                boolean hasContent = contentChecker.hasContentFor(message, config.type);
                if (hasContent) return config.type;
            }
        }

        // 如果没有匹配到其他类型，则默认返回文本消息的视图类型
        return VIEW_TYPE_TEXT_MESSAGE;
    }

    // * HOLDERS
    // *

    /**
     * 传入和传出消息的视图持有者的基类。
     * 您可以扩展它以创建自己的持有人与自定义布局，甚至使用默认布局。
     */
    public static abstract class BaseMessageViewHolder<MESSAGE extends IMessage> extends ViewHolder<MESSAGE> {

        /**
         * 标记当前项是否被选中
         */
        boolean isSelected;

        /**
         * 用于设置自定义数据到ViewHolder。
         */
        protected Object payload;

        /**
         * 回调接口，用于在消息列表中加载图片。
         */
        protected ImageLoader imageLoader;

        /**
         * 已弃用的构造函数，用于初始化ViewHolder。
         *
         * @param itemView 包含视图元素的父视图
         */
        @Deprecated
        public BaseMessageViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 构造函数，用于初始化ViewHolder并设置自定义数据。
         *
         * @param itemView 包含视图元素的父视图
         * @param payload  用于局部更新的数据对象
         */
        public BaseMessageViewHolder(View itemView, Object payload) {
            super(itemView);
            this.payload = payload;
        }

        /**
         * 判断当前项是否被选中。
         *
         * @return 当前项是否被选中
         */
        public boolean isSelected() {
            return isSelected;
        }

        /**
         * 判断选择模式是否启用。
         *
         * @return 选择模式是否启用
         */
        public boolean isSelectionModeEnabled() {
            return MessagesListAdapter.isSelectionModeEnabled;
        }

        /**
         * 获取图片加载器接口。
         *
         * @return 图片加载器接口
         */
        public ImageLoader getImageLoader() {
            return imageLoader;
        }

        /**
         * 配置TextView中的链接行为。
         *
         * @param text 要配置的TextView
         */
        protected void configureLinksBehavior(final TextView text) {
            text.setLinksClickable(false);
            text.setMovementMethod(new LinkMovementMethod() {
                @Override
                public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
                    boolean result = false;
                    if (!MessagesListAdapter.isSelectionModeEnabled) {
                        result = super.onTouchEvent(widget, buffer, event);
                    }
                    itemView.onTouchEvent(event);
                    return result;
                }
            });
        }
    }


    /**
     * 用于处理接收文本消息的默认视图持有者实现。
     *
     * @param <MESSAGE> 消息的具体类型，必须是IMessage的子类
     */
    public static class IncomingTextMessageViewHolder<MESSAGE extends IMessage>
            extends BaseIncomingMessageViewHolder<MESSAGE> {

        // 用于显示消息气泡的布局容器
        protected ViewGroup bubble;
        // 显示消息文本的TextView组件
        protected TextView text;

        /**
         * 已弃用的构造函数，用于初始化ViewHolder并设置视图元素。
         *
         * @param itemView 包含视图元素的父视图
         */
        @Deprecated
        public IncomingTextMessageViewHolder(View itemView) {
            super(itemView);
            init(itemView);
        }

        /**
         * 构造函数，用于初始化ViewHolder并设置视图元素。
         *
         * @param itemView 包含视图元素的父视图
         * @param payload  用于局部更新的数据对象
         */
        public IncomingTextMessageViewHolder(View itemView, Object payload) {
            super(itemView, payload);
            init(itemView);
        }

        /**
         * 绑定消息数据到视图上。
         *
         * @param message 要绑定的消息对象
         */
        @Override
        public void onBind(MESSAGE message) {
            super.onBind(message);
            if (bubble != null) {
                bubble.setSelected(isSelected());
            }

            if (text != null) {
                text.setText(message.getText());
            }
        }

        /**
         * 应用样式到视图上。
         *
         * @param style 消息列表的样式配置
         */
        @Override
        public void applyStyle(MessagesListStyle style) {
            super.applyStyle(style);
            if (bubble != null) {
                bubble.setPadding(style.getIncomingDefaultBubblePaddingLeft(),
                        style.getIncomingDefaultBubblePaddingTop(),
                        style.getIncomingDefaultBubblePaddingRight(),
                        style.getIncomingDefaultBubblePaddingBottom());
                ViewCompat.setBackground(bubble, style.getIncomingBubbleDrawable());
            }

            if (text != null) {
                text.setTextColor(style.getIncomingTextColor());
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getIncomingTextSize());
                text.setTypeface(text.getTypeface(), style.getIncomingTextStyle());
                text.setAutoLinkMask(style.getTextAutoLinkMask());
                text.setLinkTextColor(style.getIncomingTextLinkColor());
                configureLinksBehavior(text);
            }
        }

        /**
         * 初始化视图元素。
         *
         * @param itemView 包含视图元素的父视图
         */
        private void init(View itemView) {
            bubble = itemView.findViewById(R.id.bubble);
            text = itemView.findViewById(R.id.messageText);
        }
    }


    /**
     * 用于处理发出文本消息的默认视图持有者实现。
     *
     * @param <MESSAGE> 消息的具体类型，必须是IMessage的子类
     */
    public static class OutcomingTextMessageViewHolder<MESSAGE extends IMessage>
            extends BaseOutcomingMessageViewHolder<MESSAGE> {

        // 用于显示消息气泡的布局容器
        protected ViewGroup bubble;
        // 显示消息文本的TextView组件
        protected TextView text;

        /**
         * 已弃用的构造函数，用于初始化ViewHolder并设置视图元素。
         *
         * @param itemView 包含视图元素的父视图
         */
        @Deprecated
        public OutcomingTextMessageViewHolder(View itemView) {
            super(itemView);
            init(itemView);
        }

        /**
         * 构造函数，用于初始化ViewHolder并设置视图元素。
         *
         * @param itemView 包含视图元素的父视图
         * @param payload  用于局部更新的数据对象
         */
        public OutcomingTextMessageViewHolder(View itemView, Object payload) {
            super(itemView, payload);
            init(itemView);
        }

        /**
         * 绑定消息数据到视图上。
         *
         * @param message 要绑定的消息对象
         */
        @Override
        public void onBind(MESSAGE message) {
            super.onBind(message);
            if (bubble != null) {
                bubble.setSelected(isSelected());
            }

            if (text != null) {
                text.setText(message.getText());
            }
        }

        /**
         * 应用样式到视图上。
         *
         * @param style 消息列表的样式配置
         */
        @Override
        public final void applyStyle(MessagesListStyle style) {
            super.applyStyle(style);
            if (bubble != null) {
                bubble.setPadding(style.getOutcomingDefaultBubblePaddingLeft(),
                        style.getOutcomingDefaultBubblePaddingTop(),
                        style.getOutcomingDefaultBubblePaddingRight(),
                        style.getOutcomingDefaultBubblePaddingBottom());
                ViewCompat.setBackground(bubble, style.getOutcomingBubbleDrawable());
            }

            if (text != null) {
                text.setTextColor(style.getOutcomingTextColor());
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getOutcomingTextSize());
                text.setTypeface(text.getTypeface(), style.getOutcomingTextStyle());
                text.setAutoLinkMask(style.getTextAutoLinkMask());
                text.setLinkTextColor(style.getOutcomingTextLinkColor());
                configureLinksBehavior(text);
            }
        }

        /**
         * 初始化视图元素。
         *
         * @param itemView 包含视图元素的父视图
         */
        private void init(View itemView) {
            bubble = itemView.findViewById(R.id.bubble);
            text = itemView.findViewById(R.id.messageText);
        }
    }


    /**
     * 处理接收到的图片消息的默认视图持有者实现。
     */
    public static class IncomingImageMessageViewHolder<MESSAGE extends MessageContentType.Image>
            extends BaseIncomingMessageViewHolder<MESSAGE> {

        /**
         * 显示图片的ImageView组件。
         */
        protected ImageView image;
        /**
         * 图片覆盖层的View组件。
         */
        protected View imageOverlay;

        /**
         * @param itemView 视图项
         * @deprecated 不推荐使用的构造方法，初始化视图持有者。
         */
        @Deprecated
        public IncomingImageMessageViewHolder(View itemView) {
            super(itemView);
            init(itemView);
        }

        /**
         * 构造方法，初始化视图持有者。
         *
         * @param itemView 视图项
         * @param payload  用于局部更新的数据对象
         */
        public IncomingImageMessageViewHolder(View itemView, Object payload) {
            super(itemView, payload);
            init(itemView);
        }

        /**
         * 绑定数据到视图。
         *
         * @param message 要绑定的消息对象
         */
        @Override
        public void onBind(MESSAGE message) {
            super.onBind(message);
            if (image != null && imageLoader != null) {
                imageLoader.loadImage(image, message.getImageUrl(), getPayloadForImageLoader(message));
            }

            if (imageOverlay != null) {
                imageOverlay.setSelected(isSelected());
            }
        }

        /**
         * 应用样式到视图。
         *
         * @param style 样式对象
         */
        @Override
        public final void applyStyle(MessagesListStyle style) {
            super.applyStyle(style);
            if (time != null) {
                time.setTextColor(style.getIncomingImageTimeTextColor());
                time.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getIncomingImageTimeTextSize());
                time.setTypeface(time.getTypeface(), style.getIncomingImageTimeTextStyle());
            }

            if (imageOverlay != null) {
                ViewCompat.setBackground(imageOverlay, style.getIncomingImageOverlayDrawable());
            }
        }

        /**
         * 重写此方法以传递自定义数据给ImageLoader进行图片加载(非头像)。
         *
         * @param message 包含图片的消息对象
         * @return 自定义负载数据
         */
        protected Object getPayloadForImageLoader(MESSAGE message) {
            return null;
        }

        /**
         * 初始化视图组件。
         *
         * @param itemView 视图项
         */
        private void init(View itemView) {
            image = itemView.findViewById(R.id.image);
            imageOverlay = itemView.findViewById(R.id.imageOverlay);

            if (image instanceof RoundedImageView) {
                ((RoundedImageView) image).setCorners(
                        R.dimen.message_bubble_corners_radius,
                        R.dimen.message_bubble_corners_radius,
                        R.dimen.message_bubble_corners_radius,
                        0
                );
            }
        }
    }


    /**
     * 默认的出站图片消息视图持有者实现类
     */
    public static class OutcomingImageMessageViewHolder<MESSAGE extends MessageContentType.Image>
            extends BaseOutcomingMessageViewHolder<MESSAGE> {

        // 用于显示图片的ImageView
        protected ImageView image;
        // 图片上的覆盖层View
        protected View imageOverlay;

        /**
         * 构造函数，用于初始化视图持有者
         *
         * @param itemView 表示消息视图的View
         * @deprecated 此构造函数已被废弃，建议使用带payload的构造函数
         */
        @Deprecated
        public OutcomingImageMessageViewHolder(View itemView) {
            super(itemView);
            init(itemView);
        }

        /**
         * 构造函数，用于初始化视图持有者
         *
         * @param itemView 表示消息视图的View
         * @param payload  用于局部刷新的负载对象
         */
        public OutcomingImageMessageViewHolder(View itemView, Object payload) {
            super(itemView, payload);
            init(itemView);
        }

        /**
         * 绑定数据到视图
         *
         * @param message 要绑定的消息对象
         */
        @Override
        public void onBind(MESSAGE message) {
            super.onBind(message);
            // 如果image和imageLoader不为空，则加载图片
            if (image != null && imageLoader != null) {
                imageLoader.loadImage(image, message.getImageUrl(), getPayloadForImageLoader(message));
            }

            // 如果imageOverlay不为空，则根据消息选择状态更新overlay
            if (imageOverlay != null) {
                imageOverlay.setSelected(isSelected());
            }
        }

        /**
         * 应用样式到视图
         *
         * @param style 消息列表的样式
         */
        @Override
        public final void applyStyle(MessagesListStyle style) {
            super.applyStyle(style);
            // 如果time不为空，则更新时间文本的颜色、大小和样式
            if (time != null) {
                time.setTextColor(style.getOutcomingImageTimeTextColor());
                time.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getOutcomingImageTimeTextSize());
                time.setTypeface(time.getTypeface(), style.getOutcomingImageTimeTextStyle());
            }

            // 如果imageOverlay不为空，则更新图片overlay的背景
            if (imageOverlay != null) {
                ViewCompat.setBackground(imageOverlay, style.getOutcomingImageOverlayDrawable());
            }
        }

        /**
         * 获取用于ImageLoader的负载数据
         * <p>用于加载图片（非头像）</p>
         *
         * @param message 带有图片的消息
         * @return 为ImageLoader准备的负载数据
         */
        protected Object getPayloadForImageLoader(MESSAGE message) {
            return null;
        }

        /**
         * 初始化视图组件
         *
         * @param itemView 表示消息视图的View
         */
        private void init(View itemView) {
            // 查找并初始化视图组件
            image = itemView.findViewById(R.id.image);
            imageOverlay = itemView.findViewById(R.id.imageOverlay);

            // 如果image是RoundedImageView，则设置其圆角
            if (image instanceof RoundedImageView) {
                ((RoundedImageView) image).setCorners(
                        R.dimen.message_bubble_corners_radius,
                        R.dimen.message_bubble_corners_radius,
                        0,
                        R.dimen.message_bubble_corners_radius
                );
            }
        }
    }


    /**
     * 默认的日期头部视图持有者类，继承自 ViewHolder<Date> 并实现 DefaultMessageViewHolder 接口
     * 该类用于展示日期头部信息
     */
    public static class DefaultDateHeaderViewHolder extends ViewHolder<Date>
            implements DefaultMessageViewHolder {

        // 用于显示日期文本的 TextView
        protected TextView text;
        // 日期格式字符串
        protected String dateFormat;
        // 日期格式化器，用于特定的日期头部格式化
        protected DateFormatter.Formatter dateHeadersFormatter;

        /**
         * 构造函数，初始化视图持有者
         *
         * @param itemView 父视图，用于查找和初始化 UI 组件
         */
        public DefaultDateHeaderViewHolder(View itemView) {
            super(itemView);
            // 通过资源 ID 查找并初始化文本视图
            text = itemView.findViewById(R.id.messageText);
        }

        /**
         * 绑定数据到视图
         *
         * @param date 要显示的日期对象
         */
        @Override
        public void onBind(Date date) {
            // 如果文本视图不为空，则进行日期格式化并设置文本
            if (text != null) {
                // 尝试使用日期头部格式化器格式化日期
                String formattedDate = null;
                if (dateHeadersFormatter != null) formattedDate = dateHeadersFormatter.format(date);
                // 如果特定格式化器未格式化成功，则使用备选的日期格式进行格式化
                text.setText(formattedDate == null ? DateFormatter.format(date, dateFormat) : formattedDate);
            }
        }

        /**
         * 应用样式到视图
         *
         * @param style 消息列表样式对象，包含日期头部的样式信息
         */
        @Override
        public void applyStyle(MessagesListStyle style) {
            // 如果文本视图不为空，则应用样式
            if (text != null) {
                // 设置文本颜色、大小、样式和内边距
                text.setTextColor(style.getDateHeaderTextColor());
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getDateHeaderTextSize());
                text.setTypeface(text.getTypeface(), style.getDateHeaderTextStyle());
                text.setPadding(style.getDateHeaderPadding(), style.getDateHeaderPadding(),
                        style.getDateHeaderPadding(), style.getDateHeaderPadding());
            }
            // 设置日期格式字符串，默认使用年月日格式
            dateFormat = style.getDateHeaderFormat();
            dateFormat = dateFormat == null ? DateFormatter.Template.STRING_DAY_MONTH_YEAR.get() : dateFormat;
        }
    }


    /**
     * 用于接收消息的基本视图持有者
     */
    public abstract static class BaseIncomingMessageViewHolder<MESSAGE extends IMessage>
            extends BaseMessageViewHolder<MESSAGE> implements DefaultMessageViewHolder {

        // 显示消息时间的文本视图
        protected TextView time;
        // 用户头像的图像视图
        protected ImageView userAvatar;

        /**
         * 已弃用的构造函数，建议使用带有 itemView 和 payload 参数的构造函数
         *
         * @param itemView 与此视图持有者关联的视图
         */
        @Deprecated
        public BaseIncomingMessageViewHolder(View itemView) {
            super(itemView);
            init(itemView);
        }

        /**
         * 构造函数
         *
         * @param itemView 与此视图持有者关联的视图
         * @param payload  用于视图部分更新的数据
         */
        public BaseIncomingMessageViewHolder(View itemView, Object payload) {
            super(itemView, payload);
            init(itemView);
        }

        /**
         * 将视图持有者绑定到消息
         *
         * @param message 要绑定的消息
         */
        @Override
        public void onBind(MESSAGE message) {
            // 设置消息创建时间
            if (time != null) {
                time.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
            }

            // 设置用户头像
            if (userAvatar != null) {
                boolean isAvatarExists = imageLoader != null
                        && message.getUser().getAvatar() != null
                        && !message.getUser().getAvatar().isEmpty();

                userAvatar.setVisibility(isAvatarExists ? View.VISIBLE : View.GONE);
                if (isAvatarExists) {
                    imageLoader.loadImage(userAvatar, message.getUser().getAvatar(), null);
                }
            }
        }

        /**
         * 应用样式到视图持有者的视图
         *
         * @param style 要应用的消息列表样式
         */
        @Override
        public void applyStyle(MessagesListStyle style) {
            // 应用时间文本的颜色、大小和样式
            if (time != null) {
                time.setTextColor(style.getIncomingTimeTextColor());
                time.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getIncomingTimeTextSize());
                time.setTypeface(time.getTypeface(), style.getIncomingTimeTextStyle());
            }

            // 设置用户头像的宽度和高度
            if (userAvatar != null) {
                userAvatar.getLayoutParams().width = style.getIncomingAvatarWidth();
                userAvatar.getLayoutParams().height = style.getIncomingAvatarHeight();
            }
        }

        // 初始化视图组件
        private void init(View itemView) {
            time = itemView.findViewById(R.id.messageTime);
            userAvatar = itemView.findViewById(R.id.messageUserAvatar);
        }
    }


    /**
     * 基础视图持有者类，专用于出站消息
     * 该类继承自 BaseMessageViewHolder 并实现了 DefaultMessageViewHolder 接口
     * 它提供了一些基本功能，用于显示发送方的消息及其时间
     */
    public abstract static class BaseOutcomingMessageViewHolder<MESSAGE extends IMessage>
            extends BaseMessageViewHolder<MESSAGE> implements DefaultMessageViewHolder {

        // 用于显示消息发送时间的文本视图
        protected TextView time;

        /**
         * 构造函数，初始化视图持有者
         *
         * @param itemView 代表消息项的视图
         *                 该构造函数不推荐使用，可能在未来版本中移除
         */
        @Deprecated
        public BaseOutcomingMessageViewHolder(View itemView) {
            super(itemView);
            init(itemView);
        }

        /**
         * 构造函数，带附加载荷初始化视图持有者
         *
         * @param itemView 代表消息项的视图
         * @param payload  附加的数据载荷
         */
        public BaseOutcomingMessageViewHolder(View itemView, Object payload) {
            super(itemView, payload);
            init(itemView);
        }

        /**
         * 绑定数据到视图
         * 主要功能是设置消息的发送时间
         *
         * @param message 要绑定的消息对象
         */
        @Override
        public void onBind(MESSAGE message) {
            if (time != null) {
                time.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
            }
        }

        /**
         * 应用样式到视图
         * 这里主要设置时间文本的颜色、大小和样式
         *
         * @param style 消息列表的样式对象
         */
        @Override
        public void applyStyle(MessagesListStyle style) {
            if (time != null) {
                time.setTextColor(style.getOutcomingTimeTextColor());
                time.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getOutcomingTimeTextSize());
                time.setTypeface(time.getTypeface(), style.getOutcomingTimeTextStyle());
            }
        }

        /**
         * 初始化视图组件
         * 主要是找到并设置时间文本视图
         *
         * @param itemView 代表消息项的视图
         */
        private void init(View itemView) {
            time = itemView.findViewById(R.id.messageTime);
        }
    }


    /*
     * DEFAULTS
     * 以下是默认的消息视图持有人配置
     * */

    // 定义了一个用于应用样式的接口
    interface DefaultMessageViewHolder {
        void applyStyle(MessagesListStyle style);
    }

    // 默认的传入文本消息视图持有人类
    private static class DefaultIncomingTextMessageViewHolder
            extends IncomingTextMessageViewHolder<IMessage> {

        // 构造方法初始化视图持有人
        public DefaultIncomingTextMessageViewHolder(View itemView) {
            super(itemView, null);
        }
    }

    // 默认的传出文本消息视图持有人类
    private static class DefaultOutcomingTextMessageViewHolder
            extends OutcomingTextMessageViewHolder<IMessage> {

        // 构造方法初始化视图持有人
        public DefaultOutcomingTextMessageViewHolder(View itemView) {
            super(itemView, null);
        }
    }

    // 默认的传入图片消息视图持有人类
    private static class DefaultIncomingImageMessageViewHolder
            extends IncomingImageMessageViewHolder<MessageContentType.Image> {

        // 构造方法初始化视图持有人
        public DefaultIncomingImageMessageViewHolder(View itemView) {
            super(itemView, null);
        }
    }

    // 默认的传出图片消息视图持有人类
    private static class DefaultOutcomingImageMessageViewHolder
            extends OutcomingImageMessageViewHolder<MessageContentType.Image> {

        // 构造方法初始化视图持有人
        public DefaultOutcomingImageMessageViewHolder(View itemView) {
            super(itemView, null);
        }
    }

    // 定义了消息内容类型的配置类
    private static class ContentTypeConfig<TYPE extends MessageContentType> {

        private byte type; // 消息类型
        private HolderConfig<TYPE> incomingConfig; // 传入消息的持有人配置
        private HolderConfig<TYPE> outcomingConfig; // 传出消息的持有人配置

        // 构造方法，用于初始化内容类型及其对应的持有人配置
        private ContentTypeConfig(
                byte type, HolderConfig<TYPE> incomingConfig, HolderConfig<TYPE> outcomingConfig) {

            this.type = type;
            this.incomingConfig = incomingConfig;
            this.outcomingConfig = outcomingConfig;
        }
    }

    // 定义了消息持有人的配置类
    private static class HolderConfig<T extends IMessage> {

        protected Class<? extends BaseMessageViewHolder<? extends T>> holder; // 持有人类
        protected int layout; // 布局资源
        protected Object payload; // 附加数据

        // 构造方法，用于初始化持有人类和布局资源
        HolderConfig(Class<? extends BaseMessageViewHolder<? extends T>> holder, int layout) {
            this.holder = holder;
            this.layout = layout;
        }

        // 构造方法，用于初始化持有人类、布局资源和附加数据
        HolderConfig(Class<? extends BaseMessageViewHolder<? extends T>> holder, int layout, Object payload) {
            this.holder = holder;
            this.layout = layout;
            this.payload = payload;
        }
    }

}
