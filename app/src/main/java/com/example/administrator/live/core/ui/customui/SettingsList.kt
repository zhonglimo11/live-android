package com.example.administrator.live.core.ui.customui

import androidx.compose.runtime.Composable

data class SettingItem(
    val title: String,
    val onClick: () -> Unit,
    val hasDivider: Boolean? = true
)

@Composable
fun SettingsList(items: List<SettingItem>) {
    DefSurfaceColumn {
        items.forEach { item ->
            FunctionMenu(
                item.title,
                onClick = item.onClick,
                hasDivider = item.hasDivider ?: true
            )
        }
    }
}