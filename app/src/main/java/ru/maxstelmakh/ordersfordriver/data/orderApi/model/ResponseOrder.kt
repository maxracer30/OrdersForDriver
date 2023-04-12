package ru.maxstelmakh.ordersfordriver.data.orderApi.model

import com.google.gson.annotations.SerializedName

data class ResponseOrder(
    @SerializedName("order_id") val orderId: Int? = null,
    @SerializedName("order_num") val orderNum: String? = null,
    @SerializedName("order_date") val orderDate: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("goods") val goods: List<ResponseGoods>? = null,
)
