package ru.mirus

import android.graphics.Bitmap

object ImageCache {
    private val imageMap = mutableMapOf<String, Bitmap>()

    fun putImage(key: String, bitmap: Bitmap) {
        imageMap[key] = bitmap
    }

    fun getImage(key: String): Bitmap? {
        return imageMap[key]
    }
}
