package com.example.userstory.ui.feature.photo

interface IPhotoRepository {
    suspend fun getPhoto(): String
    suspend fun savePhoto(photo: String): Boolean
}