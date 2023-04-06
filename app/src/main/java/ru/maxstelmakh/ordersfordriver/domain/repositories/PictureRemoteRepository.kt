package ru.maxstelmakh.ordersfordriver.domain.repositories

import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.data.yandexDiskS3Api.model.repsonse.LinkToDownload

interface PictureRemoteRepository {

    suspend fun uploadPhoto(pictureName: String): Result<LinkToDownload>

}