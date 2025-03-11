package com.example.administrator.live.core.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.administrator.live.utils.AppUtils.getImageFoldersWithImages
import kotlinx.coroutines.launch

class SelectFromPhotoViewModel : ViewModel() {
    // 选中相册名
    var selectPhotoName by mutableStateOf("所有图片")

    //显示相册文件夹
    var isPhotoFolderListShow by mutableStateOf(false)

    // 相册图片
    var imgList by mutableStateOf(listOf<String>())

    // 所有图片文件夹
    var allImgFolderList = mutableListOf<Pair<String, List<String>>>()

    //选中图片列表
    var selectedImgList = mutableStateListOf<String>()

    //选择模式
    var selectMode by mutableStateOf(false)

    var onImageClick: (String) -> Unit = {}

    init {
        viewModelScope.launch {
            loadInitData()
        }
    }

    private fun loadInitData() {
        //获取系统图片文件夹
        setAllFolderList(getImageFoldersWithImages())
        updatePhotoName("所有图片")
    }

    fun updatePhotoName(newPhotoName: String) {
        isPhotoFolderListShow = false
        selectPhotoName = newPhotoName
        imgList = allImgFolderList.find { it.first == newPhotoName }?.second ?: emptyList()
    }

    fun updatePhotoFolderListShow() {
        isPhotoFolderListShow = !isPhotoFolderListShow
    }

    private fun setAllFolderList(allFloat: List<Pair<String, List<String>>>) {
        allImgFolderList.addAll(allFloat)
    }
}