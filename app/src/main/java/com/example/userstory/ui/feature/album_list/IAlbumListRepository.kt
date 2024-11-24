package com.example.userstory.ui.feature.album_list

import com.example.userstory.ui.feature.album_list.bean.AlbumList

interface IAlbumListRepository {
    suspend fun loadAlbumsGroupedByFolder(): List<AlbumList>
}