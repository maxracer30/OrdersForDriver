package ru.maxstelmakh.ordersfordriver.data.model

data class Goods(
val article: Long,
val name: String,
val quantity: Int,
val price: Float,
val summ: Float
)
