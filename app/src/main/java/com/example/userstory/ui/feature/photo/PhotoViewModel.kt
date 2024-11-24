package com.example.userstory.ui.feature.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PhotoState())
    val state: StateFlow<PhotoState> = _state

    fun handleIntent(intent: PhotoIntent) {
        when (intent) {
            is PhotoIntent.GetBorderDecoItem -> {
                getDecoImages("deco_svg_images_border/")
            }
            is PhotoIntent.GetDecoItem -> {
                getDecoImages("deco_svg_images/")
            }
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

    private fun getDecoImages(path: String) {
        viewModelScope.launch {
            try {
                when {
                    path.equals("deco_svg_images_border/", ignoreCase = true) -> {
                        _state.update {
                            it.copy(
                                borderDecoItems = photoRepository.getDecoImages(path).items
                            )
                        }
                    }
                    path.equals("deco_svg_images/", ignoreCase = true) -> {
                        _state.update {
                            it.copy(
                                decoItems = photoRepository.getDecoImages(path).items
                            )
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}