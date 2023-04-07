package ru.maxstelmakh.ordersfordriver.data.yandexDiskApi.implusecases

import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.data.yandexDiskApi.model.repsonse.LinkToDownload
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRemoteRepository
import ru.maxstelmakh.ordersfordriver.domain.usecases.remotephotousecases.UploadToRemoteUseCase
import javax.inject.Inject

class DefaultUploadToRemote @Inject constructor(
    private val pictureRemoteRepository: PictureRemoteRepository
): UploadToRemoteUseCase {

    override suspend fun invoke(photoName: String): Result<LinkToDownload> {
        return pictureRemoteRepository.uploadPhoto(photoName = photoName)
    }

}