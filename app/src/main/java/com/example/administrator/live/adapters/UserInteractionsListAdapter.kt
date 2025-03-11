package com.example.administrator.live.adapters

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.administrator.live.bean.UserInteractions
import com.example.administrator.live.core.viewmodels.UserAction
import com.example.administrator.live.databinding.ItemUserInteractionsBinding
import com.example.administrator.live.utils.AppUtils
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.utils.DateFormatter

class UserInteractionsListAdapter(layoutResId: Int, data: MutableList<UserInteractions>) :
    BaseQuickAdapter<UserInteractions, BaseViewHolder>(layoutResId, data) {

    private lateinit var binding: ItemUserInteractionsBinding

    override fun convert(holder: BaseViewHolder, item: UserInteractions) {
        binding = ItemUserInteractionsBinding.bind(holder.itemView)
        val name = item.user.name
        val dateFormat = "MM-dd"
        val timeStr = DateFormatter.format(item.time, dateFormat)
        with(binding) {
            this.item.setOnClickListener {
                //TODO 跳转到视频/评论页面
                AppUtils.showToast(context, "跳转到视频/评论页面未实现", false)
            }
            Picasso.get().load(item.user.avatar).into(userAvatar)
            item.video.cover?.let {
                Picasso.get().load(item.video.cover).into(videoImage)
            }
            if (item.user.id == item.video.author.toString()) {
                author.visibility = View.VISIBLE
            }
            userName.text = name
            when (item.action) {
                UserAction.COMMENTS.action -> {
                    comments.visibility = View.VISIBLE
                    comments.text = item.comments?.content
                    if (item.comments?.replyTo == null) {
                        userInteractions.text = "评论了你的作品 $timeStr"
                    } else {
                        userInteractions.text = "回复了你的评论 $timeStr"
                        line.visibility = View.VISIBLE
                        fromComments.visibility = View.VISIBLE
                        fromComments.text = item.comments?.replyTo?.content
                    }
                }

                UserAction.THUMBS_UP.action -> {
                    if (item.comments != null) {
                        userInteractions.text = "赞了你的评论 $timeStr"
                        line.visibility = View.VISIBLE
                        fromComments.visibility = View.VISIBLE
                        fromComments.text = item.comments?.content
                    } else {
                        userInteractions.text = "赞了你的作品 $timeStr"
                    }
                }

                UserAction.E_TER.action -> {
                    userInteractions.text = "提到了你：${item.comments?.content}"
                    userInteractions.textSize = 14F
                }
            }
        }
    }
}