package ru.maxstelmakh.ordersfordriver.domain.usecases.photousecases

interface DeletePhotoUseCase {
    suspend operator fun invoke(name: String): Boolean
}