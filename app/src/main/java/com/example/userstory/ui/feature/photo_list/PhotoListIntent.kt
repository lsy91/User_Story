package com.example.userstory.ui.feature.photo_list

sealed class PhotoListIntent {
    data class ChangeToolbarTitle(val title: String?) : PhotoListIntent()
    data object LoadPhotoList : PhotoListIntent()
}