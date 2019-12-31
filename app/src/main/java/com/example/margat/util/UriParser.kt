package com.example.margat.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.loader.content.CursorLoader

class UriParser {
    companion object {
        fun getRealPathFromURI(application: Context, fileUri: Uri): String? {
            var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
            var loader = CursorLoader(application, fileUri, proj, null, null, null)
            var cursor = loader.loadInBackground()
            var columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            var result = cursor?.getString(columnIndex!!)
            cursor?.close()
            return result
        }
    }
}