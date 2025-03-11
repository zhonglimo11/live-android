package com.example.administrator.live.adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.administrator.live.bean.Notice
import com.example.administrator.live.databinding.ItemNoticeBinding
import com.stfalcon.chatkit.utils.DateFormatter

class NoticeListAdapter(layoutResId: Int, data: MutableList<Notice>) :
    BaseQuickAdapter<Notice, BaseViewHolder>(layoutResId, data) {

    private lateinit var binding: ItemNoticeBinding

    override fun convert(holder: BaseViewHolder, notice: Notice) {
        binding = ItemNoticeBinding.bind(holder.itemView)
        val name = notice.user.name

        // 设置文本内容
        with(binding) {
            val dateFormat = "yyyy年MM月dd日 HH:mm"
            noticeTime.text = DateFormatter.format(notice.time, dateFormat)
            when (notice.action) {
                "newSubordinate" -> {
                    noticeText.text = "恭喜您有新的下级 $name 加入"
                }
            }
        }
    }
}