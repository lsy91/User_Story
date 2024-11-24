package com.example.userstory.ui.feature.photo

sealed class PhotoIntent {
    data object GetBorderDecoItem: PhotoIntent()
    data object GetDecoItem: PhotoIntent()
    data class UpdateButtonVisibility(val isVisible: Boolean) : PhotoIntent()
    data class UpdateSavingState(val isSaving: Boolean) : PhotoIntent()
}