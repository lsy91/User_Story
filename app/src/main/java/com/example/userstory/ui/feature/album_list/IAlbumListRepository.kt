package com.example.userstory.ui.feature.album_list

import com.example.userstory.ui.feature.album_list.bean.Album

interface IAlbumListRepository {
    suspend fun loadMyAlbums(): List<Album>
    suspend fun loadAlbumsGroupedByFolder(): List<Album>
}