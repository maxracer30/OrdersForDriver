package ru.maxstelmakh.ordersfordriver.domain.repositories

import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result


interface OrdersRepository {
    suspend fun fetchOrders(): Result<Order>
}