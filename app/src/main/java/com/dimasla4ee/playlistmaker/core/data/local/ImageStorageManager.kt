package com.dimasla4ee.playlistmaker.core.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

class ImageStorageManager(private val context: Context) {

    fun saveImage(uri: Uri): String {
        val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "covers")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val fileName = "cover_${System.currentTimeMillis()}.jpg"
        val file = File(dir, fileName)

        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.absolutePath
    }
}