package ru.maxstelmakh.ordersfordriver.domain.usecases.photousecases

import android.graphics.Bitmap

interface LoadPhotoUseCase {

    suspend operator fun invoke(name: String) : Bitmap?
}