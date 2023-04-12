package ru.maxstelmakh.ordersfordriver.domain.usecases.photousecases

import android.graphics.Bitmap

interface SavePhotoUseCase {
    suspend operator fun invoke(name: String, bitmap: Bitmap): Boolean
}