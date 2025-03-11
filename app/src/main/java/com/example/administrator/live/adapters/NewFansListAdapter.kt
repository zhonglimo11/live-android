package com.example.administrator.live.adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.administrator.live.bean.NewFans
import com.example.administrator.live.databinding.ItemFansBinding
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.utils.DateFormatter

class NewFansListAdapter(layoutResId: Int, data: MutableList<NewFans>) :
    BaseQuickAdapter<NewFans, BaseViewHolder>(layoutResId, data) {

    private lateinit var binding: ItemFansBinding

    override fun convert(holder: BaseViewHolder, newFans: NewFans) {
        binding = ItemFansBinding.bind(holder.itemView)
        val name = newFans.user?.name
        // 设置文本内容
        with(binding) {
            tvTitle.text = name
            Picasso.get().load(newFans.user?.avatar).into(ivIcon)
            val dateFormat = "MM-dd"
            val timeStr = DateFormatter.format(newFans.date, dateFormat)
            tvMsg.text = "开始关注了你 $timeStr"
        }
    }
}