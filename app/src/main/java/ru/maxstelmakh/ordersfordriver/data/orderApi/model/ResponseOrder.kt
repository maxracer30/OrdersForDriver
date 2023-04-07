package ru.maxstelmakh.ordersfordriver.data.orderApi.model

import com.google.gson.annotations.SerializedName

data class ResponseOrder(
    @SerializedName("order_id") var orderId: Int? = null,
    @SerializedName("order_num") var orderNum: String? = null,
    @SerializedName("order_date") var orderDate: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("goods") var goods: List<ResponseGoods>? = null,
)
