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

    private val _state = MutableStateFlow(PhotoPickerState())
    val state: StateFlow<PhotoPickerState> = _state

    fun handleIntent(intent: PhotoPickerIntent) {
        when (intent) {
            is PhotoPickerIntent.SetAccessablePhotos -> {
                // 이미 포토피커로 가져온 이미지 제외
                if (!state.value.accessablePhotoList.contains(intent.accessablePhoto)) {

                    viewModelScope.launch {

                        _state.update {
                            it.copy(
                                accessablePhotoList = it.accessablePhotoList + intent.accessablePhoto
                            )
                        }

                        photoPickerRepository.setAccessablePhotos(context, state.value.accessablePhotoList)
                    }
                }
            }
            is PhotoPickerIntent.SetAccessedPhotos -> {

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            accessedPhotoList = intent.accessedPhotoList
                        )
                    }

                    photoPickerRepository.setAccessedPhotos(context, state.value.accessedPhotoList)
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