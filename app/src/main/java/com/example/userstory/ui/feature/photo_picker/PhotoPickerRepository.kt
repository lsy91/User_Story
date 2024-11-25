package com.example.userstory.ui.feature.photo_picker

import android.content.Context
import android.net.Uri
import com.example.userstory.utils.saveUriToAppSpecificFolder
import javax.inject.Inject

class PhotoPickerRepository @Inject constructor(
): IPhotoPickerRepository {

    // 권한 부여까지 끝난 사진들을 실제 갤러리에 저장
    override suspend fun saveAccessedPhotos(context: Context, accessedPhotoList: List<Uri>, albumName: String) {
        saveUriToAppSpecificFolder(context, accessedPhotoList, albumName)
    }
}