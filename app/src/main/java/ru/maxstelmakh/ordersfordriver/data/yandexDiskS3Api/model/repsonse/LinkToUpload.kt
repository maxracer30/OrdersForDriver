package ru.maxstelmakh.ordersfordriver.data.yandexDiskS3Api.model.repsonse

import com.google.gson.annotations.SerializedName

data class LinkToUpload(
@SerializedName("operation_name") val operation_id: String,
@SerializedName("href") val href: String,
@SerializedName("method") val method: String,
@SerializedName("templated") val templated: Boolean
)