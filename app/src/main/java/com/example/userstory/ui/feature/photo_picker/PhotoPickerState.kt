package com.example.userstory.ui.feature.photo_picker

import android.net.Uri

data class PhotoPickerState(
    val accessablePhotoList: List<Uri> = emptyList(),
    val accessedPhotoList: List<Uri> = emptyList(),
    val showFolderNameInputDialog: Boolean = false,
)