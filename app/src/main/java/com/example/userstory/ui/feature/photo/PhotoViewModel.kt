package com.example.userstory.ui.feature.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userstory.di.ShimmerModule
import com.facebook.shimmer.Shimmer
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
            is PhotoIntent.GetDecoItem -> {
                getDecoImages()
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

    private fun getDecoImages() {
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(
                        decoItems = photoRepository.getDecoImages("deco_svg_images_border/").items,
                        isDecoItemLoading = false
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()

                _state.update {
                    it.copy(
                        isDecoItemLoading = false
                    )
                }
            }
        }
    }

    fun provideShimmer(): Shimmer {
        return ShimmerModule.provideShimmer()
    }
}