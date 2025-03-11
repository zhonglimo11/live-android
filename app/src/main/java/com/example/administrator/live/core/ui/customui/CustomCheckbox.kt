package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.administrator.live.R

@Composable
fun CustomCheckbox(isChecked: Boolean, onClick: () -> Unit, inGroup: Boolean = false) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clickable {
                if (!inGroup) {
                    onClick()
                }
            }
    ) {
        val resource =
            if (inGroup) {
                painterResource(id = R.drawable.img_in_group)
            } else {
                if (isChecked) {
                    painterResource(id = R.drawable.img_checked)
                } else {
                    painterResource(id = R.drawable.img_unchecked)
                }
            }
        Image(painter = resource, modifier = Modifier.size(24.dp), contentDescription = null)
    }
}

@Composable
fun CustomCheckbox(isChecked: Boolean, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clickable {
                onClick()
            }
    ) {
        val resource =
            if (isChecked) {
                painterResource(id = R.drawable.img_checked)
            } else {
                painterResource(id = R.drawable.img_unchecked)
            }
        Image(painter = resource, modifier = Modifier.size(24.dp), contentDescription = null)
    }
}