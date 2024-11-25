package com.example.userstory.ui.common

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserStoryPreferences", Context.MODE_PRIVATE)

    companion object {
        // 커스텀 포토피커에서 사용자가 지정한 접근 가능 이미지 URI 리스트
        private const val ACCESSABLE_PHOTOS_KEY = "ACCESSABLE_PHOTOS"
        // 권한 부여 완료되어서 My Album 탭에서 보여줄 이미지 URI 리스트
        private const val ACCESSED_PHOTOS_KEY = "ACCESSED_PHOTOS"
    }

    // 사진 URI 리스트 저장
    fun saveUriList(key: String, uriList: List<Uri>) {
        val uriStrings = uriList.map { it.toString() }.toSet() // 중복 방지
        sharedPreferences.edit().putStringSet(key, uriStrings).apply()
    }

    // 사진 URI 리스트 불러오기
    fun loadUriList(key: String): List<Uri> {
        val uriStrings = sharedPreferences.getStringSet(key, emptySet()) ?: emptySet()
        return uriStrings.map { Uri.parse(it) }
    }

    fun saveAccessablePhotos(uriList: List<Uri>) {
        saveUriList(ACCESSABLE_PHOTOS_KEY, uriList)
    }

    fun saveAccessedPhotos(uriList: List<Uri>) {
        saveUriList(ACCESSED_PHOTOS_KEY, uriList)
    }

    fun loadAccessablePhotos(): List<Uri> {
        return loadUriList(ACCESSABLE_PHOTOS_KEY)
    }

    fun loadAccessedPhotos(): List<Uri> {
        return loadUriList(ACCESSED_PHOTOS_KEY)
    }
}