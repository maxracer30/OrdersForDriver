package ru.maxstelmakh.ordersfordriver.data.model

import java.io.Serializable

data class Order(
val order_id : Int,
val order_num: String,
val order_date: String,
val phone: String,
val goods: List<Goods>
) : Serializable
