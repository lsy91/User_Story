package com.example.userstory.ui.feature.photo

import com.example.userstory.ui.feature.photo.bean.DecoItem

data class PhotoState(
    val isDecoItemLoading: Boolean = true,
    val isButtonVisible: Boolean = false,
    val isSaving: Boolean = false,
    val decoItems: List<DecoItem> = emptyList()
)