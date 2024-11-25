package com.example.userstory.ui.feature.album_list

import com.example.userstory.ui.feature.album_list.bean.Album

data class AlbumListState(
    val isAlbumListLoading: Boolean = false,
    val albums: List<Album> = emptyList(), // 폴더별 이미지 데이터
)