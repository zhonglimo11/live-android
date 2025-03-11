package com.example.administrator.live.core.ui.main.message.chatmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.administrator.live.R
import com.example.administrator.live.bean.FollowState
import com.example.administrator.live.bean.User
import com.example.administrator.live.core.ui.customui.Avatar
import com.example.administrator.live.core.ui.customui.AvatarWithNickname
import com.example.administrator.live.core.ui.customui.BaseScreen
import com.example.administrator.live.core.ui.customui.BottomSheetContent
import com.example.administrator.live.core.ui.customui.CustomDialog
import com.example.administrator.live.core.ui.customui.DefSurfaceColumn
import com.example.administrator.live.core.ui.customui.FunctionMenu
import com.example.administrator.live.core.ui.customui.Margin
import com.example.administrator.live.core.ui.customui.QRCodeDialog
import com.example.administrator.live.core.ui.customui.SettingItem
import com.example.administrator.live.core.ui.customui.SettingsList
import com.example.administrator.live.core.ui.customui.SimpleImage
import com.example.administrator.live.core.ui.customui.SimpleText
import com.example.administrator.live.core.ui.navhost.Screen
import com.example.administrator.live.core.ui.theme.ButtonColors
import com.example.administrator.live.core.ui.theme.ButtonColors2
import com.example.administrator.live.core.ui.theme.FontColor2
import com.example.administrator.live.core.ui.theme.SwitchColors
import com.example.administrator.live.core.viewmodels.ChatViewModel
import com.example.administrator.live.core.viewmodels.DialogType
import com.example.administrator.live.core.viewmodels.PermissionType

@Composable
fun ChatMenu(navController: NavController, viewModel: ChatViewModel) {
    with(viewModel) {
        BaseScreen(title = "聊天信息", navController = navController) {
            val dialogInfo = dialogInfo(viewModel)
            ShowDialog(viewModel, dialogInfo)
            LazyColumn(
                Modifier.padding(20.dp,0.dp,20.dp,20.dp)
            ) {
                if (users.size > 1) {
                    item {
                        GroupMenu(viewModel, navController)
                    }
                } else {
                    val user = users.first()
                    item {
                        UserMenu(viewModel, user, navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupMenu(viewModel: ChatViewModel, navController: NavController) {
    with(viewModel) {
        val userListHeight = if (users.size > 4) 170.dp else 80.dp
        val memberCountStr = "${users.size}/${maxGroupSize}"
        Margin(12)
        Members(viewModel, memberCountStr, userListHeight, navController)
        Margin(16)
        GroupInfo(viewModel, navController)
        if (userPermissions != PermissionType.MEMBER) {
            Margin(16)
            DefSurfaceColumn {
                FunctionMenu(
                    "群管理",
                    onClick = { navController.navigate(Screen.GroupAdmin.route) },
                    hasDivider = false
                )
            }
        }
        ChatSetting(viewModel, navController)
        SettingsList(
            items = listOf(
                SettingItem("设置当前聊天背景", { navController.navigate(Screen.ChatBackgroundSet.route) }),
                SettingItem("清空聊天记录", { onDeleteMessageClicked() }),
                SettingItem("举报", { navController.navigate(Screen.ReportChat.route) }, hasDivider = false)
            )
        )
        Margin(16)
        ExitGroup(viewModel)
    }

}

@Composable
private fun UserMenu(viewModel: ChatViewModel, user: User, navController: NavController) {
    with(viewModel) {
        Margin(12)
        UserInfo(viewModel, user)
        Margin(16)
        DefSurfaceColumn {
            FunctionMenu("设置备注名",
                hasDivider = false,
                info = { SimpleText(chatRemarks, 14, FontColor2) },
                onClick = { setRemarkName() })
        }
        ChatSetting(viewModel, navController)
        SettingsList(
            items = listOf(
                SettingItem("设置当前聊天背景", { navController.navigate(Screen.ChatBackgroundSet.route) }),
                SettingItem("清空聊天记录", { onDeleteMessageClicked() }),
                SettingItem("举报", { navController.navigate(Screen.ReportChat.route) }),
                SettingItem("拉黑", { blacklist() }, hasDivider = false)
            )
        )
    }
}

@Composable
private fun ShowDialog(viewModel: ChatViewModel, dialogInfo: DialogInfo) {
    with(viewModel) {
        if (dialogType != DialogType.NONE) {
            when (dialogType) {
                DialogType.QR_CODE -> QRCodeDialog(groupQRCode,
                    groupName,
                    dateQRCode,
                    { onCancelClicked() },
                    { onConfirmClicked() })

                DialogType.EDIT_NICK_NAME, DialogType.EDIT_GROUP_NAME, DialogType.CHAT_REMARK -> BottomSheetContent(
                    dialogInfo.inputTitle,
                    dialogInfo.inputHint,
                    dialogInfo.inputValue,
                    dialogInfo.onInputUpdate,
                    { onCancelClicked() },
                    { onConfirmClicked() })

                else -> CustomDialog(title = dialogInfo.dialogTitle,
                    content = dialogInfo.dialogText,
                    onConfirm = { onConfirmClicked() },
                    onCancel = { onCancelClicked() })
            }
        }
    }
}

@Composable
private fun dialogInfo(viewModel: ChatViewModel): DialogInfo {
    with(viewModel) {
        val dialogInfo = when (dialogType) {
            DialogType.EXIT_GROUP -> DialogInfo("确认退出此群聊？")
            DialogType.DELETE_MESSAGE -> DialogInfo("确认删除本地聊天记录？")
            DialogType.BLACK_LIST -> DialogInfo(
                "确认加入黑名单吗？",
                "加入黑名单后,对方将无法搜索到你,无法查看你的作品,也不能给你发送私信"
            )

            DialogType.EDIT_GROUP_NAME -> DialogInfo(
                inputTitle = "设置群名称",
                onInputUpdate = { groupNameInput = it },
                inputHint = groupName,
                inputValue = groupNameInput
            )

            DialogType.EDIT_NICK_NAME -> DialogInfo(
                inputTitle = "设置我在本群的昵称",
                onInputUpdate = ::updateNickNameInput,
                inputHint = nickName,
                inputValue = nickNameInput
            )

            DialogType.CHAT_REMARK -> DialogInfo(
                inputTitle = "设置备注名",
                onInputUpdate = ::updateChatRemarksInput,
                inputHint = chatRemarks,
                inputValue = chatRemarksInput
            )

            else -> DialogInfo()
        }
        return dialogInfo
    }
}

@Composable
private fun ExitGroup(viewModel: ChatViewModel) {
    val onExitGroupClicked = viewModel::onExitGroupClicked
    DefSurfaceColumn {
        FunctionMenu("退出群聊", R.color.fontColor_6, imageSecond = {
            SimpleImage(R.drawable.img_group_exit, 18, 18)
        }, hasDivider = false, hasDefImg = false, onClick = { onExitGroupClicked() })
    }
}

@Composable
private fun GroupInfo(viewModel: ChatViewModel, navController: NavController) {
    with(viewModel) {
        DefSurfaceColumn {
            FunctionMenu("名称", info = {
                SimpleText(groupName, 14, FontColor2)
            }, onClick = { modifyGroupName() })
            FunctionMenu("群二维码", imageSecond = {
                SimpleImage(R.drawable.q_r_code, 18, 18)
            }, onClick = { showGroupQRCode() })
            FunctionMenu("群公告", infoSecond = {
                if (groupInfo != "") {
                    Text(
                        groupInfo,
                        maxLines = 1,
                        fontSize = 12.sp,
                        color = FontColor2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }, onClick = { navController.navigate(Screen.GroupNotice.route) })
            FunctionMenu("我在本群的昵称", info = {
                SimpleText(nickName, 14, FontColor2)
            }, hasDivider = false, onClick = { modifyNickName() })
        }
    }
}

@Composable
private fun Members(
    viewModel: ChatViewModel,
    memberCountStr: String, userListHeight: Dp,
    navController: NavController
) {
    with(viewModel) {
        DefSurfaceColumn {
            FunctionMenu("群聊成员", info = {
                SimpleText(memberCountStr, 14, FontColor2)
            }, hasDivider = false, onClick = { showAllUsers() })
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier.height(userListHeight),
                horizontalArrangement = Arrangement.SpaceEvenly,
                contentPadding = PaddingValues(10.dp, 0.dp, 10.dp, 10.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                items(minOf(users.size, 9)) { index ->
                    Avatar(
                        user = users[index],
                        onClick = { navController.navigate(Screen.UserDetail.route) })
                }
                item {
                    Avatar(imgResId = R.drawable.header_invitation,
                        name = "邀请",
                        onClick = { navController.navigate(Screen.InviteMember.route) })
                }
            }
        }
    }
}

@Composable
private fun UserInfo(viewModel: ChatViewModel, user: User) {
    val followState = viewModel.followState
    val onFollowClicked = viewModel::onFollowClicked
    Surface(
        shape = MaterialTheme.shapes.small, color = colorResource(id = R.color.white)
    ) {
        val followTextMap = mapOf(
            FollowState.FOLLOW to FollowInfo("关注", Color.White, ButtonColors),
            FollowState.FOLLOWED to FollowInfo("互关", Color.White, ButtonColors),
            FollowState.UNFOLLOW to FollowInfo("已关注", FontColor2, ButtonColors2),
            FollowState.FOLLOW_EACH_OTHER to FollowInfo("互相关注", FontColor2, ButtonColors2)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val followInfo = followTextMap[followState] ?: FollowInfo(
                "关注", Color.White, ButtonColors
            )
            AvatarWithNickname(avatar = user.avatar, nickname = user.name, marginHorizontal = 8)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = { onFollowClicked() },
                colors = followInfo.buttonColors,
                modifier = Modifier.size(81.dp, 32.dp),
                contentPadding = PaddingValues()
            ) {
                SimpleText(followInfo.text, 14, followInfo.color)
            }
        }
    }
}

@Composable
fun ChatSetting(viewModel: ChatViewModel, navController: NavController) {
    Margin(16)
    DefSurfaceColumn {
        FunctionMenu(
            "查找聊天记录",
            hasDivider = false,
            onClick = { navController.navigate(Screen.ChatHistory.route) })
    }
    Margin(16)
    MessagesSettings(viewModel)
    Margin(16)
}

@Composable
fun MessagesSettings(viewModel: ChatViewModel) {
    with(viewModel) {
        DefSurfaceColumn {
            FunctionMenu(
                "消息免打扰", switch = {
                    Switch(
                        checked = switchDoNotDisturb,
                        onCheckedChange = { setDoNotDisturbEnabled(switchDoNotDisturb) },
                        colors = SwitchColors,
                        modifier = Modifier.height(24.dp)
                    )
                }, hasDefImg = false
            )
            FunctionMenu(
                "消息置顶", switch = {
                    Switch(
                        checked = switchPinned,
                        onCheckedChange = { setPinnedEnabled(switchPinned) },
                        colors = SwitchColors,
                        modifier = Modifier.height(24.dp)
                    )
                }, hasDefImg = false, hasDivider = false
            )
        }
    }
}

data class FollowInfo(val text: String, val color: Color, val buttonColors: ButtonColors)

data class DialogInfo(
    val dialogTitle: String = "",
    val dialogText: String? = null,
    val inputTitle: String = "",
    val inputHint: String = "",
    val inputValue: String = "",
    val onInputUpdate: (String) -> Unit = {}
)