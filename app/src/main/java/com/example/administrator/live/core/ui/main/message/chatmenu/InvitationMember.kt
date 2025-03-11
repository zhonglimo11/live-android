package com.example.administrator.live.core.ui.main.message.chatmenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.administrator.live.core.viewmodels.ChatViewModel
import com.example.administrator.live.core.ui.customui.AvatarWithNickname
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.CustomCheckbox
import com.example.administrator.live.core.ui.customui.Margin
import com.example.administrator.live.core.ui.customui.SearchView
import com.example.administrator.live.core.ui.customui.SimpleText
import com.example.administrator.live.core.ui.customui.VerticalIndexBar
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.ui.theme.FontColor3
import com.example.administrator.live.core.ui.theme.LineColor
import com.example.administrator.live.core.ui.theme.Red
import com.example.administrator.live.utils.AppUtils.getInitials

@Composable
fun InvitationMember(navController: NavController,viewModel: ChatViewModel) {
    with(viewModel) {
        val completeTextColor = if (selectedFriends.isEmpty()) FontColor2 else Red
        BaseScreen(
            title = "邀请群成员",
            navController = navController,
            backgroundColor = Color.White,
            topBarMenu = {
                Text(
                    "完成",
                    color = completeTextColor,
                    fontSize = 18.sp,
                    modifier = Modifier.clickable {navController.popBackStack() })
            }) {
            Column {
                Box(
                    Modifier.padding(16.dp)
                ) {
                    SearchView(
                        searchInput,
                        {searchInput = it},
                        selectedFriends = selectedFriends
                    )
                }
                HorizontalDivider(color = LineColor)
                FriendsListView(viewModel)
            }
        }
    }
}

@Composable
fun FriendsListView(viewModel: ChatViewModel) {
    with(viewModel) {
        val isSearching = searchInput.isNotEmpty()
        val filteredFriends = remember(friends, searchInput) {
            if (isSearching) {
                friends.filter { it.name.contains(searchInput, ignoreCase = true) } // 忽略大小写的过滤
            } else {
                friends
            }
        }
        val groupedUsers = remember(filteredFriends) {
            filteredFriends.groupBy { getInitials(it.name) }
        }
        val (sections, sectionPositions) = derivedStateOf {
            val sortedKeys = groupedUsers.keys.sorted()
            val positions = mutableMapOf<String, Int>()
            var position = 0
            sortedKeys.forEach { key ->
                positions[key] = position
                position += 1 + (groupedUsers[key]?.size ?: 0)
            }
            sortedKeys.map { it to (groupedUsers[it] ?: emptyList()) } to positions
        }.value

        val checkedStates =
            remember { mutableStateListOf(*filteredFriends.map { false }.toTypedArray()) }
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        // 主列表
        Row(
            Modifier.padding(horizontal = 12.dp)
        ) {
            LazyColumn(
                Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
                state = listState
            ) {
                sections.forEach { (initial, userList) ->
                    // 显示索引头
                    item {
                        Margin(6)
                        if (!isSearching) {
                            SimpleText(initial, 12, FontColor3)
                        }
                        Margin(6)
                    }
                    // 列表中的用户项
                    itemsIndexed(userList) { index, user ->
                        val userIndex = filteredFriends.indexOf(user) // 获取用户的实际索引
                        val isChecked = checkedStates[userIndex] // 使用用户索引
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val inGroup = users.any { it.id == user.id }
                            Margin(0, 4)
                            CustomCheckbox(isChecked = isChecked, onClick = {
                                checkedStates[userIndex] = !isChecked // 更新对应的状态
                                updateSelectedFriends(user)
                            }, inGroup = inGroup)

                            Margin(0, 12)
                            AvatarWithNickname(
                                avatar = user.avatar,
                                nickname = user.name,
                                marginHorizontal = 12
                            )
                        }
                        // 在用户项之间添加间隔
                        if (isSearching || index < userList.size - 1) {
                            Margin(20)
                        }
                    }
                }
            }
            // 显示所有字母的索引条
            if (!isSearching) {
                VerticalIndexBar(sectionPositions, listState, coroutineScope)
            }
        }
    }
}