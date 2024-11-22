package com.example.userstory.utils

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log

// 미디어 폴더/파일 접근 함수 (이미지 파일만)
// 허용된 이미지들을 앨범별로 정리 데이터를 가져옴
fun accessAllMediaFilesGroupedByFolder(context: Context) {
    // 필요한 컬럼 지정
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 폴더 이름
        MediaStore.Images.Media.DATE_TAKEN          // 날짜
    )
    val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

    // 쿼리 실행
    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )

    val folderMap = mutableMapOf<String, MutableList<String>>()

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val folderColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        while (it.moveToNext()) {
            // 이미지 ID와 폴더 이름 추출
            val id = it.getLong(idColumn)
            val folderName = it.getString(folderColumn) ?: "Unknown"

            // 이미지 URI 생성
            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            // 폴더별로 URI 추가
            folderMap.computeIfAbsent(folderName) { mutableListOf() }.add(uri.toString())
        }
    }

    if (folderMap.isNotEmpty()) {
        folderMap.forEach { (folder, uris) ->
            Log.e("MediaAccess", "Folder: $folder, Image Count: ${uris.size}")
            uris.forEach { uri ->
                Log.e("MediaAccess", "Image URI: $uri")
            }
        }
    } else {
        Log.e("MediaAccess", "No media files found.")
    }
}