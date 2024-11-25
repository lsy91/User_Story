package com.example.userstory.di

import com.example.userstory.ui.feature.album_list.AlbumListRepository
import com.example.userstory.ui.feature.album_list.IAlbumListRepository
import com.example.userstory.ui.feature.photo.IPhotoRepository
import com.example.userstory.ui.feature.photo.PhotoRepository
import com.example.userstory.ui.feature.photo_list.IPhotoListRepository
import com.example.userstory.ui.feature.photo_list.PhotoListRepository
import com.example.userstory.ui.feature.photo_picker.IPhotoPickerRepository
import com.example.userstory.ui.feature.photo_picker.PhotoPickerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindAlbumListRepository(
        albumListRepository: AlbumListRepository
    ): IAlbumListRepository

    @Binds
    abstract fun bindPhotoListRepository(
        photoListRepository: PhotoListRepository
    ): IPhotoListRepository

    @Binds
    abstract fun bindPhotoRepository(
        photoRepository: PhotoRepository
    ): IPhotoRepository

    @Binds
    abstract fun bindPhotoPickerRepository(
        photoPickerRepository: PhotoPickerRepository
    ): IPhotoPickerRepository
}