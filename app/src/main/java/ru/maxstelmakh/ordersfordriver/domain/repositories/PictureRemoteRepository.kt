package ru.maxstelmakh.ordersfordriver.domain.repositories

import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.data.yandexDiskApi.model.repsonse.LinkToDownload

interface PictureRemoteRepository {

    suspend fun uploadPhoto(photoName: String): Result<LinkToDownload>

}