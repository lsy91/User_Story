package com.example.userstory.ui.feature.photo_list

import com.example.userstory.ui.feature.album_list.bean.Album

interface IPhotoListRepository {
    suspend fun loadPhotoList(album: Album?): List<String>
}