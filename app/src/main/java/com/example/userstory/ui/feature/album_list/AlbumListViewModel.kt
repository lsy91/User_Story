package com.example.userstory.ui.feature.album_list

import androidx.lifecycle.ViewModel
import com.example.userstory.di.ShimmerModule
import com.facebook.shimmer.Shimmer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumListViewModel @Inject constructor(): ViewModel() {
    fun provideShimmer(): Shimmer {
        return ShimmerModule.provideShimmer()
    }
}