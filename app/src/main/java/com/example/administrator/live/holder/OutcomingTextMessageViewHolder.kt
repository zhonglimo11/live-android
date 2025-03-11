package com.example.administrator.live.holder

import android.view.View
import androidx.core.content.ContextCompat
import com.example.administrator.live.R
import com.example.administrator.live.bean.Message
import com.example.administrator.live.databinding.ItemOutcomingTextMessageBinding
import com.example.administrator.live.utils.EmoJiUtils.parseEmoJi
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesListStyle

/**
 * 继承 BaseIncomingMessageViewHolder以显示头像
 */
class OutcomingTextMessageViewHolder(itemView: View, payload: Any? = null) :
    MessageHolders.BaseIncomingMessageViewHolder<Message>(itemView, payload){
    private val binding = ItemOutcomingTextMessageBinding.bind(itemView)
    /**
     * 绑定消息数据到视图上。
     *
     * @param message 要绑定的消息对象
     */
    override fun onBind(message: Message) {
        super.onBind(message)
        with(binding){
            bubble.isSelected = isSelected
            val spannableString = message.getText().let { parseEmoJi(itemView.context, it) }
            messageText.text = spannableString
            messageText.autoLinkMask = 15
            messageText.setLinkTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
        }
    }

    override fun applyStyle(style: MessagesListStyle?) {
        super.applyStyle(style)
        configureLinksBehavior(binding.messageText)
    }
}
