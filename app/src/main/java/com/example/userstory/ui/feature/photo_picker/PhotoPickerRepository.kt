package com.example.userstory.ui.feature.photo_picker

import android.content.Context
import android.net.Uri
import com.example.userstory.ui.common.PreferenceManager
import com.example.userstory.utils.saveUriToAppSpecificFolder
import javax.inject.Inject

class PhotoPickerRepository @Inject constructor(
    private val preferenceManager: PreferenceManager
): IPhotoPickerRepository {

    // Preference 에 포토피커로 지정한 사진 uri 리스트 저장
    override suspend fun setAccessablePhotos(context: Context, accessablePhotoList: List<Uri>) {
        preferenceManager.saveAccessablePhotos(accessablePhotoList)
    }

    override suspend fun setAccessedPhotos(context: Context, accessedPhotoList: List<Uri>) {
        preferenceManager.saveAccessedPhotos(accessedPhotoList)
    }

    // 권한 부여까지 끝난 사진들을 실제 갤러리에 저장
    override suspend fun saveAccessedPhotos(context: Context, accessedPhotoList: List<Uri>, albumName: String) {
        saveUriToAppSpecificFolder(context, accessedPhotoList, albumName)
    }
}