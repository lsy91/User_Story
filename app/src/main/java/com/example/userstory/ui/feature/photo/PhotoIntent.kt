package com.example.userstory.ui.feature.photo

sealed class PhotoIntent {
    data class UpdateButtonVisibility(val isVisible: Boolean) : PhotoIntent()
    data class UpdateSavingState(val isSaving: Boolean) : PhotoIntent()
}