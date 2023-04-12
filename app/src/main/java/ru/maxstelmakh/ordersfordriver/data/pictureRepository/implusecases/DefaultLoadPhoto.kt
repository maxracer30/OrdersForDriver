package ru.maxstelmakh.ordersfordriver.data.pictureRepository.implusecases

import android.graphics.Bitmap
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRepository
import ru.maxstelmakh.ordersfordriver.domain.usecases.photousecases.LoadPhotoUseCase
import javax.inject.Inject

class DefaultLoadPhoto @Inject constructor(
    private val repository: PictureRepository,
) : LoadPhotoUseCase {
    override suspend operator fun invoke(name: String): Bitmap? {
        return repository.loadPhoto(name = name)
    }
}