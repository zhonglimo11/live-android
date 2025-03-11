package com.example.administrator.live.holder

import android.view.View
import com.example.administrator.live.databinding.ItemDateHeaderBinding
import com.stfalcon.chatkit.messages.MessageHolders.DefaultDateHeaderViewHolder
import com.stfalcon.chatkit.messages.MessagesListStyle
import com.stfalcon.chatkit.utils.DateFormatter
import java.util.Date

class DateHeaderViewHolder(itemView: View) : DefaultDateHeaderViewHolder(itemView) {
    private val binding = ItemDateHeaderBinding.bind(itemView)
    override fun onBind(date: Date) {
        val dateFormat = "yyyy-MM-dd HH:mm"
        binding.messageText.text = DateFormatter.format(date, dateFormat)
    }

    //覆盖父类方法
    override fun applyStyle(style: MessagesListStyle) {
    }

}