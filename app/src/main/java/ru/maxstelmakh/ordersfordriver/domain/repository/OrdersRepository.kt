package ru.maxstelmakh.ordersfordriver.domain.repository

import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result


interface OrdersRepository {
    suspend fun fetchOrders(): Result<Order>
}