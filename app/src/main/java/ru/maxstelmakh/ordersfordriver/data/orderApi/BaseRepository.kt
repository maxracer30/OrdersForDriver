package ru.maxstelmakh.ordersfordriver.data.orderApi

import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import ru.maxstelmakh.ordersfordriver.domain.repositories.OrdersRepository
import javax.inject.Inject

class BaseRepository @Inject constructor(
    private val apiOrders: APIOrders
) : OrdersRepository {
    override suspend fun fetchOrders(): Result<Order> {
        return try {
            val response = apiOrders.order().execute()

            if (response.isSuccessful) {
                return Result.Success(data = response.body()!!)
            }
            else Result.Failure(statusCode = response.code())
        } catch (e: Exception) {
            Result.Failure(statusCode = 0, message = e.message)
        }
    }
}