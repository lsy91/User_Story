package com.example.userstory.ui.feature.album_list.bean

import androidx.annotation.Keep

@Keep
data class Album(
    val name: String,
    val photos: List<String>,
    val photoCount: Int,
)
