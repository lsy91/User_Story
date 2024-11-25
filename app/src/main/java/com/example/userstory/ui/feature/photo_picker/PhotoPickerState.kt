package com.example.userstory.ui.feature.photo_picker

import android.net.Uri

data class PhotoPickerState(
    val accessedPhotoList: List<Uri> = emptyList(),
    val showFolderNameInputDialog: Boolean = false,
)