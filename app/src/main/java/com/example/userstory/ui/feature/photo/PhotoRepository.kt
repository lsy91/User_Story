package com.example.userstory.ui.feature.photo

import com.example.userstory.ui.feature.photo.bean.DecoItem
import com.example.userstory.ui.feature.photo.bean.FirebaseFileListResponse
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PhotoRepository @Inject constructor() : IPhotoRepository {

    private val storage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    override suspend fun getDecoImages(path: String): FirebaseFileListResponse {
        // Firebase Storage의 경로 참조
        val folderRef: StorageReference = storage.reference.child(path)

        // 폴더 내의 파일 목록 가져오기
        val result = folderRef.listAll().await()

        // 다운로드 URL 생성 및 파일 리스트 반환
        val itemsWithUrls = result.items.map { fileRef ->
            val downloadUrl = fileRef.downloadUrl.await().toString()
            DecoItem(
                name = fileRef.name,
                downloadTokens = null, // Firebase Storage SDK에서는 필요 없음
                url = downloadUrl
            )
        }

        return FirebaseFileListResponse(itemsWithUrls)
    }
}