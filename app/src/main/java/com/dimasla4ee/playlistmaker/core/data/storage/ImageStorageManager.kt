package com.dimasla4ee.playlistmaker.core.data.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

class ImageStorageManager(private val context: Context) {

    fun saveImage(uri: Uri): String {
        val externalDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val outputDirectory = File(
            externalDirectory,
            DIRECTORY_NAME
        ).also { directory ->
            if (!directory.exists()) {
                directory.mkdirs()
            }
        }

        val fileName = "cover_${System.currentTimeMillis()}.jpg"
        val file = File(outputDirectory, fileName)

        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, outputStream)

        return file.absolutePath
    }

    companion object {
        private const val DIRECTORY_NAME = "covers"
        private const val COMPRESSION_QUALITY = 30
    }

}