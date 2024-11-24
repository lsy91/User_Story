package com.example.userstory.ui.feature.photo

import com.example.userstory.ui.feature.photo.bean.DecoItem

data class PhotoState(
    val isButtonVisible: Boolean = false,
    val isSaving: Boolean = false,
    val borderDecoItems: List<DecoItem> = emptyList(),
    val decoItems: List<DecoItem> = emptyList()
)