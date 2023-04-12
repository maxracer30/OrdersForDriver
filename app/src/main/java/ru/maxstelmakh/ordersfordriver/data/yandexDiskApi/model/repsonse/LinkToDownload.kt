package ru.maxstelmakh.ordersfordriver.data.yandexDiskApi.model.repsonse

import com.google.gson.annotations.SerializedName

data class LinkToDownload(
    @SerializedName("href") val href: String?,
    @SerializedName("method") val method: String?,
    @SerializedName("templated") val templated: Boolean?
)