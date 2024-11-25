package com.example.userstory.ui.feature.photo.bean

import androidx.annotation.Keep

@Keep
data class DecoItem(
    val name: String,
    val svgImageUrl: String
)

@Keep
data class FirebaseFileListResponse(
    val items: List<DecoItem>
)


