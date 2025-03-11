package com.example.administrator.live.core.ui.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.TopAppBarColors
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val PurpleGrey = Color(0xFFF0F0F0)
val Red = Color(0xFFFF1F5A)
val Red2 = Color(0xFFFFA5BD)
var White = Color(0xFFD2D2D2)
var Gray = Color(0xFFD9D9D9)

val FontColor = Color(0xFF333333)
val FontColor2 = Color(0xFF999999)
val FontColor3 = Color(0xFF000000)
val FontColor4 = Color(0xFFF3F3F3)

val LineColor = Color(0xFFE9E9E9)
val LineColor2 = Color(0xFF858585)


val SwitchColors = SwitchColors(
    checkedThumbColor = Color.White,
    checkedTrackColor = Red,
    checkedBorderColor = Red,
    checkedIconColor = Color.White,
    uncheckedThumbColor = Color.White,
    uncheckedTrackColor = White,
    uncheckedBorderColor = White,
    uncheckedIconColor = Color.White,
    disabledCheckedThumbColor = Color.White,
    disabledCheckedTrackColor = Red,
    disabledCheckedBorderColor = Red,
    disabledCheckedIconColor = Color.White,
    disabledUncheckedThumbColor = Color.White,
    disabledUncheckedTrackColor = White,
    disabledUncheckedBorderColor = White,
    disabledUncheckedIconColor = Color.White,
)

@OptIn(ExperimentalMaterial3Api::class)
val TopAppBarColors = TopAppBarColors(
    containerColor = PurpleGrey,
    navigationIconContentColor = FontColor,
    titleContentColor = FontColor,
    actionIconContentColor = FontColor,
    scrolledContainerColor = PurpleGrey
)

val ButtonColors = ButtonColors(
    containerColor = Red,
    contentColor = Red,
    disabledContainerColor = Red,
    disabledContentColor = Red
)

val ButtonColors2 = ButtonColors(
    containerColor = PurpleGrey,
    contentColor = PurpleGrey,
    disabledContainerColor = PurpleGrey,
    disabledContentColor = PurpleGrey
)

val ButtonColors3 = ButtonColors(
    containerColor = Red,
    contentColor = Red,
    disabledContainerColor = Red2,
    disabledContentColor = Red2
)

val GradientBrush = Brush.linearGradient(
    colors = listOf(Color(0xFFf4465A), Color(0xFFf86C9F)),
)

val GradientBrush2 = Brush.linearGradient(
    colors = listOf(Color(0xff3996ff), Color(0xff49c7ff)),
)

val GradientBrush3 = Brush.linearGradient(
    colors = listOf(Color(0xffffb62d), Color(0xffffc634)),
)

val HalfTransparent = Color(0x29FFFFFF)
val HalfTransparent2 = Color(0x4D000000)
val HalfTransparent3 = Color(0x80000000)