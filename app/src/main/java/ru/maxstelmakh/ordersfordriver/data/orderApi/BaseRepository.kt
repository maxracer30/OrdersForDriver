package ru.maxstelmakh.ordersfordriver.data.orderApi

import ru.maxstelmakh.ordersfordriver.data.model.Order
import ru.maxstelmakh.ordersfordriver.domain.repository.OrdersRepository
import javax.inject.Inject

class BaseRepository @Inject constructor(
    private val apiOrders: APIOrders
) : OrdersRepository {
    override suspend fun fetchOrders(): Result<List<Order>> {
        return try {
            val response = apiOrders.orders().execute()

            when (response.isSuccessful) {
                true -> Result.Success(data = response.body()!!)
                else -> Result.Failure(statusCode = response.code())
            }
        } catch (e: Exception) {
            Result.Failure(statusCode = 0, message = e.message)
        }
    }
}