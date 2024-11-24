package com.example.userstory.ui.feature.photo_list

data class PhotoListState(
    val isPhotoListLoading: Boolean = false,
    val photoList: List<String> = emptyList(),
    val toolbarTitle: String = "",
)