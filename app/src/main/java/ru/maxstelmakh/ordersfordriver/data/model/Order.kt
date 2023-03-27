package ru.maxstelmakh.ordersfordriver.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order(
    @SerializedName("order_id") val orderId: Int,
    @SerializedName("order_num") val orderNum: String,
    @SerializedName("order_date") val orderDate: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("goods") val goods: List<Goods>
) : Serializable
