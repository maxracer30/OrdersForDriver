package ru.maxstelmakh.ordersfordriver.data.yandexDiskApi

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import ru.maxstelmakh.ordersfordriver.data.yandexDiskApi.model.repsonse.LinkToDownload
import ru.maxstelmakh.ordersfordriver.data.yandexDiskApi.model.repsonse.LinkToUpload

interface APIPhoto {

    @GET("upload")
    suspend fun connectToServer(
        @Header("Authorization") token: String = "OAuth y0_AgAAAABpwqZqAADLWwAAAADgNtgSwcGKD2VwQSeOYvIPeZWpnSVrFpY",
        @Query("path") path: String,
    ): Response<LinkToUpload>

    @Multipart
    @PUT
    suspend fun uploadFile(
        @Url url: String,
        @Part media: MultipartBody.Part,
    ): Response<ResponseBody>

    @GET("download/")
    suspend fun getLinkToDownload(
        @Header("Authorization") token: String = "OAuth y0_AgAAAABpwqZqAADLWwAAAADgNtgSwcGKD2VwQSeOYvIPeZWpnSVrFpY",
        @Query("path") path: String
    ): Response<LinkToDownload>
}