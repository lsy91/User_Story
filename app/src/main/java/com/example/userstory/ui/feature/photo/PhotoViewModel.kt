package com.example.userstory.ui.feature.photo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PhotoViewModel : ViewModel() {
    private val _state = MutableStateFlow(PhotoState())
    val state: StateFlow<PhotoState> = _state

    fun handleIntent(intent: PhotoIntent) {
        when (intent) {
            is PhotoIntent.UpdateButtonVisibility -> {
                _state.update { currentState ->
                    currentState.copy(isButtonVisible = intent.isVisible)
                }
            }
        }
    }
}