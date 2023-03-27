package ru.maxstelmakh.ordersfordriver.data.model

import com.google.gson.annotations.SerializedName

data class Goods(
    @SerializedName("article") val article: Long,
    @SerializedName("name") val name: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Float,
    @SerializedName("summ") val summ: Float
)
