package ru.maxstelmakh.ordersfordriver.data.yandexDiskS3Api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.maxstelmakh.ordersfordriver.data.yandexDiskS3Api.model.LinkToUpload

interface APIPhoto {

    @Multipart
    @POST("api/1.0/media.upload")
    suspend fun upload(
        @Part("Authorization") token: String = "OAuth y0_AgAAAABpwqZqAADLWwAAAADgNtgSwcGKD2VwQSeOYvIPeZWpnSVrFpY",
        @Part media: MultipartBody.Part
    ): Response<LinkToUpload>

}