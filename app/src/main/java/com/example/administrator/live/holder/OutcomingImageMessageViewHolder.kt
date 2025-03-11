package com.example.administrator.live.holder

import android.view.View
import com.example.administrator.live.bean.Message
import com.example.administrator.live.databinding.ItemOutcomingImageMessageBinding
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesListStyle

/**
 * 继承 BaseIncomingMessageViewHolder以显示头像
 */
class OutcomingImageMessageViewHolder(itemView: View, payload: Any? = null) :
    MessageHolders.BaseIncomingMessageViewHolder<Message>(itemView, payload){
    private val binding = ItemOutcomingImageMessageBinding.bind(itemView)
    /**
     * 绑定消息数据到视图上。
     *
     * @param message 要绑定的消息对象
     */
    override fun onBind(message: Message) {
        super.onBind(message)
        with(binding){
            imageLoader.loadImage(image,message.imageUrl,null)
            imageOverlay.isSelected = isSelected
        }
    }

    override fun applyStyle(style: MessagesListStyle?) {
        super.applyStyle(style)
    }
}
