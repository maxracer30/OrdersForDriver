package ru.maxstelmakh.ordersfordriver.data.yandexDiskS3Api

import okhttp3.MultipartBody
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRemoteRepository

class DefaultApiPhoto(
    private val apiPhoto: APIPhoto
) : PictureRemoteRepository {

    override suspend fun uploadPhoto(): Result<String> {
        return try {
            val response = apiPhoto.upload(
                media = MultipartBody.Part.create()
            )

            if (response.isSuccessful) {
                return Result.Success(data = response.body()?.href!!)
            } else Result.Failure(statusCode = response.code())
        } catch (e: Exception) {
            Result.Failure(statusCode = 0, message = e.message)
        }
    }
}