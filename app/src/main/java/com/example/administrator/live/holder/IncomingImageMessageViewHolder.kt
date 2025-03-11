package com.example.administrator.live.holder

import android.view.View
import com.example.administrator.live.bean.Message
import com.example.administrator.live.databinding.ItemIncomingImageMessageBinding
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesListStyle

class IncomingImageMessageViewHolder(itemView: View, payload: Any? = null) :
    MessageHolders.BaseIncomingMessageViewHolder<Message>(itemView, payload){
    private val binding = ItemIncomingImageMessageBinding.bind(itemView)
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
            if (message.getGroupId() != null){
                userName.text = message.user?.name ?: ""
                userName.visibility = View.VISIBLE
            }else{
                userName.visibility = View.GONE
            }
        }
    }

    override fun applyStyle(style: MessagesListStyle?) {
        super.applyStyle(style)
    }
}
