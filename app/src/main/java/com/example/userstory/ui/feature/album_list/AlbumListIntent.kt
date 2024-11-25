package com.example.userstory.ui.feature.album_list

sealed class AlbumListIntent {
    data object LoadMyAlbums : AlbumListIntent()
    data object LoadAllAlbums : AlbumListIntent()
}