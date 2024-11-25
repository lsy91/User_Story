package com.example.userstory.ui.feature.photo_picker

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoPickerViewModel @Inject constructor(
    private val photoPickerRepository: IPhotoPickerRepository,
    @ApplicationContext private val context: Context,
): ViewModel() {

    private val _state = MutableStateFlow(PhotoPickerState(
        accessedPhotoList = emptyList()
    ))
    val state: StateFlow<PhotoPickerState> = _state

    fun handleIntent(intent: PhotoPickerIntent) {
        when (intent) {
            is PhotoPickerIntent.SaveAccessedPhoto -> {

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            accessedPhotoList = state.value.accessedPhotoList + intent.accessedPhoto
                        )
                    }
                }

            }
            is PhotoPickerIntent.SaveAccessedPhotos -> {
                viewModelScope.launch {
                    photoPickerRepository.saveAccessedPhotos(context, intent.accessedPhotoList, intent.albumName)
                }
            }
            is PhotoPickerIntent.ShowFolderNameInputDialog -> {
                _state.update {
                    it.copy(
                        showFolderNameInputDialog = intent.isShow
                    )
                }
            }
        }
    }
}