package ru.maxstelmakh.ordersfordriver.domain.repository

import ru.maxstelmakh.ordersfordriver.data.model.Order
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result


interface OrdersRepository {
    suspend fun fetchOrders(): Result<Order>
}