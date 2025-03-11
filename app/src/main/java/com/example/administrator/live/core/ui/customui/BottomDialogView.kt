package com.example.administrator.live.core.ui.customui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.administrator.live.R
import com.example.administrator.live.core.ui.theme.FontColor
import com.example.administrator.live.core.ui.theme.FontColor2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomDialogView(
    selectedIndex: Int,
    selectedItemIndex: Int,
    tabs: List<String>,
    icons: List<SimpleIconWithText>,
    onCancelClicked: () -> Unit,
    onTabClick: (index: Int) -> Unit,
    onItemClick: (index: Int) -> Unit,
    paddingStart: Int = 20,
    tabWidth: Int = 57,
) {
    val tabRowWidth = (tabs.size * tabWidth).dp
    ModalBottomSheet(
        dragHandle = {},
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        scrimColor = Color.Transparent,
        onDismissRequest = { onCancelClicked() },
    ) {
        Surface(
            color = Color.Black,
        ) {
            Column {
                LazyRow(
                    if (tabs.size > 1) {
                        Modifier
                            .fillMaxWidth()
                            .padding(start = paddingStart.dp)
                            .height(41.dp)
                    } else {
                        Modifier
                            .fillMaxWidth()
                            .height(41.dp)
                    },
                    horizontalArrangement = if (tabs.size > 1) Arrangement.Start else Arrangement.Center
                ) {
                    item {
                        TabRow(
                            selectedTabIndex = selectedIndex,
                            containerColor = Color.Transparent,
                            modifier = Modifier.width(tabRowWidth),
                            indicator = { tabPositions ->
                                if (tabs.size > 1) {
                                    TabRowDefaults.SecondaryIndicator(
                                        modifier = Modifier
                                            .tabIndicatorOffset(tabPositions[selectedIndex])
                                            .padding(horizontal = 18.dp),
                                        color = Color.White
                                    )
                                }
                            },
                            divider = {}
                        ) {
                            tabs.forEachIndexed { index, title ->
                                val selected = selectedIndex == index
                                Tab(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .height(25.dp),
                                    selected = selected,
                                    onClick = {
                                        onTabClick(index)
                                    }
                                ) {
                                    Text(
                                        title,
                                        fontSize = 16.sp,
                                        color = if (selected) Color.White else FontColor2
                                    )
                                }
                            }
                            if (paddingStart == 41) {
                                Row(
                                    Modifier.padding(start = 20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painterResource(R.drawable.icon_reset),
                                        null,
                                        Modifier.size(17.dp, 16.dp)
                                    )
                                    Margin(width = 2)
                                    SimpleText("重置", 14, FontColor2)
                                }
                            }
                        }
                    }
                }
                HorizontalDivider(color = FontColor)
                LazyRow(
                    Modifier
                        .padding(start = 9.dp, top = 20.dp, bottom = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    itemsIndexed(icons) { index, item ->
                        CameraIconWithText(
                            item,
                            selectedItemIndex == index
                        ) { onItemClick(index) }
                    }
                }
            }
        }
    }
}