package com.example.userstory.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

open class PickContent : ActivityResultContract<Unit, Uri?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode == Activity.RESULT_OK) {
            return intent?.data
        }
        return null
    }
}