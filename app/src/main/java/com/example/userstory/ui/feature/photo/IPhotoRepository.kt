package com.example.userstory.ui.feature.photo

import com.example.userstory.ui.feature.photo.bean.FirebaseFileListResponse

interface IPhotoRepository {
    suspend fun getDecoImages(path: String): FirebaseFileListResponse
}