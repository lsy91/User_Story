package com.example.userstory.ui.feature.album_list

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
class AlbumListViewModel @Inject constructor(
    private val albumListRepository: IAlbumListRepository
): ViewModel() {

    private val _state = MutableStateFlow(AlbumListState())
    val state: StateFlow<AlbumListState> = _state

    fun handleIntent(intent: AlbumListIntent) {
        when (intent) {
            is AlbumListIntent.LoadMyAlbums -> loadAlbums("My")
            is AlbumListIntent.LoadAllAlbums -> loadAlbums("All")
        }
    }

    private fun loadAlbums(loadCase: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isAlbumListLoading = true
                )
            }

            try {

                val albums =
                    if (loadCase.equals("My", ignoreCase = true)) {
                        albumListRepository.loadMyAlbums()
                    } else {
                        albumListRepository.loadAlbumsGroupedByFolder()
                    }

                _state.update {
                    it.copy(
                        isAlbumListLoading = false,
                        albums = albums
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isAlbumListLoading = false
                    )
                }
            }
        }
    }

    fun provideShimmer(): Shimmer {
        return ShimmerModule.provideShimmer()
    }
}