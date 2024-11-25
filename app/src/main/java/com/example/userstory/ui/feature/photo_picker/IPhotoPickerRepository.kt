package com.example.userstory.ui.feature.photo_picker

import android.content.Context
import android.net.Uri

interface IPhotoPickerRepository {
    suspend fun saveAccessedPhotos(context: Context, accessedPhotoList: List<Uri>, albumName: String)
}