package com.example.administrator.live.views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.example.administrator.live.databinding.ViewCustomTitleBarBinding

class CustomTitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : RelativeLayout(context, attrs) {
    private val binding: ViewCustomTitleBarBinding =
        ViewCustomTitleBarBinding.inflate(LayoutInflater.from(context), this, true)
    private var onMenuItemClickListener: (() -> Unit)? = null

    init {
        binding.back.setOnClickListener {
            (context as? Activity)?.finish()
        }
        binding.menu.setOnClickListener {
            onMenuItemClickListener?.invoke()
        }
    }
    fun setTitle(title: String) {
        binding.titleText.text = title
    }

    fun setMenuVisible(visible: Boolean) {
        binding.menu.visibility = if (visible) VISIBLE else GONE
    }

    fun setOnMenuItemClickListener(listener: (() -> Unit)?) {
        onMenuItemClickListener = listener
    }
}