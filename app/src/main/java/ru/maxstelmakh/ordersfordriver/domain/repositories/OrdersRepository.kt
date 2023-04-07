package ru.maxstelmakh.ordersfordriver.domain.repositories

import okhttp3.ResponseBody
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.ResponseOrder


interface OrdersRepository {
    suspend fun fetchOrders(): Result<Order>

    suspend fun sendOrder(order: ResponseOrder): Result<ResponseBody>
}