package ru.maxstelmakh.ordersfordriver.domain.repositories

import android.graphics.Bitmap

interface PictureRepository {
    suspend fun savePhoto(name: String, bitmap: Bitmap): Boolean

    suspend fun loadPhoto(name: String): Bitmap?

    suspend fun deletePhoto(name: String): Boolean
}