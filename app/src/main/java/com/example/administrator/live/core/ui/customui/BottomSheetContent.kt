package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.ui.theme.PurpleGrey
import com.example.administrator.live.core.ui.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    title: String,
    oldString: String,
    inputString: String,
    updateInput: (String) -> Unit,
    onCancelClicked: () -> Unit,
    onConfirmClicked: () -> Unit
) {
    ModalBottomSheet(
        dragHandle = {},
        onDismissRequest = { onCancelClicked() },
    ) {
        val isSame = inputString == oldString
        val completeTextColor = if (isSame) FontColor2 else Red
        Surface(
            color = PurpleGrey,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "取消",
                        fontSize = 18.sp,
                        color = FontColor,
                        modifier = Modifier.clickable { onCancelClicked() })
                    Text(
                        title,
                        fontWeight = FontWeight.Bold,
                        color = FontColor,
                        fontSize = 18.sp
                    )
                    Text(
                        "完成",
                        fontSize = 18.sp,
                        color = completeTextColor,
                        modifier = Modifier.clickable { onConfirmClicked() })
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val valueText = if (isSame) "" else inputString
                    TextField(
                        value = valueText,
                        onValueChange = { updateInput(it) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = Red
                        ),
                        shape = RoundedCornerShape(8.dp),
                        placeholder = {
                            if (isSame) {
                                Text(oldString, color = FontColor2)
                            }
                        },
                        trailingIcon = {
                            if (!isSame) {
                                Image(
                                    painter = painterResource(id = R.drawable.icon_delete),
                                    contentDescription = "delete",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { updateInput("") })
                            }
                        }
                    )
                }
            }
        }
    }
}