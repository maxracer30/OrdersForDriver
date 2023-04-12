package ru.maxstelmakh.ordersfordriver.data.orderApi.model

import com.google.gson.annotations.SerializedName

data class ResponseGoods(
    @SerializedName("article") val article: Long,
    @SerializedName("name") val name: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Double,
    @SerializedName("summ") val summ: Double,
    @SerializedName("reason") val reason: String,
    @SerializedName("photoLink") val photoLink: String,
)