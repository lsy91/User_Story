package com.example.userstory.ui.feature.album_list

import android.content.Context
import com.example.userstory.ui.feature.album_list.bean.Album
import com.example.userstory.utils.CommonUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlbumListRepository @Inject constructor(
    @ApplicationContext private val context: Context,
): IAlbumListRepository {

    // 앱에서 접근 가능한 이미지만 가져오기
    override suspend fun loadMyAlbums(): List<Album> {
        return CommonUtils.accessAllMediaFilesGroupedByFolder(context, true)
    }

    // 전체 이미지 가져오기
    override suspend fun loadAlbumsGroupedByFolder(): List<Album> {
        return CommonUtils.accessAllMediaFilesGroupedByFolder(context, false)
    }
}