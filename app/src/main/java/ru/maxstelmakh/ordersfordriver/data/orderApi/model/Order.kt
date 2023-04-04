package ru.maxstelmakh.ordersfordriver.data.orderApi.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order(
    @SerializedName("order_id") var orderId: Int? = null,
    @SerializedName("order_num") var orderNum: String? = null,
    @SerializedName("order_date") var orderDate: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("goods") var goods: List<Goods>? = null,
)
