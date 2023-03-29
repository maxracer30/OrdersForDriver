package ru.maxstelmakh.ordersfordriver.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order(
    @SerializedName("order_id") val orderId: Int? = null,
    @SerializedName("order_num") val orderNum: String? = null,
    @SerializedName("order_date") val orderDate: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("goods") val goods: List<Goods>? = null,
) : Serializable
