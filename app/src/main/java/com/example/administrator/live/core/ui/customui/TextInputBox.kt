package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.ui.theme.PurpleGrey

@Composable
fun TextInputBox(
    modifier: Modifier = Modifier,
    value: TextFieldValue = TextFieldValue(),
    hint: String = "",
    hintStyle: TextStyle = TextStyle(
        fontSize = 14.sp,
        color = FontColor2,
    ),
    textStyle: TextStyle = TextStyle(
        fontSize = 14.sp,
        color = FontColor,
    ),
    maxTextLength: Int = 500,
    color: Color = PurpleGrey,
    roundedCornerShape: Int = 12,
    showCountText: Boolean = true,
    onValueChange: (TextFieldValue) -> Unit = {},
) {
    var textFieldValue by remember { mutableStateOf(value) }
    BasicTextField(
        modifier = modifier
            .background(color, RoundedCornerShape(roundedCornerShape.dp))
            .padding(16.dp),
        textStyle = textStyle,
        value = textFieldValue,
        onValueChange = {
            if (it.text.length <= maxTextLength) {
                textFieldValue = it
                onValueChange(it)
            }
        },
        cursorBrush = SolidColor(Color.Red),
        decorationBox = { innerTextField ->
            Column {
                Box {
                    if (textFieldValue.text.isEmpty()) {
                        Text(
                            hint,
                            style = hintStyle,
                        )
                    }
                    innerTextField()
                }
                if (showCountText) {
                    Spacer(Modifier.weight(1f))
                    Box(
                        Modifier
                            .align(Alignment.End)
                            .padding(top = 12.dp)
                    ) {
                        SimpleText(
                            "${textFieldValue.text.length}/${maxTextLength}",
                            14,
                            FontColor2
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = false)
@Composable
private fun TextInputBoxPreview() {
    TextInputBox(
        hint = "说点什么",
        hintStyle = TextStyle(
            color = FontColor2,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp
        ),
        textStyle = TextStyle(
            color = FontColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp
        ),
        color = Color.Transparent,
        showCountText = false
    )
}