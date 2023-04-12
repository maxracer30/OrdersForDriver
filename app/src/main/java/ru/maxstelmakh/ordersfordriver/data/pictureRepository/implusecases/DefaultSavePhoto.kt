package ru.maxstelmakh.ordersfordriver.data.pictureRepository.implusecases

import android.graphics.Bitmap
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRepository
import ru.maxstelmakh.ordersfordriver.domain.usecases.photousecases.SavePhotoUseCase
import javax.inject.Inject

class DefaultSavePhoto @Inject constructor(
    private val pictureRepository: PictureRepository,
) : SavePhotoUseCase {
    override suspend operator fun invoke(name: String, bitmap: Bitmap): Boolean {
        return pictureRepository.savePhoto(name = name, bitmap = bitmap)
    }
}