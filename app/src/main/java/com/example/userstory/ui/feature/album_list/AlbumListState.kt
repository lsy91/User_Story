package com.example.userstory.ui.feature.album_list

import com.example.userstory.ui.feature.album_list.bean.AlbumList

data class AlbumListState(
    val isAlbumListLoading: Boolean = false,
    val albums: List<AlbumList> = emptyList(), // 폴더별 이미지 데이터
)