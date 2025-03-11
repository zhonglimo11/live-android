package com.example.administrator.live.holder

import android.view.View
import com.example.administrator.live.R
import com.example.administrator.live.bean.Chat
import com.example.administrator.live.bean.User
import com.example.administrator.live.databinding.ItemMessageBinding
import com.othershe.combinebitmap.CombineBitmap
import com.othershe.combinebitmap.layout.DingLayoutManager
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.dialogs.DialogListStyle
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import com.stfalcon.chatkit.utils.DateFormatter

class DialogViewHolder(itemView: View) : DialogsListAdapter.DialogViewHolder<Chat>(itemView) {
    private val binding = ItemMessageBinding.bind(itemView)
    override fun onBind(chat: Chat) {
        with(binding) {
            val dateFormat = "HH:mm"
            val lastMsg = chat.lastMessage
            if (lastMsg != null) {
                val date = chat.lastMessage!!.createdAt
                dialogDate.text = DateFormatter.format(date, dateFormat)
                dialogLastMessage.text = chat.lastMessage!!.text
                if (chat.lastMessage!!.imageUrl != null) {
                    dialogLastMessage.text = "[图片]"
                }
                if (chat.lastMessage!!.getVoice() != null) {
                    dialogLastMessage.text = "[语音]"
                }
            } else {
                dialogDate.text = ""
                dialogLastMessage.text = "暂无消息"
            }
            dialogName.text = chat.dialogName
            loadAvatar(chat)
            dialogUnreadBubble.text = chat.unreadCount.toString()
            dialogUnreadBubble.visibility = if (chat.unreadCount > 0) View.VISIBLE else View.GONE
            dialogContainer.setOnClickListener { view: View? ->
                if (onDialogClickListener != null) {
                    onDialogClickListener.onDialogClick(chat)
                }
                if (onDialogViewClickListener != null) {
                    onDialogViewClickListener.onDialogViewClick(view, chat)
                }
            }
            dialogContainer.setOnLongClickListener { view: View? ->
                if (onLongItemClickListener != null) {
                    onLongItemClickListener.onDialogLongClick(chat)
                }
                if (onDialogViewLongClickListener != null) {
                    onDialogViewLongClickListener.onDialogViewLongClick(view, chat)
                }
                onLongItemClickListener != null || onDialogViewLongClickListener != null
            }
        }
    }

    private fun loadAvatar(chat: Chat) {
        when (chat.id) {
            "systemNotice" -> {
                Picasso.get().load(R.drawable.img_system_notice).into(binding.dialogAvatar)
            }

            "newFollow" -> {
                Picasso.get().load(R.drawable.img_new_follow).into(binding.dialogAvatar)
            }

            "customerService" -> {
                Picasso.get().load(R.drawable.img_customer_service).into(binding.dialogAvatar)
            }
            else -> {
                if (chat.getGroup() != null){
                    val avatarUrlList = chat.getGroup()?.member?.map { it.avatar }
                    combineAvatars(avatarUrlList)
                }
                if (chat.getUser() != null) {
                    val icon = chat.getUser()?.avatar
                    imageLoader.loadImage(binding.dialogAvatar, icon, null)
                }
            }
        }
        updateInLiveVisibility(chat.users)
    }

    private fun combineAvatars(avatarUrlList: List<String>?) {
        if (avatarUrlList.isNullOrEmpty()) return
        //微信样式不能超过9
//        val wechatLayoutManager = WechatLayoutManager()
        //钉钉样式不能超过4
        val dingLayoutManager = DingLayoutManager()
        // 确保 avatarUrlList 的大小不超过 4
        val limitedAvatarUrlList = avatarUrlList.take(4)
        CombineBitmap.init(itemView.context)
            .setLayoutManager(dingLayoutManager)
            .setSize(60)
            .setGap(1)
            .setUrls(*limitedAvatarUrlList.toTypedArray())
            .setImageView(binding.dialogAvatar)
            .build()
    }

    private fun updateInLiveVisibility(users: ArrayList<User?>) {
        if (users.size == 1 && users[0]?.isInLive() == true) {
            binding.inLive.visibility = View.VISIBLE
        } else {
            binding.inLive.visibility = View.GONE
        }
    }

    override fun setDialogStyle(dialogStyle: DialogListStyle?) {
    }
}