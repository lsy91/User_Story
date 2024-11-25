package com.example.userstory.ui.feature.photo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userstory.di.ShimmerModule
import com.example.userstory.ui.feature.album_list.bean.Album
import com.facebook.shimmer.Shimmer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val photoListRepository: IPhotoListRepository
): ViewModel() {

    private val _state = MutableStateFlow(PhotoListState())
    val state: StateFlow<PhotoListState> = _state

    fun handleIntent(intent: PhotoListIntent, album: Album? = null) {
        when (intent) {
            is PhotoListIntent.ChangeToolbarTitle -> {
                _state.update {
                    it.copy(toolbarTitle = intent.title ?: "")
                }
            }
            is PhotoListIntent.LoadPhotoList -> {
                loadPhotoList(album)
            }
        }
    }

    private fun loadPhotoList(album: Album?) {
        viewModelScope.launch {
            _state.update {
                it.copy(isPhotoListLoading = true)
            }

            try {
                val photoList = photoListRepository.loadPhotoList(album)
                _state.update {
                    it.copy(
                        isPhotoListLoading = false,
                        photoList = photoList,
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isPhotoListLoading = false,
                    )
                }
            }
        }
    }

    fun provideShimmer(): Shimmer {
        return ShimmerModule.provideShimmer()
    }
}