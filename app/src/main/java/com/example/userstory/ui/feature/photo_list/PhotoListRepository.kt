package com.example.userstory.ui.feature.photo_list

import com.example.userstory.ui.feature.album_list.bean.Album
import javax.inject.Inject

class PhotoListRepository @Inject constructor(): IPhotoListRepository {
    override suspend fun loadPhotoList(album: Album?): List<String> {
        // 앨범 데이터를 기반으로 사진 리스트 반환
        return album?.photos ?: emptyList()
    }
}