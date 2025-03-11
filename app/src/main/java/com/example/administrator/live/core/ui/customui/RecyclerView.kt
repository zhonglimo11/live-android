package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.administrator.live.utils.AppUtils.dpToPx

@Composable
fun RecyclerList(paddingTop:Int = 1, adapter: RecyclerView.Adapter<*>) {
    AndroidView(
        factory = {
            RecyclerView(it).apply {
                layoutManager = LinearLayoutManager(context)
                this.adapter = adapter
                setPadding(0,it.dpToPx(paddingTop),0,0)
            }
        },
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
    )
}