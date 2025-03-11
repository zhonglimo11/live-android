package com.example.administrator.live.core.ui.customui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.administrator.live.R
import com.example.administrator.live.bean.Image
import com.example.administrator.live.bean.modelValue
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.HalfTransparent2
import com.example.administrator.live.core.ui.theme.Red
import timber.log.Timber

@Composable
fun IconWithText(iconWithText: IconWithText) {
    with(iconWithText) {
        Column(
            modifier = Modifier.clickable { onClick() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = null,
                modifier = Modifier.size(imageWidth.dp, imageHeight.dp)
            )
            Margin(marginHeight)
            Text(
                text = text,
                color = Color.White,
                fontSize = textSize.sp
            )
        }
    }
}

data class IconWithText(
    val iconId: Int,
    val text: String,
    val onClick: () -> Unit = {},
    val imageWidth: Int = 32,
    val imageHeight: Int = imageWidth,
    val marginHeight: Int = 2,
    val textSize: Int = 13
)

fun getIconWithTextList(
    iconWithText: List<IconWithText>,
    imageWidth: Int,
    imageHeight: Int = imageWidth,
    marginHeight: Int,
    textSize: Int
): List<IconWithText> {
    return iconWithText.map {
        it.copy(
            imageWidth = imageWidth,
            imageHeight = imageHeight,
            marginHeight = marginHeight,
            textSize = textSize
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CameraIconWithText(item: SimpleIconWithText, isSelected: Boolean, onClick: () -> Unit) {
    val (icon, name) = item
    val checkBox =
        if (isSelected) Modifier.border(0.5.dp, Red, RoundedCornerShape(5.dp)) else Modifier
    Box(
        Modifier
            .size(66.dp, 82.dp)
            .clip(RoundedCornerShape(5.dp))
            .combinedClickable(
                onClick = {
                    onClick()
                    Timber.d("Item clicked")
                }
            )
            .then(checkBox)
            .background(FontColor),
    ) {
        val title = if (name.isEmpty()) "无" else name
        if (icon != null) {
            AsyncImage(
                icon.modelValue, null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(R.drawable.image_empty)
            )
        } else {
            Box(
                Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.TopCenter)
            ) {
                Image(
                    painterResource(R.drawable.empty_camera_icon),
                    null,
                    Modifier.size(30.dp),
                    contentScale = ContentScale.Crop
                )
            }

        }
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .background(HalfTransparent2)
                .height(20.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            SimpleText(title, 14, Color.White)
        }
    }
}

data class SimpleIconWithText(
    val icon: Image? = null,
    val text: String = "",
)