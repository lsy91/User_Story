package com.example.userstory.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

open class PickContent : ActivityResultContract<Unit, List<Uri>>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 다중 선택 활성화
            addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            )
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
        if (resultCode == Activity.RESULT_OK) {
            intent?.let {
                val clipData = it.clipData
                if (clipData != null) {
                    // 다중 선택
                    val uris = mutableListOf<Uri>()
                    for (i in 0 until clipData.itemCount) {
                        uris.add(clipData.getItemAt(i).uri)
                    }
                    return uris
                }
                // 단일 선택
                it.data?.let { uri -> return listOf(uri) }
            }
        }
        return emptyList() // 선택되지 않은 경우 빈 리스트 반환
    }
}