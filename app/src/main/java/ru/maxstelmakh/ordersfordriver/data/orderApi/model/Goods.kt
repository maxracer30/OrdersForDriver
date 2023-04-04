package ru.maxstelmakh.ordersfordriver.data.orderApi.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Goods(
    @SerializedName("article") var article: Long,
    @SerializedName("name") var name: String,
    @SerializedName("quantity") var quantity: Int,
    @SerializedName("price") var price: Double,
    @SerializedName("summ") var summ: Double
)
