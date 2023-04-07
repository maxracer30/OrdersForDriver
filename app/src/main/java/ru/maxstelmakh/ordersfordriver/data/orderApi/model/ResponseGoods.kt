package ru.maxstelmakh.ordersfordriver.data.orderApi.model

import com.google.gson.annotations.SerializedName

data class ResponseGoods(
    @SerializedName("article") var article: Long,
    @SerializedName("name") var name: String,
    @SerializedName("quantity") var quantity: Int,
    @SerializedName("price") var price: Double,
    @SerializedName("summ") var summ: Double,
    @SerializedName("reason") var reason: String,
    @SerializedName("photoLink") var photoLink: String
)