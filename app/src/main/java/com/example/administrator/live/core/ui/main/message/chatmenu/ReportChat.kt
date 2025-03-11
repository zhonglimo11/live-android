package com.example.administrator.live.core.ui.main.message.chatmenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.ContinueButton
import com.example.administrator.live.core.ui.customui.CustomCheckbox
import com.example.administrator.live.core.ui.customui.SimpleText
import com.example.administrator.live.core.ui.navhost.Screen
import com.example.administrator.live.core.ui.theme.Red
import com.example.administrator.live.core.ui.theme.Red2
import com.example.administrator.live.core.viewmodels.ChatViewModel

@Composable
fun ReportChat(navController: NavController, viewModel: ChatViewModel) {
    val checkedStates = viewModel.reportChecked
    val canContinue = checkedStates.any { it }
    BaseScreen(
        backgroundColor = Color.White,
        title = "举报",
        navController = navController
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp)
        ) {
            LazyColumn(
                Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                itemsIndexed(viewModel.reportItems) { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        SimpleText(item)
                        Spacer(modifier = Modifier.weight(1f))
                        CustomCheckbox(
                            isChecked = checkedStates[index],
                            onClick = {
                                checkedStates[index] = !checkedStates[index]
                            }
                        )
                    }
                }
            }
            val color = if (canContinue) Red else Red2
            Box(
                Modifier
                    .padding(horizontal = 32.dp)
                    .align(Alignment.BottomCenter)
                    .clickable {
                        if (canContinue) {
                            navController.navigate(Screen.ReportingEvidence.route)
                        }
                    }
            ) {
                ContinueButton(color, modifier = Modifier.fillMaxWidth()
                    .height(46.dp)){
                    SimpleText("下一步",16,Color.White)
                }
            }
        }
    }
}