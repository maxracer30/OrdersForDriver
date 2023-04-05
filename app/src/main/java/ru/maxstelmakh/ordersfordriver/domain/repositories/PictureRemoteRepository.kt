package ru.maxstelmakh.ordersfordriver.domain.repositories

import ru.maxstelmakh.ordersfordriver.data.orderApi.Result

interface PictureRemoteRepository {

    suspend fun uploadPhoto(): Result<String>

}