package com.example.userstory.ui.feature.photo

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PhotoState())
    val state: StateFlow<PhotoState> = _state

    fun handleIntent(intent: PhotoIntent) {
        when (intent) {
            is PhotoIntent.UpdateButtonVisibility -> {
                _state.update {
                    it.copy(isButtonVisible = intent.isVisible)
                }
            }
            is PhotoIntent.UpdateSavingState -> {
                _state.update { it.copy(isSaving = intent.isSaving) }
            }
            else -> {}
        }
    }
}