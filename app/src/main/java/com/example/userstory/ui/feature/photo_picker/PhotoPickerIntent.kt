package com.example.userstory.ui.feature.photo_picker

import android.net.Uri

sealed class PhotoPickerIntent {
    data class SaveAccessedPhoto(val accessedPhoto: Uri): PhotoPickerIntent()
    data class SaveAccessedPhotos(val accessedPhotoList: List<Uri>, val albumName: String) : PhotoPickerIntent()
    data class ShowFolderNameInputDialog(val isShow: Boolean) : PhotoPickerIntent()
}