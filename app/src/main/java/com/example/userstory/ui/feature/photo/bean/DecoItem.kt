package com.example.userstory.ui.feature.photo.bean

import androidx.annotation.Keep

@Keep
data class DecoItem(
    val name: String,
    val downloadTokens: String? = null, // Firebase Storage SDK에서는 필요 없음
    val url: String // 다운로드 URL
)

@Keep
data class FirebaseFileListResponse(
    val items: List<DecoItem>
)


