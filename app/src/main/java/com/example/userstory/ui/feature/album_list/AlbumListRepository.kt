package com.example.userstory.ui.feature.album_list

import android.content.Context
import com.example.userstory.ui.feature.album_list.bean.AlbumList
import com.example.userstory.utils.CommonUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlbumListRepository @Inject constructor(
    @ApplicationContext private val context: Context
): IAlbumListRepository {
    override suspend fun loadAlbumsGroupedByFolder(): List<AlbumList> {
        return CommonUtils.accessAllMediaFilesGroupedByFolder(context)
    }
}