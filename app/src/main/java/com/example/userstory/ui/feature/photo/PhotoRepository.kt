package com.example.userstory.ui.feature.photo

import javax.inject.Inject

class PhotoRepository @Inject constructor() : IPhotoRepository {
    override suspend fun getPhoto(): String {
        return ""
    }

    override suspend fun savePhoto(photo: String): Boolean {
        return false
    }

}