package com.example.administrator.live.core.ui.main.message.chatmenu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.CustomCheckbox
import com.example.administrator.live.core.ui.customui.Margin
import com.example.administrator.live.core.ui.customui.SimpleText
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.ui.theme.Red
import com.example.administrator.live.core.viewmodels.SelectFromPhotoViewModel

@Composable
fun SelectFromPhoto(navController: NavController, viewModel: SelectFromPhotoViewModel) {
    val isComplete = viewModel.selectedImgList.size > 0
    BaseScreen(
        backgroundColor = Color.White,
        hasTitle = false,
        hasLine = false,
        navController = navController,
        topBarMenu = {
            if (viewModel.selectMode) {
                val color = if (isComplete) Red else FontColor2
                Box(Modifier.clickable {
                    if (isComplete) {
                        navController.popBackStack()
                    }
                }) {
                    SimpleText("完成", 18, color)
                }
            }
        },
        centerContent = {
            Row(
                Modifier
                    .padding(end = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    viewModel.selectPhotoName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        viewModel.updatePhotoFolderListShow()
                    })
                Image(
                    painterResource(R.drawable.vw_ic_arrow_down),
                    null,
                    Modifier
                        .size(14.dp)
                        .graphicsLayer(
                            rotationZ = if (viewModel.isPhotoFolderListShow) 180f else 0f,
                        ),
                    colorFilter = ColorFilter.tint(
                        Color.Black
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) {
        Column {
            Margin(6)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(3.dp),// 水平间隔
                verticalArrangement = Arrangement.spacedBy(3.dp)// 垂直间隔
            ) {
                items(viewModel.imgList) { img ->
                    Box(
                        modifier = Modifier
                            .size(123.dp)
                            .clickable { viewModel.onImageClick(img) },
                    ) {
                        AsyncImage(
                            model = img,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize(),
                            placeholder = painterResource(R.drawable.image_empty),
                            contentScale = ContentScale.Crop
                        )
                        if (viewModel.selectMode) {
                            Box(
                                Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                            ) {
                                CustomCheckbox(viewModel.selectedImgList.contains(img)) {
                                    viewModel.onImageClick(img)
                                }
                            }
                        }
                    }
                }
            }
        }
        PhotoListDialog(viewModel)
    }
}

@Composable
fun PhotoListDialog(viewModel: SelectFromPhotoViewModel) {
    val isPhotoFolderListShow = viewModel.isPhotoFolderListShow
    val allFolderList = viewModel.allImgFolderList
    val updatePhotoName = viewModel::updatePhotoName
    AnimatedVisibility(
        visible = isPhotoFolderListShow,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize() // 占满整个屏幕
                .background(color = Color(0x66000000)) // 设置遮罩颜色
                .pointerInput(Unit) {
                    detectTapGestures {}
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .heightIn(max = 505.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    items(allFolderList) { folder ->
                        val folderName = folder.first
                        val previewImg = folder.second.first()
                        val imgCount = folder.second.size
                        val countStr = "(${imgCount})"
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    updatePhotoName(folderName)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = previewImg,
                                contentDescription = null,
                                modifier = Modifier.size(88.dp),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.image_empty),
                            )
                            Margin(0, 16)
                            Text(folderName, fontSize = 16.sp)
                            Text(countStr, fontSize = 16.sp, color = FontColor2)
                        }
                    }
                }
            }
        }
    }
}