package com.example.userstory.ui.feature.photo

// Intent
sealed class PhotoIntent {
    data class UpdateButtonVisibility(val isVisible: Boolean) : PhotoIntent()
}