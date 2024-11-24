package com.example.userstory.ui.feature.album_list

import com.example.userstory.ui.feature.album_list.bean.Album

interface IAlbumListRepository {
    suspend fun loadAlbumsGroupedByFolder(): List<Album>
}