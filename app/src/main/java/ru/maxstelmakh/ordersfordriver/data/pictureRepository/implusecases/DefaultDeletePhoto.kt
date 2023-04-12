package ru.maxstelmakh.ordersfordriver.data.pictureRepository.implusecases

import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRepository
import ru.maxstelmakh.ordersfordriver.domain.usecases.photousecases.DeletePhotoUseCase
import javax.inject.Inject

class DefaultDeletePhoto @Inject constructor(
    private val repository: PictureRepository,
) : DeletePhotoUseCase {

    override suspend operator fun invoke(name: String): Boolean {
        return repository.deletePhoto(name = name)
    }
}