package ru.maxstelmakh.ordersfordriver.domain.usecases.remotephotousecases

import ru.maxstelmakh.ordersfordriver.data.yandexDiskApi.model.repsonse.LinkToDownload
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result

interface UploadToRemoteUseCase {

    suspend operator fun invoke(photoName: String): Result<LinkToDownload>
}