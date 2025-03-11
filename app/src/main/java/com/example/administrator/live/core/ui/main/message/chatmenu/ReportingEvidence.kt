package com.example.administrator.live.core.ui.main.message.chatmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.ContinueButton
import com.example.administrator.live.core.ui.customui.Margin
import com.example.administrator.live.core.ui.customui.SimpleImage
import com.example.administrator.live.core.ui.customui.SimpleText
import com.example.administrator.live.core.ui.customui.TextInputBox
import com.example.administrator.live.core.ui.customui.dashedBorder
import com.example.administrator.live.core.ui.navhost.Graph
import com.example.administrator.live.core.ui.navhost.Screen
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.ui.theme.HalfTransparent2
import com.example.administrator.live.core.ui.theme.Red
import com.example.administrator.live.core.ui.theme.Red2
import com.example.administrator.live.core.viewmodels.ChatViewModel

@Composable
fun ReportingEvidence(navController: NavController, viewModel: ChatViewModel) {
    val checkedStates = viewModel.reportChecked
    val selectedItems = viewModel.reportItems.filterIndexed { index, _ -> checkedStates[index] }
    val images = viewModel.reportImages
    val canSubmit = when {
        images.isNotEmpty() && viewModel.reportRemarks.text.isNotEmpty() -> true
        else -> false
    }
    BaseScreen(
        backgroundColor = Color.White,
        title = "补充举报证据",
        navController = navController
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp, top = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp, 0.dp, 16.dp, 60.dp)
            ) {
                item {
                    SimpleText("举报原因", 12, FontColor2)
                    Margin(6)
                }
                items(selectedItems) { item ->
                    SimpleText(item)
                }
                item {
                    Margin(32)
                    SimpleText("具体情况说明")
                    Margin(12)
                    TextInputBox(value = viewModel.reportRemarks, hint = "补充更详细的说明，可帮助工作人员更快定位问题，快速处理", modifier = Modifier.heightIn(min = 160.dp)
                        .fillMaxWidth()) {
                        viewModel.reportRemarks = it
                    }
                }
                item {
                    Margin(32)
                    SimpleText("图片证明(最多6张)")
                    Margin(6)
                    SimpleText("直观的截图是非常有利的举报证据", 14, FontColor2)
                    Margin(12)
                }
                item {
                    LazyVerticalGrid(
                        userScrollEnabled = false,
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.heightIn(max = 500.dp)
                    ) {
                        if (images.isNotEmpty()) {
                            items(images) { image ->
                                Box(
                                    Modifier
                                        .size(105.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                ) {
                                    AsyncImage(
                                        image,
                                        null,
                                        Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                navController.navigate(
                                                    Screen.SelectBgPhoto(
                                                        Screen.ReportChat.route
                                                    ).route
                                                )
                                            },
                                        contentScale = ContentScale.Crop,
                                        placeholder = painterResource(R.drawable.image_empty)
                                    )
                                    Box(
                                        Modifier
                                            .size(24.dp, 14.dp)
                                            .clickable { viewModel.reportImages.remove(image) }
                                            .background(
                                                HalfTransparent2,
                                                RoundedCornerShape(bottomStart = 8.dp)
                                            )
                                            .align(Alignment.TopEnd),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        SimpleImage(
                                            R.drawable.icon_delete_img,
                                            12,
                                            12
                                        ) { viewModel.reportImages.remove(image) }
                                    }
                                }
                            }
                        }
                        if (images.size < 6) {
                            item {
                                Box(
                                    Modifier
                                        .size(105.dp)
                                        .dashedBorder()
                                        .clickable {
                                            navController.navigate(
                                                Screen.SelectBgPhoto(
                                                    Screen.ReportChat.route
                                                ).route
                                            )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    SimpleImage(
                                        R.drawable.icon_plus,
                                        27,
                                        27
                                    ) { navController.navigate(Screen.SelectBgPhoto(Screen.ReportChat.route).route) }
                                }
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 32.dp)
                    .clickable {
                        if (canSubmit) {
                            viewModel.reportImages.clear()
                            repeat(viewModel.reportChecked.size) {
                                viewModel.reportChecked[it] = false
                            }
                            viewModel.reportRemarks = TextFieldValue()
                            navController.popBackStack(Graph.ChatMenu.route, inclusive = false)
                        }
                    }
            ) {
                val color = if (canSubmit) Red else Red2
                ContinueButton(color, modifier = Modifier.fillMaxWidth()
                    .height(46.dp)){
                    SimpleText("提交", 16, Color.White)
                }
            }
        }
    }
}