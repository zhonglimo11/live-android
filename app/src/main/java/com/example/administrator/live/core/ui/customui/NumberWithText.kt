package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun NumberWithText(
    number: String,
    numberSize: Int,
    text: String,
    textSize: Int = 12,
    margin: Int = 0,
    numberColor: Color = Color.White,
    textColor: Color = Color.White
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SimpleText(number, numberSize, numberColor)
        Margin(margin)
        SimpleText(text, textSize, textColor)
    }
}