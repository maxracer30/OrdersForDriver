package ru.maxstelmakh.ordersfordriver.data.orderApi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.ResponseOrder
import ru.maxstelmakh.ordersfordriver.domain.repositories.OrdersRepository
import javax.inject.Inject

class BaseRepository @Inject constructor(
    private val apiOrders: APIOrders,
) : OrdersRepository {
    companion object {
        private const val FETCH_FAILURE_CODE = 601
        private const val SEND_FAILURE_CODE = 602
    }

    override suspend fun fetchOrders(): Result<Order> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiOrders.order()

                if (response.isSuccessful) {
                    return@withContext Result.Success(data = response.body()!!)
                } else Result.Failure(statusCode = response.code())
            } catch (e: Exception) {
                Result.Failure(
                    statusCode = FETCH_FAILURE_CODE,
                    message = "Fetch order failure, message: ${e.message}"
                )
            }
        }
    }

    override suspend fun sendOrder(order: ResponseOrder): Result<ResponseBody> {
        return withContext(Dispatchers.IO) {
            try {

                val response = apiOrders.orderComplete(order = order)

                if (response.isSuccessful) {
                    Result.Success(data = response.body()!!)
                } else {
                    Result.Failure(statusCode = response.code())
                }
            } catch (e: Exception) {
                Result.Failure(
                    statusCode = SEND_FAILURE_CODE,
                    message = "Send order failure, message: ${e.message}"
                )
            }
        }
    }
}