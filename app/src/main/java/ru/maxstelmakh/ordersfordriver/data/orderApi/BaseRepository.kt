package ru.maxstelmakh.ordersfordriver.data.orderApi

import okhttp3.ResponseBody
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.ResponseOrder
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
            } else Result.Failure(statusCode = response.code())
        } catch (e: Exception) {
            Result.Failure(statusCode = 601, message = e.message)
        }
    }

    override suspend fun sendOrder(order: ResponseOrder): Result<ResponseBody> {
        return try {

            val response = apiOrders.orderComplete(order = order).execute()

            if (response.isSuccessful) {
                Result.Success(data = response.body()!!)
            } else {
                Result.Failure(statusCode = response.code())
            }
        } catch (e: Exception) {
            Result.Failure(statusCode = 602, message = e.message)
        }
    }
}